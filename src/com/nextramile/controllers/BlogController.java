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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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
import com.nextramile.util.CookieManager;
import com.nextramile.util.FileOperations;
import com.nextramile.util.ImageResizer;
import com.nextramile.util.TextFileOperations;

@ManagedBean(name = "blogController", eager = true)
@SessionScoped
public class BlogController {
    
	private Blog blog;
	private List<Blog> blogList;
	private BlogCategory blogCategory;
	private List<BlogCategory> blogCategoryList;
	private UploadedFile file;
	private String appDataDirectory;
	private String webResourcePath;
	private StreamedContent image;
	@ManagedProperty(value = "#{customerController}")
	private CustomerController customerController;
	private String url;
	private String encodedUrl;
	private int blogListSize;

	
	@PostConstruct
	public void init() {
		blog = new Blog();
		blog.setBlogCategory(new BlogCategory());
	}

	public BlogController() {
		blogCategoryList = BlogCategoryDAO.getBlogCategoryList();
		appDataDirectory = System.getProperty("user.home") + "/." + Configs.getConfig("dir");
		webResourcePath = FacesContext.getCurrentInstance()
				.getExternalContext().getRealPath("/");
		blogList = BlogDAO.getBlogList();
		blogListSize = 1;
		for(Blog blg : blogList) {
			FileOperations.copyFile(appDataDirectory + "blog/" + blg.getImageFileName(), 
					webResourcePath + "/blog/" + blg.getImageFileName());
		}
	}
	
	public void refresh() {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, String> params = context.getRequestParameterMap();
		String slug = params.get("id");
		String ref = params.get("ref"); // article referer
		ref = ref == "" ? null : ref;
		if(ref != null) {
			CookieManager.setCookie(context, "refId", ref, 60 * 60 * 24 * 30);
			int refId = Integer.parseInt(ref);
			Customer customer = new Customer();
			customer.setReferer(CustomerDAO.find(refId));
			customerController.setCustomer(customer);
		}
		if(slug != null) {
			int loggedId = customerController.getCustomer().getId();
			String queryString = loggedId > 0 ? ";" + loggedId : "";
			url = Configs.getConfig("appurl") + "post/" + slug + queryString;
			try {
				encodedUrl = URLEncoder.encode(url, "UTF-8");
			} catch (UnsupportedEncodingException e1) {}
			blog = BlogDAO.findBySlug(slug);
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
		TextFileOperations.writePageUrl(webResourcePath + "sitemap.txt", "post/" + blog.getSlug());
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

	public String displayCategoryBlogs() {
	    blogList = BlogDAO.getBlogListByCategory(blogCategory);	
	    blogListSize = blogList.size();
	    return "blog.xhtml?face-redirect=true";
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

	public List<BlogCategory> getBlogCategoryList() {
		return blogCategoryList;
	}

	public void setBlogCategoryList(List<BlogCategory> blogCategoryList) {
		this.blogCategoryList = blogCategoryList;
	}
	
	public BlogCategory getBlogCategory() {
		return blogCategory;
	}

	public void setBlogCategory(BlogCategory blogCategory) {
		this.blogCategory = blogCategory;
	}

	public int getBlogListSize() {
		return blogListSize;
	}

	public void setBlogListSize(int blogListSize) {
		this.blogListSize = blogListSize;
	}
}
