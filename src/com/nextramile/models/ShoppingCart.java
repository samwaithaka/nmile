package com.nextramile.models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 * @author Samuel
 *
 */
@Entity
public class ShoppingCart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private Timestamp createdOn;
	private String createdBy;
	private Timestamp editedOn;
	private String editedBy;
	@Transient
	private int total;
	private boolean active = true;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="customerid")
    private Customer customer;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy="shoppingCart")
    private List<ShoppingCartItem> shoppingCartItems;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getEditedOn() {
		return editedOn;
	}

	public void setEditedOn(Timestamp editedOn) {
		this.editedOn = editedOn;
	}

	public String getEditedBy() {
		return editedBy;
	}

	public void setEditedBy(String editedBy) {
		this.editedBy = editedBy;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<ShoppingCartItem> getShoppingCartItems() {
		return shoppingCartItems;
	}

	public void setShoppingCartItems(List<ShoppingCartItem> shoppingCartItems) {
		this.shoppingCartItems = shoppingCartItems;
	}

	@Override
	public String toString() {
		return "ShoppingCart [id=" + id + ", createdOn=" + createdOn + ", createdBy=" + createdBy + ", editedOn="
				+ editedOn + ", editedBy=" + editedBy + ", active=" + active + ", customer=" + customer
				+ ", shoppingCartItems=" + shoppingCartItems + "]";
	}
}