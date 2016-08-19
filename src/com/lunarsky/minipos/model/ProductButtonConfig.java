package com.lunarsky.minipos.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;

public class ProductButtonConfig extends PersistenceObject {
	private static final Logger log = LogManager.getLogger();
	
	final PersistenceId parentId; 
	final Product product;
	final Integer column;
	final Integer row;
	
	public ProductButtonConfig(final PersistenceId parentId, final Product product, final Integer column, final Integer row) {
		this(null,parentId,product,column,row);
	}
	
	public ProductButtonConfig(final PersistenceId id, final PersistenceId parentId, final Product product, final Integer column, final Integer row) {
		super(id);
		
		assert(null != product);
		assert(null != column);		
		assert(null != row);
		
		this.parentId = parentId;
		this.product = product;
		this.column = column;
		this.row = row;
	}
	
	public PersistenceId getParent() {
		return parentId;
	}
	
	public Product getProduct() {
		assert(null != product);
		return product;
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
		return getProduct().getName()+","+column+","+row;
	}
}
