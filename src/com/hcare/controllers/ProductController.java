package com.hcare.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.hcare.dao.ProductDAO;
import com.hcare.models.Product;

@ManagedBean(name = "productController", eager = true)
@SessionScoped
public class ProductController {
    
	private Product product;
	private UploadedFile file;
	private String resourcePath;
	private String webResourcePath;
	private StreamedContent image;
	
	@PostConstruct
	public void init() {
		product = new Product();
	}

	public ProductController() {
		resourcePath = new File(ProductController.class.getClassLoader()
				.getResource(".").getFile()).getAbsolutePath();
		webResourcePath = FacesContext.getCurrentInstance()
				.getExternalContext().getRealPath("/");
	}
	
	public void refresh() {
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext()
				.getRequestParameterMap();
		String idValue = params.get("id");
		if(idValue != null) {
			int productId = Integer.parseInt(idValue);
			product = ProductDAO.find(productId);
			if(product != null) {
				File imageFile = new File(resourcePath + "/images/" + product.getFileName());
				try {
					image = new DefaultStreamedContent(new FileInputStream(imageFile),"image/jpeg");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String createProduct() {
	    product = ProductDAO.addProduct(product);
	    upload();
		return "admin-product.xhtml";
	}
	
	public String updateProduct() {
		upload();
		ProductDAO.updateProduct(product);
		return "admin-product.xhtml";
	}
	
	public void upload() {
		FacesMessage message = null;
		//System.out.println(imagesPath);
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
        		byte[] buffer = new byte[file.getInputstream().available()];
        		file.getInputstream().read(buffer);
        		fileName = fileName.replace(fileExtension, "");
        		File fileUpload = new File(resourcePath + "/images/" + fileName + product.getId() + fileExtension);
        		File webFileUpload = new File(webResourcePath + "/images/" + fileName + product.getId() + fileExtension);
        		FileOutputStream out = new FileOutputStream(fileUpload);
        		FileOutputStream out2 = new FileOutputStream(webFileUpload);
        		out.write(buffer);
        		out2.write(buffer);
				product.setFileName(fileName + product.getId() + fileExtension);
				ProductDAO.updateProduct(product);
				System.out.println(file.getFileName() + " Photo uploaded");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
        } else {
        	System.out.println("No photo");
        	message = new FacesMessage("Error", "Error occured!.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public StreamedContent getImage() {
		return image;
	}

	public void setImage(StreamedContent image) {
		this.image = image;
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
