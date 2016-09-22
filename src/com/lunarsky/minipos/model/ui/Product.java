package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.dto.ProductDTO;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Product extends ProductBase {
	private static final Logger log = LogManager.getLogger();
	
	private final DoubleProperty priceProperty;
	
	public Product() {
		priceProperty = new SimpleDoubleProperty();
	}
	

	public Product(final PersistenceId parentId, final String name, final Double price) {
		this();
		setParentId(parentId);
		setName(name);
		setPrice(price);
		
	}
	
	public Product(final PersistenceId id, final PersistenceId parentId, final String name, final Double price) {
		this(parentId,name,price);
		setId(id);
	}
	
	public Product(final Product product) {
		this(product.getId(),product.getParentId(),product.getName(),product.getPrice());
	}

	public Product(final ProductDTO productDTO) {
		this();
		setDTO(productDTO);
	}
	
	public void setDTO(final ProductDTO productDTO) {
		setId(new PersistenceId(productDTO.getId()));
		setParentId(new PersistenceId(productDTO.getParentId()));
		setName(productDTO.getName());
		setPrice(productDTO.getPrice());
	}
	
	public ProductDTO getDTO() {
		final ProductDTO productDTO = new ProductDTO(getId().getDTO(),getParentId().getDTO(),getName(),getPrice());
		return productDTO;
	}
	

	public DoubleProperty priceProperty() {
		return priceProperty;
	}
	
	public Double getPrice() {
		return priceProperty().getValue();
	}
	
	public void setPrice(final Double price) {
		priceProperty.set(price);
	}
	
	public Product duplicate() {
		//Do not duplicate the ID
		final Product product = new Product(getParentId(),getName(),getPrice());
		return product;
	}
	
	@Override
	public String toString() {
		return String.format("name:[%s] id:[%s] parentId:[%s] price:[%.2f]",getName(),getId(),getParentId(),getPrice());
	}

}
