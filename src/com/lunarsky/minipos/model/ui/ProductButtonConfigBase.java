package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.property.StringProperty;

public class ProductButtonConfigBase extends PersistenceObject {
	
	private static final Logger log = LogManager.getLogger();
	
	private PersistenceId parentId; 
	private Integer columnIdx;
	private Integer rowIdx;
	
	public ProductButtonConfigBase() {
		parentId = new PersistenceId();
		columnIdx = new Integer(0);
		rowIdx = new Integer(0);
	}
	
	public ProductButtonConfigBase(final PersistenceId parentId, final Integer columnIdx, final Integer rowIdx) {
		this();
		setParentId(parentId);
		setColumnIndex(columnIdx);
		setRowIndex(rowIdx);
	}
	
	public PersistenceId getParentId() {
		return parentId;
	}
	
	public void setParentId(final PersistenceId parentId) {
		this.parentId = parentId;
	}
	
	public Integer getColumnIndex() {
		return columnIdx;
	}
	
	public void setColumnIndex(final Integer columnIdx) {
		this.columnIdx = columnIdx;
	}
	
	public Integer getRowIndex() {
		return rowIdx;
	}
	
	public void setRowIndex(final Integer rowIdx) {
		this.rowIdx = rowIdx;
	}
	
	@Override
	public String toString() {
		return String.format("id:[%s] parentId:[%s] columnIdx:[%d] rowIdx:[%d]",getId(),getParentId(),getColumnIndex(),getRowIndex());
	}

}
