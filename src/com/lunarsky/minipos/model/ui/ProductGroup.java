package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.dto.ProductGroupDTO;

public class ProductGroup extends ProductBase {
	private static final Logger log = LogManager.getLogger();

	public ProductGroup() {
		
	}
	
	public ProductGroup(final PersistenceId parentId, final String name) {
		this();
		setParentId(parentId);
		setName(name);
	}
	
	public ProductGroup(final PersistenceId id, final PersistenceId parentId, final String name) {
		this(parentId,name);
		setId(id);
	}
	
	public ProductGroup(final ProductGroup group) {
		this(group.getId(),group.getParentId(),group.getName());
	}
	
	public ProductGroup(final ProductGroupDTO group) {
		this();
		setDTO(group);
	}

	public void setDTO(final ProductGroupDTO group) {
		setId(new PersistenceId(group.getId()));
		setParentId(new PersistenceId(group.getParentId()));
		setName(group.getName());
	}
	
	public ProductGroupDTO getDTO() {
		final ProductGroupDTO groupDTO = new ProductGroupDTO(getId().getDTO(),getParentId().getDTO(),getName());
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
