package com.nextramile.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextramile.dao.CustomerSegmentDAO;
import com.nextramile.models.Customer;
import com.nextramile.models.CustomerSegment;
import com.nextramile.models.Segment;

@ManagedBean(name = "customerSegmentController", eager = true)
@SessionScoped
public class CustomerSegmentController {
    
	private CustomerSegment customerSegment;
	private Customer customer;
	private Segment segment;
	
	@PostConstruct
	public void init() {
		customer = new Customer();
		segment = new Segment();
	}

	public CustomerSegmentController() {
	}
	
	public String createCustomerSegment() {
		
	    CustomerSegmentDAO.addCustomerSegment(customerSegment);
		return "profile.xhtml";
	}
	
	public String updateCustomerSegment() {
		CustomerSegmentDAO.updateCustomerSegment(customerSegment);
		return "profile.xhtml";
	}

	public CustomerSegment getCustomerSegment() {
		return customerSegment;
	}

	public void setCustomerSegment(CustomerSegment customerSegment) {
		this.customerSegment = customerSegment;
	}
}
