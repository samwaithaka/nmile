package com.nextramile.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.nextramile.dao.WishListDAO;
import com.nextramile.dao.WishListItemDAO;
import com.nextramile.models.Customer;
import com.nextramile.models.WishList;
import com.nextramile.models.WishListItem;

@ManagedBean(name = "wishListController", eager = true)
@SessionScoped
public class WishListController {
    
	private WishList wishList;
	private WishListItem wishListItem;
	private boolean itemAdded = false;
	
	@ManagedProperty(value = "#{productController}")
	private ProductController productController;

	@ManagedProperty(value = "#{customerController}")
	private CustomerController customerController;

	
	@PostConstruct
	public void init() {
	}
	
	public WishListController() {
		wishListItem = new WishListItem();
	}
	
	public void refresh() {
		wishList = WishListDAO.findPendingWishList(customerController.getCustomer());
		wishList.setWishListItems(WishListItemDAO.getWishListItems(wishList));
	}
	
	public String addToWishList() {
		System.out.println("Adding to wish list");
		Customer customer = customerController.getCustomer();
		customerController.setCustomerAction("wishList");
		if(customer.getId() == 0) {
			//FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong Password", "Username/Password failed. Please try again");
			//FacesContext.getCurrentInstance().addMessage(null, fm);
			customerController.setForm("check");
			return "user.xhtml?faces-redirect=true";
		}
		
		if(wishList.getId() == 0) {
			wishList.setCustomer(customer);
			wishList = WishListDAO.addWishList(wishList);
			wishList.getWishListItems();
		}
		wishListItem.setWishList(wishList);
		wishListItem.setQuantity(1);
		wishListItem.setProduct(productController.getProduct());
		WishListItemDAO.addWishListItem(wishListItem);
		wishList.setWishListItems(WishListItemDAO.getWishListItems(wishList));
		itemAdded = true;
		return null;
	}
	
	public String removeItem() {
		if(wishListItem.getId() > 0) {
			wishListItem.setActive(false);
	        WishListItemDAO.updateWishListItem(wishListItem);
		}
		wishList.setWishListItems(WishListItemDAO.getWishListItems(wishList));
		return "checkout.xhtml?faces-redirect=true";
	}
	
	public String remove() {
		if(wishList.getId() > 0) {
			wishList.setActive(false);
	        WishListDAO.updateWishList(wishList);
		}
		return "home.xhtml?faces-redirect=true";
	}

	public String proceed() {
		itemAdded = false;
		return "checkout.xhtml?faces-redirect=true";
	}

	public WishList getWishList() {
		return wishList;
	}

	public void setWishList(WishList wishList) {
		this.wishList = wishList;
	}

	public WishListItem getWishListItem() {
		return wishListItem;
	}

	public void setWishListItem(WishListItem wishListItem) {
		this.wishListItem = wishListItem;
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
