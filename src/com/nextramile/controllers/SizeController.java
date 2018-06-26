package com.nextramile.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextramile.dao.SizeDAO;
import com.nextramile.models.Size;

@ManagedBean(name = "sizeController", eager = true)
@SessionScoped
public class SizeController {
    
	private Size size;
	
	@PostConstruct
	public void init() {
		size = new Size();
	}

	public SizeController() {
	}
	
	public String createSize() {
	    SizeDAO.addSize(size);
	    size = new Size();
		return "size.xhtml";
	}
	
	public String updateSize() {
		SizeDAO.updateSize(size);
		return "size.xhtml";
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}
}
