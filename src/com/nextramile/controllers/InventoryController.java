package com.nextramile.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextramile.dao.InventoryDAO;
import com.nextramile.models.Inventory;

@ManagedBean(name = "inventoryController", eager = true)
@SessionScoped
public class InventoryController {
    
	private Inventory inventory;
	
	@PostConstruct
	public void init() {
		inventory = new Inventory();
	}

	public InventoryController() {
	}
	
	public String createInventory() {
	    InventoryDAO.addInventory(inventory);
		return "inventory.xhtml";
	}
	
	public String updateInventory() {
		InventoryDAO.updateInventory(inventory);
		return "inventory.xhtml";
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
}
