package com.nextramile.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.primefaces.PrimeFaces;

import com.nextramile.dao.CustomerAddressDAO;
import com.nextramile.dao.DeliveryAddressDAO;
import com.nextramile.dao.OrderDAO;
import com.nextramile.models.Customer;
import com.nextramile.models.CustomerAddress;
import com.nextramile.models.CustomerOrder;
import com.nextramile.models.DeliveryAddress;
import com.nextramile.models.ShoppingCart;

@ManagedBean(name = "orderController", eager = true)
@SessionScoped
public class OrderController {
    
	private CustomerOrder order;
	private DeliveryAddress deliveryAddress;
	private CustomerAddress customerAddress;
	private List<CustomerAddress> customerAddressList;
	private String addressText;
	private ShoppingCart shoppingCart;
	private boolean showAddressForm;
	
	@ManagedProperty(value = "#{productController}")
	private ProductController productController;

	@ManagedProperty(value = "#{customerController}")
	private CustomerController customerController;

	public void refresh() {
		Customer customer = customerController.getCustomer();
		customerAddress = CustomerAddressDAO.findCurrentCustomerAddress(customer);
		customerAddressList = CustomerAddressDAO.findAddressByCustomer(customer);
		if(customerAddress.getId() > 0) {
			addressText = "New delivery address?";
			showAddressForm = showAddressForm == true ? showAddressForm : false;
		} else {
			addressText = "Tell us where you are:";
		}
	}
	
	@PostConstruct
	public void init() {
		//order = new CustomerOrder();
		deliveryAddress = new DeliveryAddress();
		//customerAddress = new CustomerAddress();
	}

	public void initializeAddress() {
		//deliveryAddress = new DeliveryAddress();
		showAddressForm = true;
		Customer customer = customerController.getCustomer();
		customerAddress = CustomerAddressDAO.findCurrentCustomerAddress(customer);
		customerAddressList = CustomerAddressDAO.findAddressByCustomer(customer);
		if(customerAddressList.size() > 0) {
			addressText = "New delivery Address?";
		} else {
			addressText = "Tell us where you are:";
		}
  	}
	
	public void revertAddress() { 
		showAddressForm = false;
	}
	
	public void edit() {
		PrimeFaces.current().ajax().update("addressform");
	}
	
	public OrderController() {
	}
	
	public String completePurchase() {
		System.out.println(shoppingCart);
		CustomerOrder order = new CustomerOrder();
		order.setShoppingCart(shoppingCart);
		order.setDeliveryAddress(customerAddress.getDeliveryAddress());
		OrderDAO.addOrder(order);
		return "successful.xhtml?faces-redirect=true";
	}
	
	public String makeOrder() {
		Customer customer = customerController.getCustomer();
		if(customer.getId() == 0) {
			return "user.xhtml?faces-redirect=true";
		}
		
		if(order.getId() == 0) {
			//order.setCustomer(customer);
			//order.setProduct(productController.getProduct());
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
		addressText = "Different Address?";
		showAddressForm = false;
	}
    
	public void updateAddress() {
		DeliveryAddressDAO.updateDeliveryAddress(deliveryAddress);
		showAddressForm = false;
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

	public List<CustomerAddress> getCustomerAddressList() {
		return customerAddressList;
	}

	public void setCustomerAddressList(List<CustomerAddress> customerAddressList) {
		this.customerAddressList = customerAddressList;
	}

	public boolean getShowAddressForm() {
		return showAddressForm;
	}

	public void setShowAddressForm(boolean showAddressForm) {
		this.showAddressForm = showAddressForm;
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}
	
	
}
