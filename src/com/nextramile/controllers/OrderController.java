package com.nextramile.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

import com.nextramile.dao.CartDAO;
import com.nextramile.dao.CustomerAddressDAO;
import com.nextramile.dao.DeliveryAddressDAO;
import com.nextramile.dao.OrderDAO;
import com.nextramile.dao.WishListDAO;
import com.nextramile.dao.WishListItemDAO;
import com.nextramile.models.Customer;
import com.nextramile.models.CustomerAddress;
import com.nextramile.models.CustomerOrder;
import com.nextramile.models.DeliveryAddress;
import com.nextramile.models.Location;
import com.nextramile.models.Product;
import com.nextramile.models.ShoppingCart;
import com.nextramile.models.ShoppingCartItem;
import com.nextramile.models.WishList;
import com.nextramile.models.WishListItem;
import com.nextramile.util.Configs;
import com.nextramile.util.Emailer;

@ManagedBean(name = "orderController", eager = true)
@SessionScoped
public class OrderController {
    
	private CustomerOrder order;
	private DeliveryAddress deliveryAddress;
	private CustomerAddress customerAddress;
	private List<CustomerAddress> customerAddressList;
	private String addressText;
	private Product mainProduct;
	private String shareUrl;
	private String encodedShareUrl;
	private ShoppingCart shoppingCart;
	private boolean showAddressForm;
	
	@ManagedProperty(value = "#{productController}")
	private ProductController productController;

	@ManagedProperty(value = "#{customerController}")
	private CustomerController customerController;

	public OrderController() {		
	}
	
	@PostConstruct
	public void init() {
		order = new CustomerOrder();
		deliveryAddress = new DeliveryAddress();
		deliveryAddress.setLocation(new Location());
	}
	
	public void refresh() {
		Customer customer = customerController.getCustomer();
		customerAddress = CustomerAddressDAO.findCurrentCustomerAddress(customer);
		customerAddressList = CustomerAddressDAO.findAddressByCustomer(customer);
		if(customerAddress.getId() > 0) {
			addressText = "New delivery address?";
			showAddressForm = showAddressForm == true ? showAddressForm : false;
		} else {
			addressText = "Tell us where you are:";
		}
	}

	public void clearForm() {
		showAddressForm = true;
		deliveryAddress = new DeliveryAddress();
	}
	
	public void initializeAddress() {
		showAddressForm = true;
  	}
	
	public void revertAddress() { 
		showAddressForm = false;
	}
	
	public void deleteAddress() { 
		customerAddress = CustomerAddressDAO.findCustomerAddressByDeliveryAddress(deliveryAddress);
		if(customerAddress.getCurrent() == false) {
			customerAddress.setActive(false);
			deliveryAddress.setActive(false);
			CustomerAddressDAO.updateCustomerAddress(customerAddress);
			DeliveryAddressDAO.updateDeliveryAddress(deliveryAddress);
		} else {
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Address in Use","You can't delete current address");
			FacesContext.getCurrentInstance().addMessage(null, fm);
		}
	}
	
	public void edit() {
		PrimeFaces.current().ajax().update("addressform");
	}
	
	public String completePurchase() {
		order.setShoppingCart(shoppingCart);
		if(customerAddress.getId() > 0) {
			order.setDeliveryAddress(customerAddress.getDeliveryAddress());
			order.setCheckout(true);
			OrderDAO.addOrder(order);
			shoppingCart.setActive(false);
			List<ShoppingCartItem> shoppingCartItemList = shoppingCart.getShoppingCartItems();
			// Remove wishListItems for this customer product
			mainProduct = new Product();
			for(ShoppingCartItem cartItem: shoppingCartItemList) {
				// This product will be used as primary for reference blog, sharing
				mainProduct = mainProduct.getId() < 1 ? cartItem.getProduct() : mainProduct;
				WishListItem wishListItem = WishListItemDAO.getWishListItemByCustomerProduct(shoppingCart.getCustomer(), cartItem.getProduct());
				if(wishListItem != null) {
				    wishListItem.setActive(false);
				    WishListItemDAO.updateWishListItem(wishListItem);
				}
			}
			// If wishList is empty, remove it as well
			WishList wishList = WishListDAO.findPendingWishList(shoppingCart.getCustomer());
			if(wishList.getId() > 0) {
				if(wishList.getWishListItems().size() == 0) {
					wishList.setActive(false);
					WishListDAO.updateWishList(wishList);
				}
			}
			CartDAO.updateShoppingCart(shoppingCart);
			Customer customer = customerController.getCustomer();
			String subject = "Nextramile Order for " + customer.getCustomerName();
			StringBuilder builder = new StringBuilder();
			builder.append("<p>Dear " + customer.getCustomerName() + ",</p>");
			builder.append("You have successfully ordered the following items: <br />");
			StringBuilder table = new StringBuilder();
			table.append("<table style='width:100%;'>");
			table.append("<tr>");
			table.append("<th>&nbsp;</th>");
			table.append("<th>Product Name</th>");
			table.append("<th>Qty</th>");
			table.append("<th>Price</th>");
			table.append("<th>Total</th>");
			table.append("</tr>");
			int total = 0;
			for(ShoppingCartItem item : shoppingCart.getShoppingCartItems()) {
				total += item.getProduct().getPrice() * item.getQuantity();
				table.append("<tr>");
				table.append("<td style='width:14%;'><img src='https://www.nextramile.com/images/" + item.getProduct().getFileName() + "' style='width:55px;' alt='" + item.getProduct().getProductName() + "'/></td>");
				table.append("<td style='width:50%;'>" + item.getProduct().getProductName() + "</td>");
				table.append("<td style='width:8%;'>" + item.getQuantity() + "</td>");
				table.append("<td style='width:14%;'>" + item.getProduct().getPrice() + "</td>");
				table.append("<td style='width:14%;'>" + item.getProduct().getPrice() * item.getQuantity() + "</td>");
				table.append("</tr>");
			}
			table.append("<tr>");
			table.append("<td>&nbsp;</td>");
			table.append("<td colspan='3'>Delivery Charge</td>");
			int deliveryCharge = customerAddress.getDeliveryAddress().getLocation().getDeliveryFee();
			total += deliveryCharge;
			table.append("<td>" + deliveryCharge + "</td>");
			table.append("</tr>");
			table.append("<tr>");
			table.append("<td>&nbsp;</td>");
			table.append("<td colspan='3' style='border-top:solid 1px #555;'>Grand Total</td>");
			table.append("<td style='border-top:solid 1px #555;'><b>" + total + "</b></td>");
			table.append("</tr>");
			table.append("</table>");
			builder.append(table.toString());
			builder.append("<br />Delivery to your address is being processed, be ready to pay as soon as you recieve your delivery.");
			String message = builder.toString();
			Emailer.send(Configs.getConfig("adminemail"), 
					"'" + customer.getCustomerName() + "'<" + customer.getEmail() + ">", 
					subject, message);
			String queryString = ";" + customer.getId();
			shareUrl = Configs.getConfig("appurl") + "post/" + mainProduct.getRefBlogPost().getSlug() + queryString;
			StringBuilder adminEmail = new StringBuilder(); 
			adminEmail.append(customer.getCustomerName() + " has made the following order<br />");
			adminEmail.append(table.toString());
			Emailer.send(Configs.getConfig("salesemail"), 
					"'" + customer.getCustomerName() + "'<" + customer.getEmail() + ">", 
					subject, adminEmail.toString());
			try {
				encodedShareUrl = URLEncoder.encode(shareUrl, "UTF-8");
			} catch (UnsupportedEncodingException e1) {}
			
			return "checkout-successful.xhtml?faces-redirect=true";
		} else {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Address", "There's No Address to deliver to! Kindly add your address");
			FacesContext.getCurrentInstance().addMessage(null, fm);
            return null;
		}
	}
	
	public String makeAddressCurrent() {
		customerAddress.setDeliveryAddress(deliveryAddress);
		CustomerAddress currentCustomerAddress = new CustomerAddress();
		currentCustomerAddress.setCustomer(customerAddress.getCustomer());
		currentCustomerAddress.setDeliveryAddress(deliveryAddress);
		CustomerAddressDAO.makeCustomerAddressCurrent(currentCustomerAddress);
		customerAddressList = CustomerAddressDAO.findAddressByCustomer(customerAddress.getCustomer());
		return null;
	}
	
	public String remove() {
		if(order.getId() > 0) {
			order.setActive(false);
	        OrderDAO.updateOrder(order);
		}
		return "home.xhtml?faces-redirect=true";
	}
	
	public void createAddress() {
		deliveryAddress = DeliveryAddressDAO.addDeliveryAddress(deliveryAddress);
		customerAddress.setDeliveryAddress(deliveryAddress);
		customerAddress.setCustomer(customerController.getCustomer());
		CustomerAddressDAO.addCustomerAddress(customerAddress);
		customerAddressList = CustomerAddressDAO.findAddressByCustomer(customerAddress.getCustomer());
		addressText = "Different Address?";
		showAddressForm = false;
	}
    
	public void updateAddress() {
		DeliveryAddressDAO.updateDeliveryAddress(deliveryAddress);
		showAddressForm = false;
	}

	public CustomerOrder getOrder() {
		return order;
	}

	public void setOrder(CustomerOrder order) {
		this.order = order;
	}
	
	public ProductController getProductController() {
		return productController;
	}

	public void setProductController(ProductController productController) {
		this.productController = productController;
	}

	public CustomerController getCustomerController() {
		return customerController;
	}

	public void setCustomerController(CustomerController customerController) {
		this.customerController = customerController;
	}

	public DeliveryAddress getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public CustomerAddress getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(CustomerAddress customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getAddressText() {
		return addressText;
	}

	public void setAddressText(String addressText) {
		this.addressText = addressText;
	}

	public List<CustomerAddress> getCustomerAddressList() {
		return customerAddressList;
	}

	public void setCustomerAddressList(List<CustomerAddress> customerAddressList) {
		this.customerAddressList = customerAddressList;
	}

	public boolean getShowAddressForm() {
		return showAddressForm;
	}

	public void setShowAddressForm(boolean showAddressForm) {
		this.showAddressForm = showAddressForm;
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	public Product getMainProduct() {
		return mainProduct;
	}

	public void setMainProduct(Product mainProduct) {
		this.mainProduct = mainProduct;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getEncodedShareUrl() {
		return encodedShareUrl;
	}

	public void setEncodedShareUrl(String encodedShareUrl) {
		this.encodedShareUrl = encodedShareUrl;
	}
}
