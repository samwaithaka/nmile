package com.hcare.models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Samuel
 *
 */
@Entity
public class Inventory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int quantity;
	private Timestamp createdOn;
	private String createdBy;
	private Timestamp editedOn;
	private String editedBy;
	private boolean active = true;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="productid")
    private Product product;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="colorid")
    private Color color;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sizeid")
    private Size size;

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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Inventory [id=" + id + ", quantity=" + quantity + ", createdOn=" + createdOn + ", createdBy="
				+ createdBy + ", editedOn=" + editedOn + ", editedBy=" + editedBy + ", active=" + active + ", product="
				+ product + ", color=" + color + ", size=" + size + "]";
	}
}