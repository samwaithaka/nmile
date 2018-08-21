package com.nextramile.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.shiro.session.Session;

import com.nextramile.dao.LocationDAO;
import com.nextramile.dao.UserDAO;
import com.nextramile.models.Location;
import com.nextramile.models.UserAccount;
import com.nextramile.util.AuthManager;

@ManagedBean(name="locationController", eager=true)
@SessionScoped
public class LocationController {
	
	private Location location;
	private List<Location> locationList;
	private UserAccount user;
	
	@PostConstruct
	public void init() {
		location = new Location();
	}
	
	public LocationController() {
		locationList = LocationDAO.getLocationList();
		Session session = AuthManager.getSession();
		if(session != null) {
		    user = (UserAccount)session.getAttribute("user");
		}
	}
	
	public String addLocation() {
		location.setCreatedBy(user.getUsername());
	    LocationDAO.addLocation(location);
	    locationList = LocationDAO.getLocationList();
	    location = new Location();
	    return "admin-list-locations.xhtml?faces-redirect=true";
	}
	
	public String updateLocation() {
		location.setEditedBy(user.getUsername());
	    LocationDAO.updateLocation(location);
	    locationList = LocationDAO.getLocationList();
	    location = new Location();
	    return "admin-list-locations.xhtml?faces-redirect=true";
	}
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}
}