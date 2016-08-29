package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.dto.ProductDTO;
import com.lunarsky.minipos.model.dto.ProductGroupDTO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProductGroup extends ProductBase {
	private static final Logger log = LogManager.getLogger();

	public ProductGroup(final PersistenceId id, final PersistenceId parentId, final String name) {
		super(id,parentId,name);
	}
	
	public ProductGroup(final ProductGroup group) {
		this(group.getId(),group.getParentId(),group.getName());
	}
	
	public ProductGroup(final ProductGroupDTO group) {
		this(group.getId(),group.getParentId(),group.getName());
	}

	public void set(final ProductGroupDTO group) {
		assert(null != group);

		setId(group.getId());
		setParentId(group.getParentId());
		setName(group.getName());
	}
	
	public ProductGroupDTO createDTO() {
		final ProductGroupDTO groupDTO = new ProductGroupDTO(getId(),getParentId(),getName());
		return groupDTO;
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
}
