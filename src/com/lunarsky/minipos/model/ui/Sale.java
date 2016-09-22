package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Sale extends PersistenceObject {
	private static final Logger log = LogManager.getLogger();
	
	private final StringProperty descriptionProperty;
	private final DoubleProperty totalProperty;

	
	public Sale() {
		descriptionProperty = new SimpleStringProperty();
		totalProperty = new SimpleDoubleProperty();
	}
	
	
	public StringProperty descriptionProperty(){
		return descriptionProperty;
	}
	
	public String getDescription() {
		return descriptionProperty.getValue();
	}
	
	public void setDescription(final String description) {
		descriptionProperty.setValue(description);
	}
	
	public DoubleProperty totalProperty() {
		return totalProperty;
	}
	
	public Double getTotal() {
		return totalProperty.getValue();
	}
	
	public void setTotal(final Double cost) {
		totalProperty.setValue(cost);
	}
	
	@Override
	public String toString() {
		return String.format("description:[%s] total:[%.2f]",getDescription(),getTotal());
	}

}
