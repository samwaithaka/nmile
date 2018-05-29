package com.hcare.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import com.hcare.dao.OrderDAO;
import com.hcare.models.CustomerOrder;

@ManagedBean(name = "orderController", eager = true)
public class OrderController {
    
	private CustomerOrder order;
	
	@PostConstruct
	public void init() {
		order = new CustomerOrder();
	}

	public OrderController() {
	}
	
	public String createOrder() {
	    OrderDAO.addOrder(order);
		return "order.xhtml";
	}
	
	public String updateOrder() {
		OrderDAO.updateOrder(order);
		return "order.xhtml";
	}

	public CustomerOrder getOrder() {
		return order;
	}

	public void setOrder(CustomerOrder order) {
		this.order = order;
	}
}
