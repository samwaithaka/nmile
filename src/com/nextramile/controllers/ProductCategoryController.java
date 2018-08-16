package com.nextramile.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextramile.dao.ProductCategoryDAO;
import com.nextramile.models.Product;
import com.nextramile.models.ProductCategory;

@ManagedBean(name = "productCategoryController", eager = true)
@SessionScoped
public class ProductCategoryController {
    
	private ProductCategory productCategory;
	private List<ProductCategory> productCategoryList;
	
	@PostConstruct
	public void init() {
		productCategory = new ProductCategory();
		productCategoryList = ProductCategoryDAO.getProductCategoryList();
	}

	public ProductCategoryController() {
	}
	
	public String createProductCategory() {
	    ProductCategoryDAO.addProductCategory(productCategory);
	    productCategory = new ProductCategory();
	    productCategoryList = ProductCategoryDAO.getProductCategoryList();
	    productCategory = new ProductCategory();
		return "admin-product-category-list.xhtml?faces-redirect=true";
	}
	
	public String updateProductCategory() {
		ProductCategoryDAO.updateProductCategory(productCategory);
		productCategoryList = ProductCategoryDAO.getProductCategoryList();
		productCategory = new ProductCategory();
		return "admin-product-category-list.xhtml?faces-redirect=true";
	}

	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public List<ProductCategory> getProductCategoryList() {
		return productCategoryList;
	}

	public void setProductCategoryList(List<ProductCategory> productCategoryList) {
		this.productCategoryList = productCategoryList;
	}
}
