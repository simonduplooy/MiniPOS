package com.lunarsky.minipos.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.dto.ProductDTO;

public class ProductButtonConfig extends PersistenceObject {
	private static final Logger log = LogManager.getLogger();
	
	final PersistenceId parentId; 
	final ProductDTO product;
	final Integer columnIdx;
	final Integer rowIdx;
	
	public ProductButtonConfig(final PersistenceId parentId, final ProductDTO product, final Integer columnIdx, final Integer rowIdx) {
		this(null,parentId,product,columnIdx,rowIdx);
	}
	
	public ProductButtonConfig(final PersistenceId id, final PersistenceId parentId, final ProductDTO product, final Integer columnIdx, final Integer rowIdx) {
		super(id);
		
		assert(null != product);
		assert(null != columnIdx);		
		assert(null != rowIdx);
		
		this.parentId = parentId;
		this.product = product;
		this.columnIdx = columnIdx;
		this.rowIdx = rowIdx;
	}
	
	public PersistenceId getParentId() {
		return parentId;
	}
	
	public ProductDTO getProduct() {
		assert(null != product);
		return product;
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
		return getProduct().getName()+","+columnIdx+","+rowIdx;
	}
}
