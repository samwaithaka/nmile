package com.nextramile.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextramile.dao.DeliveryAddressDAO;
import com.nextramile.models.DeliveryAddress;

@ManagedBean(name = "deliveryAddressController", eager = true)
@SessionScoped
public class DeliveryAddressController {
    
	private DeliveryAddress deliveryAddress;
	private List<DeliveryAddress> deliveryAddressList;
	
	@PostConstruct
	public void init() {
		deliveryAddress = new DeliveryAddress();
	}

	public DeliveryAddressController() {
	}
	
	public String createDeliveryAddress() {
	    DeliveryAddressDAO.addDeliveryAddress(deliveryAddress);
		return "delivery-address-list.xhtml";
	}
	
	public String updateDeliveryAddress() {
		DeliveryAddressDAO.updateDeliveryAddress(deliveryAddress);
		return "delivery-address-list.xhtml";
	}
	
	public String deleteDeliveryAddress() {
		deliveryAddress.setActive(false);
		DeliveryAddressDAO.updateDeliveryAddress(deliveryAddress);
		return "delivery-address-list.xhtml";
	}

	public DeliveryAddress getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public List<DeliveryAddress> getDeliveryAddressList() {
		return deliveryAddressList;
	}

	public void setDeliveryAddressList(List<DeliveryAddress> deliveryAddressList) {
		this.deliveryAddressList = deliveryAddressList;
	}
}
