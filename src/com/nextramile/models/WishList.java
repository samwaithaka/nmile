package com.nextramile.models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Samuel
 *
 */
@Entity
@Table(name = "wish_list")
public class WishList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "created_on")
	private Timestamp createdOn;
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "edited_on")
	private Timestamp editedOn;
	@Column(name = "edited_by")
	private String editedBy;
	@Column(name = "active")
	private boolean active = true;
	@Transient
	private int total;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="customer_id")
    private Customer customer;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy="wishList")
    private List<WishListItem> wishListItems;

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

	public List<WishListItem> getWishListItems() {
		return wishListItems;
	}

	public void setWishListItems(List<WishListItem> wishListItems) {
		this.wishListItems = wishListItems;
	}

	@Override
	public String toString() {
		return "WishList [id=" + id + ", createdOn=" + createdOn + ", createdBy=" + createdBy + ", editedOn="
				+ editedOn + ", editedBy=" + editedBy + ", active=" + active + ", customer=" + customer
				+ ", wishListItems=" + wishListItems + "]";
	}
}