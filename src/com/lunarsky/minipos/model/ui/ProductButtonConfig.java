package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.dto.ProductButtonConfigDTO;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ProductButtonConfig extends ProductButtonConfigBase {
	private static final Logger log = LogManager.getLogger();
	
	private ObjectProperty<Product> productProperty;

	public ProductButtonConfig() {
		productProperty = new SimpleObjectProperty<Product>();	
	}
	
	public ProductButtonConfig(final PersistenceId parentId, final Product product, final Integer columnIdx, final Integer rowIdx) {
		this();
		setParentId(parentId);
		setProduct(product);
		setColumnIndex(columnIdx);
		setRowIndex(rowIdx);
	}
	
	public ProductButtonConfig(final PersistenceId id, final PersistenceId parentId, final Product product, final Integer columnIdx, final Integer rowIdx) {
		this(parentId,product,columnIdx,rowIdx);
		setId(id);
	}
	
	public ProductButtonConfig(final ProductButtonConfigDTO configDTO) {
		this();
		setDTO(configDTO);
	}
	
	public ProductButtonConfigDTO getDTO() {
		final ProductButtonConfigDTO configDTO = new ProductButtonConfigDTO(getId().getDTO(),getParentId().getDTO(),getProduct().getDTO(),getColumnIndex(),getRowIndex());
		return configDTO;
	}
	
	public void setDTO(final ProductButtonConfigDTO configDTO) {
		setId(new PersistenceId(configDTO.getId()));
		setParentId(new PersistenceId(configDTO.getParentId()));
		final Product product = new Product(configDTO.getProduct());
		setProduct(product);
		setColumnIndex(configDTO.getColumnIndex());
		setRowIndex(configDTO.getRowIndex());
	}

	public ObjectProperty<Product> productProperty() {
		assert(null != productProperty);
		return productProperty;
	}
	
	public Product getProduct() {
		return productProperty().get();
	}
	
	public void setProduct(final Product product) {
		productProperty().set(product);
	}
	
	public String toString() {
		return String.format("product:[%s] parentId:[%s] columnIdx:[%s] rowIdx[%s]",getProduct(),getParentId(),getColumnIndex(),getRowIndex());
	}
}
