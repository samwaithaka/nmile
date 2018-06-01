package com.hcare.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.hcare.dao.OrderDAO;
import com.hcare.models.Color;
import com.hcare.models.CustomerOrder;
import com.hcare.models.Product;
import com.hcare.models.Size;

@ManagedBean(name = "orderController", eager = true)
public class OrderController {
    
	private CustomerOrder order;
	private Color color;
	private Size size;
	private Product product;
	
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
		if(customerController.getCustomer() != null) {
			return "login.xhtml";
		}
	    OrderDAO.addOrder(order);
		return "order.xhtml";
	}
	
	public String updateOrder() {
		OrderDAO.updateOrder(order);
		return "order.xhtml";
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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
