package com.hcare.controllers;

import javax.faces.bean.ManagedBean;

import com.hcare.models.Customer;

@ManagedBean(name = "customerController", eager = true)
public class CustomerBean {
    
	private Customer customer;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
