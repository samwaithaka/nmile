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
@Table(name = "blog")
public class Blog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "title")
	private String title;
	@Column(name = "slug")
	private String slug;
	@Column(name = "description_meta")
	private String descriptionMeta;
	@Column(name = "keywords_meta")
	private String keywordsMeta;
	@Column(name = "short_text")
	private String shortText;
	@Column(name = "long_text",length=2097152)
	private String longText;
	@Column(name = "imageFile_name")
	private String imageFileName;
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
	@JoinColumn(name="blog_category_id")
    private BlogCategory blogCategory = new BlogCategory();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public BlogCategory getBlogCategory() {
		return blogCategory;
	}

	public void setBlogCategory(BlogCategory blogCategory) {
		this.blogCategory = blogCategory;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescriptionMeta() {
		return descriptionMeta;
	}
	
	public void setDescriptionMeta(String descriptionMeta) {
		this.descriptionMeta = descriptionMeta;
	}
	
	public String getKeywordsMeta() {
		return keywordsMeta;
	}
	
	public void setKeywordsMeta(String keywordsMeta) {
		this.keywordsMeta = keywordsMeta;
	}
	
	public String getShortText() {
		return shortText;
	}
	
	public void setShortText(String shortText) {
		this.shortText = shortText;
	}
	
	public String getLongText() {
		return longText;
	}
	
	public void setLongText(String longText) {
		this.longText = longText;
	}
	
	public String getImageFileName() {
		return imageFileName;
	}
	
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
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
	
	@Override
	public String toString() {
		return "Blog [id=" + id + ", title=" + title + ", slug=" + slug + ", descriptionMeta=" + descriptionMeta
				+ ", keywordsMeta=" + keywordsMeta + ", shortText=" + shortText + ", longText=" + longText
				+ ", imageFileName=" + imageFileName + ", createdOn=" + createdOn + ", createdBy=" + createdBy
				+ ", editedOn=" + editedOn + ", editedBy=" + editedBy + ", active=" + active + ", blogCategory="
				+ blogCategory + "]";
	}
}