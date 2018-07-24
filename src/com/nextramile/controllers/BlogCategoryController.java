package com.nextramile.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextramile.dao.BlogCategoryDAO;
import com.nextramile.models.BlogCategory;

@ManagedBean(name = "blogCategoryController", eager = true)
@SessionScoped
public class BlogCategoryController {
    
	private BlogCategory blogCategory;
	private List<BlogCategory> blogCategoryList;
	
	@PostConstruct
	public void init() {
		blogCategory = new BlogCategory();
		blogCategoryList = BlogCategoryDAO.getBlogCategoryList();
	}

	public BlogCategoryController() {
	}
	
	public String createBlogCategory() {
	    BlogCategoryDAO.addBlogCategory(blogCategory);
	    blogCategory = new BlogCategory();
	    blogCategoryList = BlogCategoryDAO.getBlogCategoryList();
		return "admin-blog-category-list.xhtml";
	}
	
	public String updateBlogCategory() {
		BlogCategoryDAO.updateBlogCategory(blogCategory);
		blogCategoryList = BlogCategoryDAO.getBlogCategoryList();
		return "admin-blog-category-list.xhtml";
	}

	public BlogCategory getBlogCategory() {
		return blogCategory;
	}

	public void setBlogCategory(BlogCategory blogCategory) {
		this.blogCategory = blogCategory;
	}

	public List<BlogCategory> getBlogCategoryList() {
		return blogCategoryList;
	}

	public void setBlogCategoryList(List<BlogCategory> blogCategoryList) {
		this.blogCategoryList = blogCategoryList;
	}
}
