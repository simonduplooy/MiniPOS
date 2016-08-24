package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.dto.ProductDTO;
import com.lunarsky.minipos.model.dto.ProductGroupDTO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProductGroup extends ProductBase implements Comparable<ProductGroup> {
	private static final Logger log = LogManager.getLogger();
	
	private PersistenceId parentId;
	private final StringProperty nameProperty;

	public ProductGroup() {
		nameProperty = new SimpleStringProperty("");
	}
	
	public ProductGroup(final PersistenceId parentId) {
		this();
		setParentId(parentId);
	}
		
	public ProductGroup(final ProductGroupDTO group) {
		this();
		assert(null != group);

		setId(group.getId());
		setParentId(group.getParentId());
		setName(group.getName());
	}
	
	public ProductGroupDTO createDTO() {
		final ProductGroupDTO groupDTO = new ProductGroupDTO(getId(),getParentId(),getName());
		return groupDTO;
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

	//TODO
	/*
	public ProductGroup duplicate() {
		final ProductGroup group = new ProductGroup(getParentId(),getName(),getProduct().getPrice());
		return group;
	}
	*/
	
	public String toString() {
		return String.format("name:[%s] id:[%s] parentId:[%s]",getName(),getId(),getParentId());
	}

	//Implements Comparable
	public int compareTo(ProductGroup group) {
		return getName().compareToIgnoreCase(group.getName());
	}}
