package com.hcare.controllers;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.hcare.dao.CustomerDAO;
import com.hcare.dao.OrderDAO;
import com.hcare.models.Customer;
import com.hcare.models.CustomerOrder;
import com.hcare.models.Product;

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
		String view = null;
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext()
				.getRequestParameterMap();
		String ref = params.get("ref");
		String email = params.get("e");
		String token = params.get("t");
		if(email != null) customer.setEmail(email);
		if(token != null) {
			customer.setEmail(token);
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
		String token = "fdfajfwejiro445348954895435345";
		String url = "reset.xhtml?t=" + token + "&e=" + customer.getEmail();
		String subject = "Password Reset";
		String message = "Hello " + customer.getCustomerName() + ",\n\n" +
				"You have requested to reset your password. Click on the link below "
				+ "to proceed with reset. Incase you didn't initiate reset, just ignore the email.\n\n"
				+ "Reset link: " + url;
		customer.setPasswordResetToken(token);
		CustomerDAO.updateCustomer(customer);
		//Emailer.send(customer.getEmail(), subject, message);
	}
    
	public void reset() {
		customer = CustomerDAO.getCustomerByToken(customer);
		String view = null;
	    if(customer.getResetStatusMessage().equalsIgnoreCase("expired")) {
	    	// Token expired message
	    } else if(customer.getId() > 0) {
	    	// Enter password and confirm message
	    }
	}
	
	public String changePassword() {
		String view = null;
		if(customer.getPassword().equals(customer.getPasswordConfirm())) {
			CustomerDAO.updateCustomer(customer);
			view = "home.xhtml?faces-redirect=true";
			// Success message
		} else {
			// Password mismatch message
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
