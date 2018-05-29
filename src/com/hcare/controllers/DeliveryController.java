package com.hcare.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import com.hcare.dao.DeliveryDAO;
import com.hcare.models.Delivery;

@ManagedBean(name = "deliveryController", eager = true)
public class DeliveryController {
    
	private Delivery delivery;
	
	@PostConstruct
	public void init() {
		delivery = new Delivery();
	}

	public DeliveryController() {
	}
	
	public String createDelivery() {
	    DeliveryDAO.addDelivery(delivery);
		return "delivery.xhtml";
	}
	
	public String updateDelivery() {
		DeliveryDAO.updateDelivery(delivery);
		return "delivery.xhtml";
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}
}
