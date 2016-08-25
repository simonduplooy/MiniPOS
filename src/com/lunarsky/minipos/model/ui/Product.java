package com.lunarsky.minipos.model.ui;

import java.text.DecimalFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.dto.ProductDTO;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product extends ProductBase implements Comparable<Product>{
	private static final Logger log = LogManager.getLogger();
	
	private PersistenceId parentId;
	private final StringProperty nameProperty;
	private final DoubleProperty priceProperty;

	public Product() {
		nameProperty = new SimpleStringProperty("");
		priceProperty = new SimpleDoubleProperty(0.0);
	}
	
	public Product(final PersistenceId parentId) {
		this();
		setParentId(parentId);
	}
		
	public Product(final ProductDTO product) {
		this();
		assert(null != product);

		setId(product.getId());
		setParentId(product.getParentId());
		setName(product.getName());
		setPrice(product.getPrice());
	}
	
	public ProductDTO createDTO() {
		final ProductDTO productDTO = new ProductDTO(getId(),getParentId(),getName(),getPrice());
		return productDTO;
	}
	
	public PersistenceId getParentId() {
		return parentId;
	}
	
	public void setParentId(final PersistenceId parentId) {
		assert(null == this.parentId);
		this.parentId = parentId;
	}
	
	public StringProperty nameProperty() {
		assert(null != nameProperty);
		return nameProperty;
	}
	
	public String getName() {
		return nameProperty().getValue();
	}
	
	public void setName(final String name) {
		assert(null != name);
		nameProperty().set(name);
	}
	
	public DoubleProperty priceProperty() {
		assert(null != priceProperty);
		return priceProperty;
	}
	
	public Double getPrice() {
		return priceProperty().getValue();
	}
	
	public void setPrice(final Double price) {
		assert(null != price);
		priceProperty.set(price);
	}
	
	//TODO
	/*
	public Product duplicate() {
		final Product product = new Product(getName(),getProduct().getPrice());
		return product;
	}
	*/
	
	public String toString() {
		return String.format("name:[%s] id:[%s] parentId:[%s] price:[%s]",getName(),getId(),getParentId(),getPrice());
	}

	//Implements Comparable
	public int compareTo(Product product) {
		return getName().compareToIgnoreCase(product.getName());
	}
}
