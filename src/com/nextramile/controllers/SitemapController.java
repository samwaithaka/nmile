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
import com.nextramile.dao.ProductDAO;
import com.nextramile.models.Blog;
import com.nextramile.models.BlogCategory;
import com.nextramile.models.Customer;
import com.nextramile.models.Product;
import com.nextramile.util.Configs;

@ManagedBean(name = "sitemapController", eager = true)
@SessionScoped
public class SitemapController {
    
	private List<Blog> blogList;
	private List<Product> productList;
	private String sitemap;

	@PostConstruct
	public void init() {
	}

	public SitemapController() {
	}
	
	public void refresh() {
		blogList = BlogDAO.getBlogList();
		productList = ProductDAO.getProductList();
		
		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		builder.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
		for(Blog blog : blogList) {
			builder.append("<url><loc>" + Configs.getConfig("appurl") + "article.xhtml?id=" + blog.getSlug() + "</loc></url>");
		}
		for(Product product : productList) {
			builder.append("<url><loc>" + Configs.getConfig("appurl") + "product-details.xhtml?id=" + product.getId() + "</loc></url>");
		}
		builder.append("</urlset>");
		sitemap = builder.toString().trim();
	}

	public String getSitemap() {
		return sitemap;
	}

	public void setSitemap(String sitemap) {
		this.sitemap = sitemap;
	}
}
