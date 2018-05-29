package com.hcare.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import com.hcare.dao.ColorDAO;
import com.hcare.models.Color;

@ManagedBean(name = "colorController", eager = true)
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
