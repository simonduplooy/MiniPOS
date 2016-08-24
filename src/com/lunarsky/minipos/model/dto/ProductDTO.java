package com.lunarsky.minipos.model.dto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;

public class ProductDTO extends PersistenceObjectDTO {
	private static final Logger log = LogManager.getLogger();
	
	final PersistenceId parentId;
	final String name;
	final Double price;
	
	public ProductDTO(final PersistenceId parentId, final String name, final Double price) {
		this(null,parentId,name,price);
	}
	
	public ProductDTO(final PersistenceId id, final PersistenceId parentId, final String name, final Double price) {
		super(id);
		//id can be null
		//parentId can be null
		assert(null != name);
		assert(null != price);
		
		this.parentId = parentId;
		this.name = name;
		this.price = price;
	}
	
	public PersistenceId getParentId() {
		return parentId;
	}
	
	public String getName() {
		assert(null != name);
		return name;
	}
	
	public Double getPrice() {
		assert(null != price);
		return price;
	}
	
	public String toString() {
		return String.format("name:[%s] id:[%s] parentid:[%s] price:[%.2f]",getName(),getId(),getParentId(),getPrice());
	}
}
