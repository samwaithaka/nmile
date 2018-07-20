package com.nextramile.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.nextramile.dao.CartDAO;
import com.nextramile.dao.CartItemDAO;
import com.nextramile.dao.CustomerDAO;
import com.nextramile.dao.WishListDAO;
import com.nextramile.dao.WishListItemDAO;
import com.nextramile.models.Customer;
import com.nextramile.models.CustomerOrder;
import com.nextramile.models.Product;
import com.nextramile.models.ShoppingCart;
import com.nextramile.models.ShoppingCartItem;
import com.nextramile.models.WishList;
import com.nextramile.models.WishListItem;
import com.nextramile.util.Emailer;

@ManagedBean(name = "customerController", eager = true)
@SessionScoped
public class CustomerController {

	private Customer customer;
	private ShoppingCart shoppingCart;
	private ShoppingCartItem shoppingCartItem;
	private WishList wishList;
	private WishListItem wishListItem;
	private List<CustomerOrder> customerOrderList;
	private Product product;
	private String form = "check";
	private String customerAction;
	private int refId = 0;

	@PostConstruct
	public void init() {
		customer = new Customer();
		product = new Product();
		shoppingCart = new ShoppingCart();
		shoppingCartItem = new ShoppingCartItem();
		wishList = new WishList();
		wishListItem = new WishListItem();
	}

	public CustomerController() {
	}

	public void refresh() {
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext()
				.getRequestParameterMap();
		String ref = params.get("ref");
		String token = null;
		String email = null;

		email = params.get("e");
		token = params.get("t");
		form = params.get("f") != null ? params.get("f") : "check";
		try {
			if(email != null) email = URLDecoder.decode(email, "UTF-8");
			if(token != null) token = URLDecoder.decode(token, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if(email != null) {
			customer = new Customer();
			customer.setEmail(email);
		}
		if(token != null) {
			customer.setPasswordResetToken(token);
			reset();
		}
		if(ref != null) {
			refId = Integer.parseInt(ref);
		}
		if(customer.getId() > 0) {
			//customerOrderList = OrderDAO.getCustomerOrderList(customer);
		}
	}

	public String createCustomer() {
		customer.setReferer(CustomerDAO.find(refId));
		CustomerDAO.addCustomer(customer);
		return "account.xhtml?faces-redirect=true";
	}

	public void queryCustomerEmail() {		
		customer = CustomerDAO.findByEmail(customer.getEmail());	
		if(customer.getId() > 0) {	
			form = "login";
		} else {
			customer.setPassword("password");
			customer = CustomerDAO.addCustomer(customer);
			form = "signup";
		}
		System.out.println(product);
		System.out.println(customerAction);
		if(product.getId() > 0) {
			if(customerAction.equalsIgnoreCase("shoppingCart")) {
				System.out.println("shopping cart");
				shoppingCart.setCustomer(customer);
				shoppingCart = CartDAO.addShoppingCart(shoppingCart);
				shoppingCartItem.setShoppingCart(shoppingCart);
				shoppingCartItem.setProduct(product);
				CartItemDAO.addShoppingCartItem(shoppingCartItem);
			} else if(customerAction.equalsIgnoreCase("wishList")) {
				System.out.println("wish list");
				wishList.setCustomer(customer);
				wishList = WishListDAO.addWishList(wishList);
				wishListItem.setWishList(wishList);
				wishListItem.setProduct(product);
				WishListItemDAO.addWishListItem(wishListItem);
			}
		}
	}

	public String updateCustomer() {
		CustomerDAO.updateCustomer(customer);
		return redirectUser();
	}

	public String login() {
		String page = null;
		String email = customer.getEmail();
		customer = CustomerDAO.login(customer);
		if(customer.getId() > 0) {
			page = redirectUser();
		} else {
			customer.setEmail(email);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username/Password Error","Username and password used do not match");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return page;
	}
    
	public void logout() throws IOException {
	    customer = new Customer();
	    form = "login";
	    FacesContext.getCurrentInstance().getExternalContext().redirect("user.xhtml");
	}
	
	public String redirectUser() {
		String page = null;
		shoppingCart = CartDAO.findPendingShoppingCart(customer);
		if(shoppingCart.getId() > 0) {
			shoppingCart.setShoppingCartItems(CartItemDAO.getCartItems(shoppingCart));
			page =  "checkout.xhtml?faces-redirect=true";
		} else {
			page = "account.xhtml?faces-redirect=true";
		}
		return page;
	}

	public void generatePasswordResetToken() {
		FacesMessage fm = null;
		String email = customer.getEmail();
		customer = CustomerDAO.findByEmail(email);
		if(email == null) {
			fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email Required","Someone forgot to specify email ;-)");
		} else if(customer.getId() == 0) {
			fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email Not Found","The email address is not recognized.");
		} else {
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			String url;
			try {
				url = "http://localhost:8080/nextramile/reset.xhtml?t=" + URLEncoder.encode(ts.toString(), "UTF-8") + "&e=" + URLEncoder.encode(customer.getEmail(),"UTF-8");
				String subject = "Password Reset";
				String salutation = null;
				if(customer.getCustomerName() != null) {
					salutation = "Hello " + customer.getCustomerName() + ",\n\n";
				}
				String message = salutation +
						"You have requested to reset your password. Click on the link below "
						+ "to proceed with reset. Incase you didn't initiate reset, just ignore the email.\n\n"
						+ "Reset link: " + url;
				customer.setPasswordResetToken(ts.toString());
				CustomerDAO.updateCustomer(customer);
				customer.setPasswordResetToken(null);
				fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Check Your Mail","A password reset link has been sent to " + customer.getEmail());
				Emailer.send("samwaithaka@gmail.com", customer.getEmail(), subject, message);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FacesContext.getCurrentInstance().addMessage(null, fm);
	}

	public void reset() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try {
			Date parsedDate = dateFormat.parse(customer.getPasswordResetToken());
			Timestamp resetTime = new java.sql.Timestamp(parsedDate.getTime());

			Timestamp now = new Timestamp(System.currentTimeMillis());
			Long diff = now.getTime() - resetTime.getTime();
			Long diffHours = diff / (60 * 60 * 1000);
			if(diffHours > 1) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Expired","Token is expired. Request reset again");
				FacesContext.getCurrentInstance().addMessage(null, message);
			} else {
				customer = CustomerDAO.getCustomerByToken(customer);
				if(customer.getId() > 0) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Change","Go ahead and change password");
					FacesContext.getCurrentInstance().addMessage(null, message);
				} else {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error","Error Occured. Make sure you copied the right link from your email");
					FacesContext.getCurrentInstance().addMessage(null, message);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String changePassword() {
		String view = null;
		System.out.println(customer);
		if(customer.getPassword().equals(customer.getPasswordConfirm())) {
			CustomerDAO.changePassword(customer);
			view = "home.xhtml?faces-redirect=true";
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful","Password Successully changed");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mismatch","The passwords did not match, please try again");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return view;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}	
	
	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	public ShoppingCartItem getShoppingCartItem() {
		return shoppingCartItem;
	}

	public void setShoppingCartItem(ShoppingCartItem shoppingCartItem) {
		this.shoppingCartItem = shoppingCartItem;
	}

	public WishList getWishList() {
		return wishList;
	}

	public void setWishList(WishList wishList) {
		this.wishList = wishList;
	}

	public WishListItem getWishListItem() {
		return wishListItem;
	}

	public void setWishListItem(WishListItem wishListItem) {
		this.wishListItem = wishListItem;
	}

	public List<CustomerOrder> getCustomerOrderList() {
		return customerOrderList;
	}

	public void setCustomerOrderList(List<CustomerOrder> customerOrderList) {
		this.customerOrderList = customerOrderList;
	}

	public String getCustomerAction() {
		return customerAction;
	}

	public void setCustomerAction(String customerAction) {
		this.customerAction = customerAction;
	}
}
