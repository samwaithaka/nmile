package com.hcare.controllers;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.model.UploadedFile;

import com.hcare.dao.ProductDAO;
import com.hcare.models.Product;

@ManagedBean(name = "productController", eager = true)
public class ProductController {
    
	private Product product;
	private UploadedFile file;
	
	@PostConstruct
	public void init() {
		product = new Product();
	}

	public ProductController() {
	}
	
	public String createProduct() {
		upload();
	    ProductDAO.addProduct(product);
		return "product.xhtml";
	}
	
	public String updateProduct() {
		upload();
		ProductDAO.updateProduct(product);
		return "product.xhtml";
	}
	
	public void upload() {
		FacesMessage message = null;
        if(file != null) {
            message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
        } else {
        	message = new FacesMessage("Error", "Error occured!.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
}
