package com.nextramile.controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.nextramile.dao.BlogDAO;
import com.nextramile.dao.ProductCategoryDAO;
import com.nextramile.dao.ProductDAO;
import com.nextramile.models.Blog;
import com.nextramile.models.Product;
import com.nextramile.models.ProductCategory;
import com.nextramile.util.Configs;
import com.nextramile.util.FileOperations;
import com.nextramile.util.ImageResizer;
import com.nextramile.util.TextFileOperations;

@ManagedBean(name = "productController", eager = true)
@SessionScoped
public class ProductController {
    
	private Product product;
	private List<Product> productList;
	private UploadedFile file;
	private String appDataDirectory;
	private String webResourcePath;
	private StreamedContent image;
	private String url;
	private String encodedUrl;
	
	@PostConstruct
	public void init() {
		product = new Product();
		product.setRefBlogPost(new Blog());
		product.setProductCategory(new ProductCategory());
		System.out.println(product);
	}

	public ProductController() {
		appDataDirectory = System.getProperty("user.home") + "/." + Configs.getConfig("dir");
		webResourcePath = FacesContext.getCurrentInstance()
				.getExternalContext().getRealPath("/");
		productList = ProductDAO.getProductList();
		for(Product prod : productList) {
			FileOperations.copyFile(appDataDirectory + "images/" + prod.getFileName(), 
					webResourcePath + "/images/" + prod.getFileName());
			FileOperations.createThumbnails(appDataDirectory + "images/" + prod.getFileName(), 
					webResourcePath + "/images/thumbnails/" + prod.getFileName());
		}
	}
	
	public void refresh() {
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext()
				.getRequestParameterMap();
		String slug = params.get("id");
		if(slug != null) {
			url = Configs.getConfig("appurl") + "/product/" + slug;
			try {
				encodedUrl = URLEncoder.encode(url, "UTF-8");
			} catch (UnsupportedEncodingException e1) {}
			product = ProductDAO.findBySlug(slug);
			if(product != null) {
				File imageFile = new File(appDataDirectory + "/images/" + product.getFileName());
				/*try {
					image = new DefaultStreamedContent(new FileInputStream(imageFile),"image/jpeg");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}*/
			}
		}
	}
	
	public String createProduct() {
		String slug = product.getProductName().replace(" ", "-").toLowerCase();
	    Blog refBlogPost = BlogDAO.find(product.getRefBlogPost().getId());
	    ProductCategory productCategory = ProductCategoryDAO.find(product.getProductCategory().getId());
	    product.setSlug(slug);
	    product.setRefBlogPost(refBlogPost);
	    product.setProductCategory(productCategory);
	    product = ProductDAO.addProduct(product);
	    TextFileOperations.writePageUrl(webResourcePath + "sitemap.txt", "product/" + product.getSlug());
	    if(file.getFileName() != null && file.getSize() > 0) {
		    upload();
		}
	    productList = ProductDAO.getProductList();
	    product = new Product();
		return "admin-product-list.xhtml?faces-redirect=true";
	}
	
	public String updateProduct() {
		if(file.getFileName() != null && file.getSize() > 0) {
		    upload();
		}
		Blog refBlogPost = BlogDAO.find(product.getRefBlogPost().getId());
	    product.setRefBlogPost(refBlogPost);
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
        		
        		BufferedImage im = ImageIO.read(file.getInputstream());
        		BufferedImage mainPhoto = ImageResizer.resize(im, 350, 350);
        		BufferedImage thumbNail = ImageResizer.resize(im, 150, 150);
        		fileName = fileName.replace(fileExtension, "");
        		ImageIO.write(thumbNail, "JPEG", new File(webResourcePath + "/images/thumbnails/" + fileName + product.getId() + fileExtension));
        		ImageIO.write(mainPhoto, "JPEG", new File(webResourcePath + "/images/" + fileName + product.getId() + fileExtension));
        		ImageIO.write(mainPhoto, "JPEG", new File(appDataDirectory + "/images/" + fileName + product.getId() + fileExtension));
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEncodedUrl() {
		return encodedUrl;
	}

	public void setEncodedUrl(String encodedUrl) {
		this.encodedUrl = encodedUrl;
	}
}
