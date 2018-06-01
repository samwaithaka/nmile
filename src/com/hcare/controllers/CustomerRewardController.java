package com.hcare.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

//import com.hcare.dao.CustomerSegmentDAO;
import com.hcare.models.CustomerSegment;

@ManagedBean(name = "customerRewardController", eager = true)
@SessionScoped
public class CustomerRewardController {
    
	private CustomerSegment customerReward;
	
	@PostConstruct
	public void init() {
		customerReward = new CustomerSegment();
	}

	public CustomerRewardController() {
	}
	
	public String createCustomerReward() {
	    //CustomerSegmentDAO.addCustomerReward(customerReward);
		return "profile.xhtml";
	}
	
	public String updateCustomerReward() {
		//CustomerSegmentDAO.updateCustomerReward(customerReward);
		return "profile.xhtml";
	}

	public CustomerSegment getCustomerReward() {
		return customerReward;
	}

	public void setCustomerReward(CustomerSegment customerReward) {
		this.customerReward = customerReward;
	}
}
