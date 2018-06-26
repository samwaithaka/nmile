package com.nextramile.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextramile.dao.ColorDAO;
import com.nextramile.models.Color;

@ManagedBean(name = "colorController", eager = true)
@SessionScoped
public class ColorController {
    
	private Color color;
	
	@PostConstruct
	public void init() {
		color = new Color();
	}

	public ColorController() {
	}
	
	public String createColor() {
	    ColorDAO.addColor(color);
	    color = new Color();
		return "color.xhtml";
	}
	
	public String updateColor() {
		ColorDAO.updateColor(color);
		return "color.xhtml";
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}