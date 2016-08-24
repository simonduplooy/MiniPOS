package com.lunarsky.minipos.model.dto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;

public class ProductGroupDTO extends PersistenceObjectDTO {
	private static final Logger log = LogManager.getLogger();
	
	final PersistenceId parentId;
	final String name;
	
	public ProductGroupDTO(final PersistenceId parentId, final String name) {
		this(null,parentId,name);
	}
	
	public ProductGroupDTO(final PersistenceId id, final PersistenceId parentId, final String name) {
		super(id);
		//id can be null
		//parentId can be null
		assert(null != name);
		
		this.parentId = parentId;
		this.name = name;
	}
	
	public PersistenceId getParentId() {
		return parentId;
	}
	
	public String getName() {
		assert(null != name);
		return name;
	}
	
	public String toString() {
		return String.format("name:[%s] id:[%s] parentid:[%s]",getName(),getId(),getParentId());
	}
}
