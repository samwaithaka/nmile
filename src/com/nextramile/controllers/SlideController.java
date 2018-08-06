package com.nextramile.controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.primefaces.model.UploadedFile;

import com.nextramile.dao.ProductDAO;
import com.nextramile.dao.SlideDAO;
import com.nextramile.dao.UserDAO;
import com.nextramile.models.Product;
import com.nextramile.models.Slide;
import com.nextramile.models.UserAccount;
//import com.nextramile.utils.CoreClass;
//import com.nextramile.utils.SessionManager;
import com.nextramile.util.Configs;
import com.nextramile.util.FileOperations;
import com.nextramile.util.ImageResizer;

@ManagedBean(name="slideController", eager=true)
@SessionScoped
public class SlideController {
	
	private Slide slide;
	private List<Slide> slideList;
	private List<Integer> indexList;
	//private SessionManager sessionManager;
	private UserAccount user;
	private UploadedFile file;
	private String appDataDirectory;
	private String webResourcePath;
	
	@PostConstruct
	public void init() {
		slide = new Slide();
	}
	
	public SlideController() {
		slideList = SlideDAO.getSlideList();
		String userId = null;
		if(userId != null) {
			user = UserDAO.find(Integer.parseInt(userId));
		}
		
		appDataDirectory = System.getProperty("user.home") + "/." + Configs.getConfig("uri");
		webResourcePath = FacesContext.getCurrentInstance()
				.getExternalContext().getRealPath("/");
		int i = 0;
		indexList = new ArrayList<Integer>();
		for(Slide sld : slideList) {
			FileOperations.copyFile(appDataDirectory + "slides/" + sld.getSlidePhoto(), 
					webResourcePath + "/slides/" + sld.getSlidePhoto());
			indexList.add(i);
			++i;
		}
		indexList.add(i);
	}
	
	public String createSlide() {
		//slide.setCreatedBy(user.getUsername());
		slide.setCreatedBy("User");
		slide = SlideDAO.addSlide(slide);
	    if(file.getFileName() != null && file.getSize() > 0) {
		    upload();
		}
	    slide = new Slide();
	    slideList = SlideDAO.getSlideList();
	    return "admin-slide-list.xhtml?faces-redirect=true";
	}
	
	public String updateSlide() {
		if(file.getFileName() != null && file.getSize() > 0) {
		    upload();
		}
		//slide.setEditedBy(user.getUsername());
		slide.setEditedBy("User");
	    SlideDAO.updateSlide(slide);
	    slideList = SlideDAO.getSlideList();
	    slide = new Slide();
	    return "admin-slide-list.xhtml?faces-redirect=true";
	}
	
	public void upload() {
		FacesMessage message = null;
        if(file != null) {
        	try {
        		//create filename string
        		String fileName = file.getFileName()
        				.replace(" ", "-")
        				.toLowerCase() + "-";
        		String fileExtension = ".";
        		int i = file.getFileName().lastIndexOf('.');
        		if (i > 0) {
        			fileExtension += file.getFileName().substring(i+1);
        		}
        		BufferedImage im = ImageIO.read(file.getInputstream());
        		BufferedImage mainPhoto = ImageResizer.resize(im, 800, 400);
        		fileName = fileName.replace(fileExtension, "");
        		ImageIO.write(mainPhoto, "JPEG", new File(webResourcePath + "/slides/" + fileName + slide.getId() + fileExtension));
        		ImageIO.write(mainPhoto, "JPEG", new File(appDataDirectory + "/slides/" + fileName + slide.getId() + fileExtension));
        		slide.setSlidePhoto(fileName + slide.getId() + fileExtension);
				SlideDAO.updateSlide(slide);
			} catch (IOException e) {
				e.printStackTrace();
			}
            message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
        } else {
        	message = new FacesMessage("Error", "Error occured!.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

	public Slide getSlide() {
		return slide;
	}

	public void setSlide(Slide slide) {
		this.slide = slide;
	}

	public List<Slide> getSlideList() {
		return slideList;
	}

	public void setSlideList(List<Slide> slideList) {
		this.slideList = slideList;
	}
	
	public List<Integer> getIndexList() {
		return indexList;
	}

	public void setIndexList(List<Integer> indexList) {
		this.indexList = indexList;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
}