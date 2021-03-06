package com.nextramile.controllers;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.nextramile.dao.*;
import com.nextramile.dao.CartItemDAO;
import com.nextramile.models.Customer;
import com.nextramile.models.ShoppingCart;
import com.nextramile.models.ShoppingCartItem;

@ManagedBean(name = "shoppingCartController", eager = true)
@SessionScoped
public class ShoppingCartController {
    
	private ShoppingCart shoppingCart;
	private ShoppingCartItem shoppingCartItem;
	private boolean itemAdded = false;
	
	@ManagedProperty(value = "#{productController}")
	private ProductController productController;

	@ManagedProperty(value = "#{customerController}")
	private CustomerController customerController;

	
	@PostConstruct
	public void init() {
		shoppingCartItem = new ShoppingCartItem();
	}
	
	public ShoppingCartController() {
	}
	
	public void refresh() {
		shoppingCart = CartDAO.findPendingShoppingCart(customerController.getCustomer());
		shoppingCart.setShoppingCartItems(CartItemDAO.getCartItems(shoppingCart));
	}
	
	public String addToShoppingCart() {
		System.out.println("Adding to cart...");
		Customer customer = customerController.getCustomer();
		customerController.setCustomerAction("shoppingCart");
		if(customer.getId() == 0) {
			customerController.setForm("check");
			return "user.xhtml?faces-redirect=true";
		}
		
		if(customer.getId() == 0 && customer.getEmail() != null) {
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong Password", "Username/Password failed. Please try again");
			FacesContext.getCurrentInstance().addMessage(null, fm);
			return "user.xhtml?faces-redirect=true";
		}

		if(shoppingCart.getId() == 0) {
			shoppingCart.setCustomer(customer);
			shoppingCart = CartDAO.addShoppingCart(shoppingCart);
			shoppingCart.getShoppingCartItems();
		}

		shoppingCartItem.setShoppingCart(shoppingCart);
		shoppingCartItem.setProduct(customerController.getProduct());
		CartItemDAO.addShoppingCartItem(shoppingCartItem);
		FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Added to Cart", "You have successfully added " + shoppingCartItem.getProduct().getProductName() + " to cart. "
				+ "You can click on the cart to checkout, or you can continue shopping");
		FacesContext.getCurrentInstance().addMessage(null, fm);
		shoppingCartItem.setQuantity(1);
		shoppingCart.setShoppingCartItems(CartItemDAO.getCartItems(shoppingCart));
		itemAdded = true;
		return null;
	}
	
	public String removeItem() {
		if(shoppingCartItem.getId() > 0) {
			shoppingCartItem.setActive(false);
	        CartItemDAO.updateShoppingCartItem(shoppingCartItem);
		}
		shoppingCart.setShoppingCartItems(CartItemDAO.getCartItems(shoppingCart));
		return "checkout.xhtml?faces-redirect=true";
	}
	
	public String remove() {
		if(shoppingCart.getId() > 0) {
			shoppingCart.setActive(false);
	        CartDAO.updateShoppingCart(shoppingCart);
		}
		return "checkout.xhtml?faces-redirect=true";
	}

	public String proceed() {
		itemAdded = false;
		return "checkout.xhtml?faces-redirect=true";
	}

	public String continueShopping() {
		itemAdded = false;
		return "shop.xhtml?faces-redirect=true";
	}
	
	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	public ShoppingCartItem getShoppingCartItem() {
		return shoppingCartItem;
	}

	public void setShoppingCartItem(ShoppingCartItem shoppingCartItem) {
		this.shoppingCartItem = shoppingCartItem;
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

	public boolean getItemAdded() {
		return itemAdded;
	}

	public void setItemAdded(boolean itemAdded) {
		this.itemAdded = itemAdded;
	}
}
