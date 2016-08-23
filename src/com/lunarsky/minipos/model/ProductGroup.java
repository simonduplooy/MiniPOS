package com.lunarsky.minipos.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;

public class ProductGroup extends PersistenceObject implements Comparable<ProductGroup> {
	private static final Logger log = LogManager.getLogger();
	
	PersistenceId parentId;
	String name;
	
	public ProductGroup() {
		super(null);
	}
	
	public PersistenceId getParentId() {
		return parentId;
	}
	
	public void setParentId(final PersistenceId parentId){
		if(null == parentId) {throw new IllegalArgumentException();}
		if(null != this.parentId) {throw new IllegalArgumentException();}
		this.parentId = parentId;
	}
	
	public String getName() {
		assert(null != name);
		return name;
	}
	
	public void setName(final String name){
		if(null == name) {throw new IllegalArgumentException();}
		if(null != this.name) {throw new IllegalArgumentException();}
		this.name = name;
	}
	
	public ProductGroup duplicate() {
		final ProductGroup group = new ProductGroup();
		group.setParentId(getParentId());
		group.setName(getName());
		return group;
	}
	
	public String toString() {
		return String.format("id:[%s] parentId:[%s] name[%s]",getId(),getParentId(),getName());
	}
	//Implements Comparable
	public int compareTo(ProductGroup group) {
		return getName().compareToIgnoreCase(group.getName());
	}
}
