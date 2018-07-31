/**
 * 
 */
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Samuel
 *
 */
@Entity
@Table(name = "slide")
public class Slide {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "slide_title", length=32)
    private String slideTitle;
	@Column(name = "slide_text", length=64)
    private String slideText;
	@Column(name = "slide_photo", length=64)
    private String slidePhoto;
	@Column(name = "created_on")
	private Timestamp createdOn;
	@Column(name = "created_by", length=32)
	private String createdBy;
	@Column(name = "edited_on")
	private Timestamp editedOn;
	@Column(name = "edited_by", length=32)
	private String editedBy;
	@Column(name = "active")
	private boolean active = true;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSlideTitle() {
		return slideTitle;
	}
	public void setSlideTitle(String slideTitle) {
		this.slideTitle = slideTitle;
	}
	public String getSlideText() {
		return slideText;
	}
	public void setSlideText(String slideText) {
		this.slideText = slideText;
	}
	public String getSlidePhoto() {
		return slidePhoto;
	}
	public void setSlidePhoto(String slidePhoto) {
		this.slidePhoto = slidePhoto;
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
}