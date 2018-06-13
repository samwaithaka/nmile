package com.hcare.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.hcare.dao.BlogDAO;
import com.hcare.dao.CustomerDAO;
import com.hcare.models.Blog;
import com.hcare.models.Customer;

@ManagedBean(name = "blogController", eager = true)
@SessionScoped
public class BlogController {
    
	private Blog blog;
	private UploadedFile file;
	private String resourcePath;
	private String webResourcePath;
	private StreamedContent image;
	@ManagedProperty(value = "#{customerController}")
	private CustomerController customerController;

	
	@PostConstruct
	public void init() {
		blog = new Blog();
	}

	public BlogController() {
		resourcePath = new File(BlogController.class.getClassLoader()
				.getResource(".").getFile()).getAbsolutePath();
		webResourcePath = FacesContext.getCurrentInstance()
				.getExternalContext().getRealPath("/");
	}
	
	public void refresh() {
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext()
				.getRequestParameterMap();
		String slug = params.get("id");
		String ref = params.get("ref"); // article referer
		if(ref != null) {
			int refId = Integer.parseInt(ref);
			Customer customer = new Customer();
			customer.setReferer(CustomerDAO.find(refId));
			customerController.setCustomer(customer);
		}
		if(slug != null) {
			blog = BlogDAO.findBySlug(slug);
			/*if(product != null) {
				File imageFile = new File(resourcePath + "/images/" + product.getFileName());
				try {
					image = new DefaultStreamedContent(new FileInputStream(imageFile),"image/jpeg");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}*/
		}
	}
	
	public String createBlog() {
		System.out.println(blog.getLongText());
		String slug = blog.getTitle().replace(" ", "-").toLowerCase();
		blog.setSlug(slug);
	    BlogDAO.addBlog(blog);
		return "blog.xhtml";
	}
	
	public String updateBlog() {
		BlogDAO.updateBlog(blog);
		return "blog.xhtml";
	}

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getWebResourcePath() {
		return webResourcePath;
	}

	public void setWebResourcePath(String webResourcePath) {
		this.webResourcePath = webResourcePath;
	}

	public StreamedContent getImage() {
		return image;
	}

	public void setImage(StreamedContent image) {
		this.image = image;
	}

	public CustomerController getCustomerController() {
		return customerController;
	}

	public void setCustomerController(CustomerController customerController) {
		this.customerController = customerController;
	}
}
