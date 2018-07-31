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
import javax.persistence.Transient;

/**
 * @author Samuel
 *
 */
@Entity
@Table(name = "user_account")
public class UserAccount {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "username", length=32)
    private String username;
    @Column(name = "first_name", length=32)
    private String firstName;
    @Column(name = "last_name", length=32)
    private String lastName;
    @Column(name = "email_address", length=64)
    private String emailAddress;
    @Column(name = "password", length=100)
    private String password;
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
	@Column(name = "reset_flag")
	private boolean resetFlag = false;
	@Transient
	private int passwordAge;
	
	@OneToMany(mappedBy = "user", fetch=FetchType.LAZY)
    @JoinColumn(name="user_account_id", referencedColumnName="id")
	private List<UserRole> userRoles;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
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
	
	public List<UserRole> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	public int getPasswordAge() {
		return passwordAge;
	}

	public void setPasswordAge(int passwordAge) {
		this.passwordAge = passwordAge;
	}

	public boolean getResetFlag() {
		return resetFlag;
	}

	public void setResetFlag(boolean resetFlag) {
		this.resetFlag = resetFlag;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", firstName=" + firstName + ", lastName=" + lastName + ", emailAddress="
				+ emailAddress + ", createdOn=" + createdOn + ", createdBy=" + createdBy + ", passwordAge=" + passwordAge
				+ ", editedOn=" + editedOn + ", editedBy=" + editedBy + ", active=" + active + "]";
	}
}
