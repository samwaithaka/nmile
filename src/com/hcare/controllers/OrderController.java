package com.hcare.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.hcare.dao.OrderDAO;
import com.hcare.models.Color;
import com.hcare.models.CustomerOrder;
//import com.hcare.models.Product;
import com.hcare.models.Size;

@ManagedBean(name = "orderController", eager = true)
@SessionScoped
public class OrderController {
    
	private CustomerOrder order;
	private Color color;
	private Size size;
	//private Product product;
	
	@ManagedProperty(value = "#{productController}")
	private ProductController productController;

	@ManagedProperty(value = "#{customerController}")
	private CustomerController customerController;

	
	@PostConstruct
	public void init() {
		order = new CustomerOrder();
	}

	public OrderController() {
	}
	
	public String makeOrder() {
		if(customerController.getCustomer().getId() == 0) {
			return "user.xhtml";
		}
		if(order.getId() == 0) {
	        OrderDAO.addOrder(order);
		}
		return "order.xhtml";
	}
	
	public String remove() {
		if(order.getId() > 0) {
			order.setActive(false);
	        OrderDAO.updateOrder(order);
		}
		return "home.xhtml";
	}
	
	public String updateOrder() {
		OrderDAO.updateOrder(order);
		return "profile.xhtml";
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

/*	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}*/
	
	public CustomerOrder getOrder() {
		return order;
	}

	public void setOrder(CustomerOrder order) {
		this.order = order;
	}
	
	public ProductController getProductController() {
		return productController;
	}

	public void setProductController(ProductController productController) {
		this.productController = productController;
	}

	public CustomerController getCustomerController() {
		return customerController;
	}

	public void setCustomerController(CustomerController customerController) {
		this.customerController = customerController;
	}
}
