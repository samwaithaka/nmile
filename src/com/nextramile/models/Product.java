package com.nextramile.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Samuel
 *
 */
@Entity
@Table(name = "product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "product_name")
	private String productName;
	@Column(name = "slug")
	private String slug;
	@Column(name = "short_description")
	private String shortDescription;
	@Column(name = "description", length=24000000)
	private String description;
	@Column(name = "file_name")
	private String fileName;
	@Column(name = "share_message")
	private String shareMessage;
	@Column(name = "price")
	private int price;
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
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="product_category_id")
    private ProductCategory productCategory = new ProductCategory();
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="blog_id")
    private Blog refBlogPost = new Blog();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ProductCategory getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
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
	public Blog getRefBlogPost() {
		return refBlogPost;
	}
	public void setRefBlogPost(Blog refBlogPost) {
		this.refBlogPost = refBlogPost;
	}
	
	public String getShareMessage() {
		return shareMessage;
	}
	public void setShareMessage(String shareMessage) {
		this.shareMessage = shareMessage;
	}
	@Override
	public String toString() {
		return "Product [id=" + id + ", productName=" + productName + ", slug=" + slug + ", shortDescription="
				+ shortDescription + ", description=" + description + ", fileName=" + fileName + ", shareMessage="
				+ shareMessage + ", price=" + price + ", createdOn=" + createdOn + ", createdBy=" + createdBy
				+ ", editedOn=" + editedOn + ", editedBy=" + editedBy + ", active=" + active + ", refBlogPost="
				+ refBlogPost + "]";
	}
}