package com.lunarsky.minipos.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;

public class ProductButtonGroupConfig extends PersistenceObject {
	private static final Logger log = LogManager.getLogger();
	
	final PersistenceId parentId; 
	final String name;
	final Integer column;
	final Integer row;
	
	public ProductButtonGroupConfig(final PersistenceId parentId, final String name, final Integer column, final Integer row) {
		this(null,parentId,name,column,row);
	}
	
	public ProductButtonGroupConfig(final PersistenceId id, final PersistenceId parentId, final String name, final Integer column, final Integer row) {
		super(id);
		
		assert(null != name);
		assert(null != column);		
		assert(null != row);
		
		this.parentId = parentId;
		this.name = name;
		this.column = column;
		this.row = row;
	}
	
	public PersistenceId getParent() {
		return parentId;
	}
	
	public String getName() {
		assert(null != name);
		return name;
	}
	
	public Integer getColumn() {
		assert(null != column);
		return column;
	}
	
	public Integer getRow() {
		assert(null != row);
		return row;
	}
	
	public String toString() {
		return getName()+","+column+","+row;
	}
}
