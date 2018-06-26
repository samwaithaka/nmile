package com.nextramile.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.primefaces.PrimeFaces;

import com.nextramile.dao.ColorDAO;
import com.nextramile.dao.CustomerAddressDAO;
import com.nextramile.dao.DeliveryAddressDAO;
import com.nextramile.dao.OrderDAO;
import com.nextramile.dao.SizeDAO;
import com.nextramile.models.Color;
import com.nextramile.models.Customer;
import com.nextramile.models.CustomerAddress;
import com.nextramile.models.CustomerOrder;
import com.nextramile.models.DeliveryAddress;
import com.nextramile.models.Size;

@ManagedBean(name = "orderController", eager = true)
@SessionScoped
public class OrderController {
    
	private CustomerOrder order;
	private DeliveryAddress deliveryAddress;
	private CustomerAddress customerAddress;
	private List<CustomerAddress> customerAddressList;
	private Color color;
	private Size size;
	private List<Color> colorList;
	private List<Size> sizeList;
	private String addressText;
	
	@ManagedProperty(value = "#{productController}")
	private ProductController productController;

	@ManagedProperty(value = "#{customerController}")
	private CustomerController customerController;

	
	@PostConstruct
	public void init() {
		order = new CustomerOrder();
		color = new Color();
		size = new Size();
		deliveryAddress = new DeliveryAddress();
		customerAddress = new CustomerAddress();
	}

	public void initializeAddress() {
		deliveryAddress = new DeliveryAddress();
		PrimeFaces.current().ajax().update("addressform");
	}
	
	public void edit() {
		PrimeFaces.current().ajax().update("addressform");
	}
	
	public OrderController() {
		colorList = ColorDAO.getColorList();
		sizeList = SizeDAO.getSizeList();
	}
	
	public String makeOrder() {
		Customer customer = customerController.getCustomer();
		if(customer.getId() == 0) {
			return "user.xhtml?faces-redirect=true";
		}
		
		if(order.getId() == 0) {
			order.setCustomer(customer);
			order.setProduct(productController.getProduct());
			order = OrderDAO.addOrder(order);
		}
		customerAddress = CustomerAddressDAO.findCurrentCustomerAddress(customer);
		customerAddressList = CustomerAddressDAO.findAddressByCustomer(customer);
		if(customerAddressList.size() > 0) {
			addressText = "Different Address?";
		} else {
			addressText = "Tell us where you are:";
		}
		return "order.xhtml?faces-redirect=true";
	}
	
	public String updateOrder() {
		size = SizeDAO.find(size.getId());
		color = ColorDAO.find(color.getId());
		order.setSize(size);
		order.setColor(color);
		Customer customer = customerController.getCustomer();
		customerAddress = CustomerAddressDAO.findCurrentCustomerAddress(customer);
		order.setDeliveryAddress(customerAddress.getDeliveryAddress());
		OrderDAO.updateOrder(order);
		return "checkout.xhtml";
	}

	public String checkout() {
		order.setCheckout(true);
		OrderDAO.updateOrder(order);
		order = new CustomerOrder();
		return "successful.xhtml";
	}
	
	public String makeAddressCurrent() {
		customerAddress.setDeliveryAddress(deliveryAddress);
		CustomerAddress currentCustomerAddress = new CustomerAddress();
		currentCustomerAddress.setCustomer(customerAddress.getCustomer());
		currentCustomerAddress.setDeliveryAddress(deliveryAddress);
		CustomerAddressDAO.makeCustomerAddressCurrent(currentCustomerAddress);
		customerAddressList = CustomerAddressDAO.findAddressByCustomer(customerAddress.getCustomer());
		return null;
	}
	
	public String remove() {
		if(order.getId() > 0) {
			order.setActive(false);
	        OrderDAO.updateOrder(order);
		}
		return "home.xhtml?faces-redirect=true";
	}
	
	public void createAddress() {
		deliveryAddress = DeliveryAddressDAO.addDeliveryAddress(deliveryAddress);
		customerAddress.setDeliveryAddress(deliveryAddress);
		customerAddress.setCustomer(customerController.getCustomer());
		CustomerAddressDAO.addCustomerAddress(customerAddress);
		customerAddressList = CustomerAddressDAO.findAddressByCustomer(customerAddress.getCustomer());
	}
    
	public void updateAddress() {
		DeliveryAddressDAO.updateDeliveryAddress(deliveryAddress);
	}

	public CustomerOrder getOrder() {
		return order;
	}

	public void setOrder(CustomerOrder order) {
		this.order = order;
	}
	
	public ProductController getProductController() {
		return productController;
	}

	public void setProductController(ProductController productController) {
		this.productController = productController;
	}

	public CustomerController getCustomerController() {
		return customerController;
	}

	public void setCustomerController(CustomerController customerController) {
		this.customerController = customerController;
	}

	public DeliveryAddress getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public CustomerAddress getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(CustomerAddress customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getAddressText() {
		return addressText;
	}

	public void setAddressText(String addressText) {
		this.addressText = addressText;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public List<Color> getColorList() {
		return colorList;
	}

	public void setColorList(List<Color> colorList) {
		this.colorList = colorList;
	}

	public List<Size> getSizeList() {
		return sizeList;
	}

	public void setSizeList(List<Size> sizeList) {
		this.sizeList = sizeList;
	}

	public List<CustomerAddress> getCustomerAddressList() {
		return customerAddressList;
	}

	public void setCustomerAddressList(List<CustomerAddress> customerAddressList) {
		this.customerAddressList = customerAddressList;
	}
}