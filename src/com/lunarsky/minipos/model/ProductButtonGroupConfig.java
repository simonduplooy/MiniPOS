package com.lunarsky.minipos.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;

public class ProductButtonGroupConfig extends PersistenceObject {
	private static final Logger log = LogManager.getLogger();
	
	final PersistenceId parentId; 
	final String name;
	final Integer columnIdx;
	final Integer rowIdx;
	
	public ProductButtonGroupConfig(final PersistenceId parentId, final String name, final Integer columnIdx, final Integer rowIdx) {
		this(null,parentId,name,columnIdx,rowIdx);
	}
	
	public ProductButtonGroupConfig(final PersistenceId id, final PersistenceId parentId, final String name, final Integer columnIdx, final Integer rowIdx) {
		super(id);
		
		assert(null != name);
		assert(null != columnIdx);		
		assert(null != rowIdx);
		
		this.parentId = parentId;
		this.name = name;
		this.columnIdx = columnIdx;
		this.rowIdx = rowIdx;
	}
	
	public PersistenceId getParentId() {
		return parentId;
	}
	
	public String getName() {
		assert(null != name);
		return name;
	}
	
	public Integer getColumnIndex() {
		assert(null != columnIdx);
		return columnIdx;
	}
	
	public Integer getRowIndex() {
		assert(null != rowIdx);
		return rowIdx;
	}
	
	public String toString() {
		return getName()+","+columnIdx+","+rowIdx;
	}
}
