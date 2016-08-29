package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.dto.PersistenceObjectDTO;
import com.lunarsky.minipos.model.dto.ProductButtonConfigDTO;
import com.lunarsky.minipos.model.dto.ProductDTO;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ProductButtonConfig extends PersistenceObjectDTO {
	private static final Logger log = LogManager.getLogger();
	
	private PersistenceId parentId; 
	private ObjectProperty<Product> productProperty;
	private Integer columnIdx;
	private Integer rowIdx;
	
	public ProductButtonConfig(final PersistenceId parentId, final Product product, final Integer columnIdx, final Integer rowIdx) {
		this(null,parentId,product,columnIdx,rowIdx);
	}
	
	public ProductButtonConfig(final PersistenceId id, final PersistenceId parentId, final Product product, final Integer columnIdx, final Integer rowIdx) {
		super(id);

		productProperty = new SimpleObjectProperty<Product>(product);	
		
		setParentId(parentId);
		setProduct(product);
		setColumnIndex(columnIdx);
		setRowIndex(rowIdx);
	}
	
	public ProductButtonConfig(final ProductButtonConfigDTO configDTO) {
		this(configDTO.getId(),configDTO.getParentId(),new Product(configDTO.getProduct()),configDTO.getColumnIndex(),configDTO.getRowIndex());
	}
	
	public void set(final ProductButtonConfigDTO configDTO) {
		assert(null != configDTO);
		setId(configDTO.getId());
		setParentId(configDTO.getParentId());
		final Product product = new Product(configDTO.getProduct());
		setProduct(product);
		setColumnIndex(configDTO.getColumnIndex());
		setRowIndex(configDTO.getRowIndex());
	}
	
	public void set(final ProductButtonConfig config) {
		setId(config.getId());
		setParentId(config.getParentId());
		setProduct(config.getProduct());
		setColumnIndex(config.getColumnIndex());
		setRowIndex(config.getRowIndex());
	}
	
	public ProductButtonConfigDTO createDTO() {
		final ProductDTO productDTO = getProduct().createDTO();
		final ProductButtonConfigDTO configDTO = new ProductButtonConfigDTO(getId(),getParentId(),productDTO,getColumnIndex(),getRowIndex());
		return configDTO;
	}
	
	public PersistenceId getParentId() {
		return parentId;
	}
	
	public void setParentId(final PersistenceId parentId) {
		this.parentId = parentId;
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
	
	public Integer getColumnIndex() {
		assert(null != columnIdx);
		return columnIdx;
	}
	
	public void setColumnIndex(final Integer columnIdx) {
		assert(null != columnIdx);
		this.columnIdx = columnIdx;
	}
	
	public Integer getRowIndex() {
		assert(null != rowIdx);
		return rowIdx;
	}
	
	public void setRowIndex(final Integer rowIdx) {
		assert(null != rowIdx);
		this.rowIdx = rowIdx;
	}
	
	public String toString() {
		return String.format("product:[%s] parentId:[%s] columnIdx:[%s] rowIdx[%s]",getProduct(),getParentId(),getColumnIndex(),getRowIndex());
	}
}
