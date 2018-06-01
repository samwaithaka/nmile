package com.hcare.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.hcare.dao.ColorDAO;
import com.hcare.dao.OrderDAO;
import com.hcare.dao.SizeDAO;
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
	private List<Color> colorList;
	private List<Size> sizeList;
	
	@ManagedProperty(value = "#{productController}")
	private ProductController productController;

	@ManagedProperty(value = "#{customerController}")
	private CustomerController customerController;

	
	@PostConstruct
	public void init() {
		order = new CustomerOrder();
		color = new Color();
		size = new Size();
	}

	public OrderController() {
		colorList = ColorDAO.getColorList();
		sizeList = SizeDAO.getSizeList();
	}
	
	public String makeOrder() {
		if(customerController.getCustomer().getId() == 0) {
			return "user.xhtml";
		}
		System.out.println("order here: " + order);
		if(order.getId() == 0) {
			order.setCustomer(customerController.getCustomer());
			order.setProduct(productController.getProduct());
	        OrderDAO.addOrder(order);
		}
		return "order.xhtml";
	}
	
	public String remove() {
		if(order.getId() > 0) {
			order.setActive(false);
	        OrderDAO.updateOrder(order);
		}
		return "home.xhtml?faces-redirect=true";
	}
	
	public String updateOrder() {
		size = SizeDAO.find(size.getId());
		color = ColorDAO.find(color.getId());
		System.out.println("size: " + size);
		order.setSize(size);
		order.setColor(color);
		OrderDAO.updateOrder(order);
		return "checkout.xhtml";
	}
	
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

	public List<Color> getColorList() {
		return colorList;
	}

	public void setColorList(List<Color> colorList) {
		this.colorList = colorList;
	}

	public List<Size> getSizeList() {
		return sizeList;
	}

	public void setSizeList(List<Size> sizeList) {
		this.sizeList = sizeList;
	}
}
