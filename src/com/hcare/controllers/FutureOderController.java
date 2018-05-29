package com.hcare.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import com.hcare.dao.FutureOrderDAO;
import com.hcare.models.FutureOrder;

@ManagedBean(name = "futureOrderController", eager = true)
public class FutureOderController {
    
	private FutureOrder futureOrder;
	
	@PostConstruct
	public void init() {
		futureOrder = new FutureOrder();
	}

	public FutureOderController() {
	}
	
	public String createFutureOrder() {
	    FutureOrderDAO.addFutureOrder(futureOrder);
		return "future-order.xhtml";
	}
	
	public String updateFutureOrder() {
		FutureOrderDAO.updateFutureOrder(futureOrder);
		return "future-order.xhtml";
	}

	public FutureOrder getFutureOrder() {
		return futureOrder;
	}

	public void setFutureOrder(FutureOrder futureOrder) {
		this.futureOrder = futureOrder;
	}
}
