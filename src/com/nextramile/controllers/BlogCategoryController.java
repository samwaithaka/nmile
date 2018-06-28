package com.nextramile.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextramile.dao.BlogCategoryDAO;
import com.nextramile.models.BlogCategory;

@ManagedBean(name = "blogCategoryController", eager = true)
@SessionScoped
public class BlogCategoryController {
    
	private BlogCategory blogCategory;
	
	@PostConstruct
	public void init() {
		blogCategory = new BlogCategory();
	}

	public BlogCategoryController() {
	}
	
	public String createBlogCategory() {
	    BlogCategoryDAO.addBlogCategory(blogCategory);
	    blogCategory = new BlogCategory();
		return "blogCategory.xhtml";
	}
	
	public String updateBlogCategory() {
		BlogCategoryDAO.updateBlogCategory(blogCategory);
		return "blogCategory.xhtml";
	}

	public BlogCategory getBlogCategory() {
		return blogCategory;
	}

	public void setBlogCategory(BlogCategory blogCategory) {
		this.blogCategory = blogCategory;
	}
}
