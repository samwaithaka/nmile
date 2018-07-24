package com.nextramile.controllers;

import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

//import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.nextramile.dao.BlogCategoryDAO;
import com.nextramile.dao.BlogDAO;
import com.nextramile.dao.CustomerDAO;
import com.nextramile.models.Blog;
import com.nextramile.models.BlogCategory;
import com.nextramile.models.Customer;

@ManagedBean(name = "blogController", eager = true)
@SessionScoped
public class BlogController {
    
	private Blog blog;
	private List<Blog> blogList;
	private UploadedFile file;
	private String resourcePath;
	private String webResourcePath;
	private StreamedContent image;
	@ManagedProperty(value = "#{customerController}")
	private CustomerController customerController;

	
	@PostConstruct
	public void init() {
		blog = new Blog();
		blog.setBlogCategory(new BlogCategory());
	}

	public BlogController() {
		resourcePath = new File(BlogController.class.getClassLoader()
				.getResource(".").getFile()).getAbsolutePath();
		webResourcePath = FacesContext.getCurrentInstance()
				.getExternalContext().getRealPath("/");
		blogList = BlogDAO.getBlogList();
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
		String slug = blog.getTitle().replace(" ", "-").toLowerCase();
		blog.setBlogCategory(BlogCategoryDAO.find(blog.getBlogCategory().getId()));
		blog.setSlug(slug);
		System.out.println(blog);
	    BlogDAO.addBlog(blog);
	    blogList = BlogDAO.getBlogList();
		return "admin-blog-list.xhtml";
	}
	
	public String updateBlog() {
		BlogDAO.updateBlog(blog);
		blogList = BlogDAO.getBlogList();
		return "admin-blog-list.xhtml";
	}

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

	public List<Blog> getBlogList() {
		return blogList;
	}

	public void setBlogList(List<Blog> blogList) {
		this.blogList = blogList;
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
