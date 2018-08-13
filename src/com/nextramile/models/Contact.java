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
@Table(name = "contact")
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "email", length=32)
    private String email;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
		return "Contact [id=" + id + ", email=" + email + ", createdOn=" + createdOn + ", createdBy=" + createdBy
				+ ", editedOn=" + editedOn + ", editedBy=" + editedBy + ", active=" + active + "]";
	}
}