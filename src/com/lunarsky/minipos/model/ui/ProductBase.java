package com.lunarsky.minipos.model.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProductBase extends PersistenceObject implements Comparable<ProductBase> {

	private PersistenceId parentId;
	private final StringProperty nameProperty;

	public ProductBase() {
		parentId = new PersistenceId();
		nameProperty = new SimpleStringProperty();
	}
	
	public ProductBase(final String name) {
		this();
		setName(name);
	}
	
	public ProductBase(final PersistenceId id, final String name) {
		this(name);
		setId(id);
	}
	
	public PersistenceId getParentId() {
		return parentId;
	}
	
	public void setParentId(final PersistenceId parentId) {
		this.parentId = parentId;
	}
	
	public StringProperty nameProperty() {
		return nameProperty;
	}
	
	public String getName() {
		return nameProperty().getValue();
	}
	
	public void setName(final String name) {
		nameProperty().set(name);
	}
	
	//Implements Comparable
	public int compareTo(final ProductBase product) {
		final int result = getName().compareToIgnoreCase(product.getName());
		return result;
	}
}
