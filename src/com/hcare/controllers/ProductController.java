package com.hcare.controllers;

import java.util.Map;

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
	
	public void refresh() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idValue = params.get("id");
		if(idValue != null) {
		int productId = Integer.parseInt(idValue);
		    System.out.println(productId);
			product = ProductDAO.find(productId);
		}
	}
	
	public String createProduct() {
		upload();
	    ProductDAO.addProduct(product);
		return "admin-product.xhtml";
	}
	
	public String updateProduct() {
		upload();
		ProductDAO.updateProduct(product);
		return "admin-product.xhtml";
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
