package com.nextramile.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextramile.dao.DeliveryDAO;
import com.nextramile.models.Delivery;

@ManagedBean(name = "deliveryController", eager = true)
@SessionScoped
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
