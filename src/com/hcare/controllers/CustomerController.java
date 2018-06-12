package com.hcare.controllers;

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

import com.hcare.dao.CustomerDAO;
import com.hcare.dao.OrderDAO;
import com.hcare.models.Customer;
import com.hcare.models.CustomerOrder;
import com.hcare.models.Product;
import com.hcare.util.Emailer;

@ManagedBean(name = "customerController", eager = true)
@SessionScoped
public class CustomerController {

	private Customer customer;
	private List<CustomerOrder> customerOrderList;
	private Product product;
	private int refId = 0;

	@PostConstruct
	public void init() {
		customer = new Customer();
		product = new Product();
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
		if(customer.getPasswordResetToken() == null) {
			try {
				email = URLDecoder.decode(params.get("e"), "UTF-8");
				token = URLDecoder.decode(params.get("t"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if(email != null) customer.setEmail(email);
		if(token != null) {
			customer.setPasswordResetToken(token);
			reset();
		}
		if(ref != null) {
			refId = Integer.parseInt(ref);
		}
		if(customer.getId() > 0) {
			customerOrderList = OrderDAO.getCustomerOrderList(customer);
		}
	}

	public String createCustomer() {
		System.out.println(customer);
		customer.setReferer(CustomerDAO.find(refId));
		CustomerDAO.addCustomer(customer);
		return "home.xhtml?faces-redirect=true";
	}

	public String queryCustomerEmail() {
		customer = CustomerDAO.findByEmail(customer.getEmail());
		String page = "";
		if(customer.getId() > 0) {	
			page = "login.xhtml?faces-redirect=true";
		} else {
			customer.setPassword("password");
			customer = CustomerDAO.addCustomer(customer);
			page = "profile.xhtml?faces-redirect=true";
		}
		if(product.getId() > 0) {
			CustomerOrder order = new CustomerOrder();
			order.setCustomer(customer);
			order.setProduct(product);
			OrderDAO.addOrder(order);
			//page = "profile.xhtml?faces-redirect=true";
		}
		return page;
	}

	public String updateCustomer() {
		CustomerDAO.updateCustomer(customer);
		return "home.xhtml?faces-redirect=true";
	}

	public String login() {
		customer = CustomerDAO.login(customer);
		if(customer.getId() > 0) {
			return "home.xhtml?faces-redirect=true";
		} else {
			return null;
		}
	}

	public void generatePasswordResetToken() {
		System.out.println(customer);
		FacesContext context = FacesContext.getCurrentInstance();
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String url;
		try {
			url = "http://localhost:8080/hcare/reset.xhtml?t=" + URLEncoder.encode(ts.toString(), "UTF-8") + "&e=" + URLEncoder.encode(customer.getEmail(),"UTF-8");
			String subject = "Password Reset";
			String message = "Hello " + customer.getCustomerName() + ",\n\n" +
					"You have requested to reset your password. Click on the link below "
					+ "to proceed with reset. Incase you didn't initiate reset, just ignore the email.\n\n"
					+ "Reset link: " + url;
			customer.setPasswordResetToken(ts.toString());
			CustomerDAO.updateCustomer(customer);
			customer.setPasswordResetToken(null);
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Check Your Mail","A password reset link has been sent to " + customer.getEmail());
			context.getCurrentInstance().addMessage(null, fm);
			System.out.println(url);
			//Emailer.send("samwaithaka@gmail.com", customer.getEmail(), subject, message);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void reset() {
		System.out.println(customer);
		FacesContext context = FacesContext.getCurrentInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		try {
			Date parsedDate = dateFormat.parse(customer.getPasswordResetToken());
			Timestamp resetTime = new java.sql.Timestamp(parsedDate.getTime());

			Timestamp now = new Timestamp(System.currentTimeMillis());
			Long diff = now.getTime() - resetTime.getTime();
			Long diffHours = diff / (60 * 60 * 1000);
			if(diffHours > 1) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Expired","Token is expired. Request reset again");
				context.getCurrentInstance().addMessage(null, message);
			} else {
				customer = CustomerDAO.getCustomerByToken(customer);
				if(customer.getId() > 0) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Change","Go ahead and change password");
					context.getCurrentInstance().addMessage(null, message);
				} else {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error","Error Occured. Make sure you copied the right link from your email");
					context.getCurrentInstance().addMessage(null, message);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String changePassword() {
		FacesContext context = FacesContext.getCurrentInstance();
		String view = null;
		System.out.println(customer);
		if(customer.getPassword().equals(customer.getPasswordConfirm())) {
			CustomerDAO.changePassword(customer);
			view = "home.xhtml?faces-redirect=true";
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful","Password Successully changed");
			context.getCurrentInstance().addMessage(null, message);
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mismatch","The passwords did not match, please try again");
			context.getCurrentInstance().addMessage(null, message);
		}
		return view;
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

	public List<CustomerOrder> getCustomerOrderList() {
		return customerOrderList;
	}

	public void setCustomerOrderList(List<CustomerOrder> customerOrderList) {
		this.customerOrderList = customerOrderList;
	}
}
