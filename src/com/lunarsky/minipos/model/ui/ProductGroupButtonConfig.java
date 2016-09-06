package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.dto.PersistenceObjectDTO;
import com.lunarsky.minipos.model.dto.ProductGroupButtonConfigDTO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProductGroupButtonConfig extends ProductButtonConfigBase {
	private static final Logger log = LogManager.getLogger();
	
	private StringProperty nameProperty;

	public ProductGroupButtonConfig() {
		nameProperty = new SimpleStringProperty();
	}
	
	public ProductGroupButtonConfig(final PersistenceId parentId, final String name, final Integer columnIdx, final Integer rowIdx) {
		this();
		setParentId(parentId);
		setName(name);
		setColumnIndex(columnIdx);
		setRowIndex(rowIdx);
	}
	
	public ProductGroupButtonConfig(final PersistenceId id, final PersistenceId parentId, final String name, final Integer columnIdx, final Integer rowIdx) {
		this(parentId,name,columnIdx,rowIdx);
	}
	
	public ProductGroupButtonConfig(final ProductGroupButtonConfig config) {
		this(config.getId(),config.getParentId(),config.getName(),config.getColumnIndex(),config.getRowIndex());
	}
	
	public ProductGroupButtonConfig(final ProductGroupButtonConfigDTO configDTO) {
		this();
		setDTO(configDTO);
	}
	
	public ProductGroupButtonConfigDTO getDTO() {
		final ProductGroupButtonConfigDTO configDTO = new ProductGroupButtonConfigDTO(getId().getDTO(),
				getParentId().getDTO(),
				getName(),
				getColumnIndex(),
				getRowIndex());
		return configDTO;
	}
	
	public void setDTO(ProductGroupButtonConfigDTO configDTO) {
		setId(new PersistenceId(configDTO.getId()));
		setParentId(new PersistenceId(configDTO.getParentId()));
		setName(configDTO.getName());
		setColumnIndex(configDTO.getColumnIndex());
		setRowIndex(configDTO.getRowIndex());
		
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
		nameProperty().setValue(name);
	}
	
	public String toString() {
		return String.format("name:[%s] id:[%s] parentId:[%s] columnIdx:[%d] rowIdx:[%d]",getName(),getId(),getParentId(),getColumnIndex(),getRowIndex());
	}
}
