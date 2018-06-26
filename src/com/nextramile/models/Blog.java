package com.nextramile.models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Samuel
 *
 */
@Entity
public class Blog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String title;
	private String slug;
	private String descriptionMeta;
	private String keywordsMeta;
	private String shortText;
	private String longText;
	private String imageFileName;
	private Timestamp createdOn;
	private String createdBy;
	private Timestamp editedOn;
	private String editedBy;
	private boolean active = true;
	
	public int getId() {
		return id;
	}
	
	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public void setId(int id) {
		this.id = id;
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
				+ ", imageFileName=" + imageFileName + ", createdBy=" + createdBy + ", editedOn=" + editedOn
				+ ", editedBy=" + editedBy + ", active=" + active + "]";
	}
}