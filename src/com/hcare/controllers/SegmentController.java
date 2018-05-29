package com.hcare.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import com.hcare.dao.SegmentDAO;
import com.hcare.models.Segment;

@ManagedBean(name = "segmentController", eager = true)
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
