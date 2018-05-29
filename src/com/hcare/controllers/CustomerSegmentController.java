package com.hcare.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import com.hcare.dao.CustomerSegmentDAO;
import com.hcare.models.Customer;
import com.hcare.models.CustomerSegment;
import com.hcare.models.Segment;

@ManagedBean(name = "customerSegmentController", eager = true)
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
