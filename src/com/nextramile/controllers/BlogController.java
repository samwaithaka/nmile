package com.nextramile.controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

//import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.nextramile.dao.BlogCategoryDAO;
import com.nextramile.dao.BlogDAO;
import com.nextramile.dao.CustomerDAO;
import com.nextramile.dao.ProductDAO;
import com.nextramile.dao.SlideDAO;
import com.nextramile.models.Blog;
import com.nextramile.models.BlogCategory;
import com.nextramile.models.Customer;
import com.nextramile.models.Product;
import com.nextramile.util.Configs;
import com.nextramile.util.FileOperations;
import com.nextramile.util.ImageResizer;

@ManagedBean(name = "blogController", eager = true)
@SessionScoped
public class BlogController {
    
	private Blog blog;
	private List<Blog> blogList;
	private UploadedFile file;
	private String appDataDirectory;
	private String webResourcePath;
	private StreamedContent image;
	@ManagedProperty(value = "#{customerController}")
	private CustomerController customerController;
	private String url;
	private String encodedUrl;

	
	@PostConstruct
	public void init() {
		blog = new Blog();
		blog.setBlogCategory(new BlogCategory());
	}

	public BlogController() {
		appDataDirectory = System.getProperty("user.home") + "/." + Configs.getConfig("dir");
		webResourcePath = FacesContext.getCurrentInstance()
				.getExternalContext().getRealPath("/");
		blogList = BlogDAO.getBlogList();
		for(Blog blg : blogList) {
			FileOperations.copyFile(appDataDirectory + "blog/" + blg.getImageFileName(), 
					webResourcePath + "/blog/" + blg.getImageFileName());
		}
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
			int loggedId = customerController.getCustomer().getId();
			String queryString = loggedId > 0 ? "&ref=" + loggedId : "";
			url = Configs.getConfig("appurl") + "article.xhtml?id=" + slug + queryString;
			try {
				encodedUrl = URLEncoder.encode(url, "UTF-8");
			} catch (UnsupportedEncodingException e1) {}
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
		blog = BlogDAO.addBlog(blog);
		if(file != null) {
		    upload();
		}
	    blogList = BlogDAO.getBlogList();
	    blog = new Blog();
		return "admin-blog-list.xhtml?faces-redirect=true";
	}
	
	public String updateBlog() {
		if(file != null) {
		    upload();
		}
		BlogDAO.updateBlog(blog);
		blogList = BlogDAO.getBlogList();
		blog = new Blog();
		return "admin-blog-list.xhtml?faces-redirect=true";
	}
	
	public void upload() {
		FacesMessage message = null;
		if(file.getFileName() != null && file.getSize() > 0) {
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
        		ImageIO.write(mainPhoto, "JPEG", new File(webResourcePath + "/blog/" + fileName + blog.getId() + fileExtension));
        		ImageIO.write(mainPhoto, "JPEG", new File(appDataDirectory + "/blog/" + fileName + blog.getId() + fileExtension));
        		blog.setImageFileName(fileName + blog.getId() + fileExtension);
				BlogDAO.updateBlog(blog);
			} catch (IOException e) {
				e.printStackTrace();
			}
            message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
        } else {
        	message = new FacesMessage("Error", "Error occured!.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
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
