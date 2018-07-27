package com.nextramile.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.nextramile.dao.ProductDAO;
import com.nextramile.models.Product;

@ManagedBean(name = "productController", eager = true)
@SessionScoped
public class ProductController {
    
	private Product product;
	private List<Product> productList;
	private UploadedFile file;
	private String resourcePath;
	private String webResourcePath;
	private StreamedContent image;
	
	@PostConstruct
	public void init() {
		product = new Product();
		productList = ProductDAO.getProductList();
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
	    productList = ProductDAO.getProductList();
	    product = new Product();
		return "admin-product-list.xhtml?faces-redirect=true";
	}
	
	public String updateProduct() {
		upload();
		ProductDAO.updateProduct(product);
		productList = ProductDAO.getProductList();
		product = new Product();
		return "admin-product-list.xhtml?faces-redirect=true";
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
        		byte[] buffer = new byte[file.getInputstream().available()];
        		file.getInputstream().read(buffer);
        		fileName = fileName.replace(fileExtension, "");
        		File fileUpload = new File(resourcePath + "/images/" + fileName + product.getId() + fileExtension);
        		File webFileUpload = new File(webResourcePath + "/images/" + fileName + product.getId() + fileExtension);
        		FileOutputStream out = new FileOutputStream(fileUpload);
        		FileOutputStream out2 = new FileOutputStream(webFileUpload);
        		out.write(buffer);
        		out2.write(buffer);
        		out.close();
        		out2.close();
				product.setFileName(fileName + product.getId() + fileExtension);
				ProductDAO.updateProduct(product);
				System.out.println(file.getFileName() + " Photo uploaded");
			} catch (IOException e) {
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

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
}
