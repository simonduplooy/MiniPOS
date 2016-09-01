package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.dto.ProductDTO;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product extends ProductBase {
	private static final Logger log = LogManager.getLogger();
	
	private final DoubleProperty priceProperty;
	
	public Product(final PersistenceId id, final PersistenceId parentId, final String name, final Double price) {
		super(id,parentId,name);
		
		//id can be null
		//parentId can be null
		assert(null != name);
		assert(null != price);
		
		priceProperty = new SimpleDoubleProperty(price);
		
		setId(id);
		setParentId(parentId);
		setName(name);
		setPrice(price);
	}
	
	public Product(final PersistenceId parentId, final String name, final Double price) {
		this(null,parentId,name,price);
	}
	
	public Product(final Product product) {
		this(product.getId(),product.getParentId(),product.getName(),product.getPrice());
	}

	public Product(final ProductDTO productDTO) {
		this(productDTO.getId(),productDTO.getParentId(),productDTO.getName(),productDTO.getPrice());
	}
	
	public void set(final ProductDTO productDTO) {
		assert(null != productDTO);
		setId(productDTO.getId());
		setParentId(productDTO.getParentId());
		setName(productDTO.getName());
		setPrice(productDTO.getPrice());
	}
	
	public ProductDTO createDTO() {
		final ProductDTO productDTO = new ProductDTO(getId(),getParentId(),getName(),getPrice());
		return productDTO;
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
	
	public Product duplicate() {
		//Do not duplicate the ID
		final Product product = new Product(null,getParentId(),getName(),getPrice());
		return product;
	}
	
	public String toString() {
		return String.format("name:[%s] id:[%s] parentId:[%s] price:[%s]",getName(),getId(),getParentId(),getPrice());
	}

}
