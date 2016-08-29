package com.lunarsky.minipos.model.ui;

import com.lunarsky.minipos.interfaces.PersistenceId;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProductBase extends PersistenceObject implements Comparable<ProductBase> {

	private PersistenceId parentId;
	private final StringProperty nameProperty;
	
	public ProductBase(final PersistenceId id, final PersistenceId parentId, final String name) {
		//id can be null
		//parentId can be null
		super(id);
		assert(null != name);

		nameProperty = new SimpleStringProperty(name);

		setParentId(parentId);
		setName(name);
	}
	
	public PersistenceId getParentId() {
		return parentId;
	}
	
	public void setParentId(final PersistenceId parentId) {
		this.parentId = parentId;
	}
	
	public StringProperty nameProperty() {
		assert(null != nameProperty);
		return nameProperty;
	}
	
	public String getName() {
		return nameProperty().getValue();
	}
	
	public void setName(final String name) {
		assert(null != name);
		nameProperty().set(name);
	}
	
	//Implements Comparable
	public int compareTo(final ProductBase product) {
		final int result = getName().compareToIgnoreCase(product.getName());
		return result;
	}
}
