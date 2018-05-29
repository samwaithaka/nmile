package com.hcare.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import com.hcare.dao.CustomerDAO;
import com.hcare.models.Customer;

@ManagedBean(name = "customerController", eager = true)
public class CustomerController {
    
	private Customer customer;
	private int refId = 0;
	
	@PostConstruct
	public void init() {
		customer = new Customer();
	}

	public CustomerController() {
		String ref = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("ref");
		System.out.println("Ref: " + ref);
		if(ref != null) {
		    refId = Integer.parseInt(ref);
		}
	}
	
	public String createCustomer() {
		refId = 2;
		customer.setReferer(CustomerDAO.find(refId));
	    CustomerDAO.addCustomer(customer);
		return "profile.xhtml";
	}
	
	public String queryCustomerEmail() {
		customer = CustomerDAO.findByEmail(customer.getEmail());
		if(customer.getId() > 0) {
			return "login.xhtml";
		} else {
			return "profile.xhtml";
		}
	}
	
	public String updateCustomer() {
		CustomerDAO.updateCustomer(customer);
		return "profile.xhtml";
	}
	
	public String login() {
		customer = CustomerDAO.login(customer);
		if(customer.getId() > 0) {
		    return "profile.xhtml";
		} else {
			return null;
		}
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
