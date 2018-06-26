package com.nextramile.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextramile.dao.SegmentDAO;
import com.nextramile.models.Segment;

@ManagedBean(name = "segmentController", eager = true)
@SessionScoped
public class SegmentController {
    
	private Segment segment;
	
	@PostConstruct
	public void init() {
		segment = new Segment();
	}

	public SegmentController() {
	}
	
	public String createSegment() {
	    SegmentDAO.addSegment(segment);
		return "segment.xhtml";
	}
	
	public String updateSegment() {
		SegmentDAO.updateSegment(segment);
		return "segment.xhtml";
	}

	public Segment getSegment() {
		return segment;
	}

	public void setSegment(Segment segment) {
		this.segment = segment;
	}
}
