package com.nextramile.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.nextramile.dao.CartDAO;
import com.nextramile.dao.CartItemDAO;
import com.nextramile.models.Customer;
import com.nextramile.models.ShoppingCart;
import com.nextramile.models.ShoppingCartItem;

@ManagedBean(name = "shoppingCartController", eager = true)
@SessionScoped
public class ShoppingCartController {
    
	private ShoppingCart shoppingCart;
	private int quantity;
	
	@ManagedProperty(value = "#{productController}")
	private ProductController productController;

	@ManagedProperty(value = "#{customerController}")
	private CustomerController customerController;

	
	@PostConstruct
	public void init() {
		shoppingCart = new ShoppingCart();
	}
	
	public ShoppingCartController() {
	}
	
	public String addToShoppingCart() {
		System.out.println("S.C. Quantity: " + quantity);
		Customer customer = customerController.getCustomer();
		if(customer.getId() == 0) {
			return "user.xhtml?faces-redirect=true";
		}
		
		if(shoppingCart.getId() == 0) {
			shoppingCart.setCustomer(customer);
			shoppingCart = CartDAO.addShoppingCart(shoppingCart);
		}
		System.out.println(shoppingCart);
		ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
		shoppingCartItem.setShoppingCart(shoppingCart);
		shoppingCartItem.setProduct(productController.getProduct());
		shoppingCartItem.setQuantity(quantity);
		CartItemDAO.addShoppingCartItem(shoppingCartItem);
		return null;
	}
	
	public String remove() {
		if(shoppingCart.getId() > 0) {
			shoppingCart.setActive(false);
	        CartDAO.updateShoppingCart(shoppingCart);
		}
		return "home.xhtml?faces-redirect=true";
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
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
