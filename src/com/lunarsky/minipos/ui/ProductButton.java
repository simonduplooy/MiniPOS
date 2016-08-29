package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.ui.Product;
import com.lunarsky.minipos.model.ui.ProductButtonConfig;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class ProductButton extends Button {
	private static final Logger log = LogManager.getLogger();
	
	private final ProductButtonObserver observer;
	private final ObjectProperty<ProductButtonConfig> configProperty;
	
	public ProductButton(final ProductButtonObserver observer, final ProductButtonConfig config) {
		assert(null != observer);
		assert(null != config);
		
		this.observer = observer;

	    UiUtil.loadRootConstructNode(this,"ProductButton.fxml");
	    
		configProperty = new SimpleObjectProperty<ProductButtonConfig>(config);
		setConfig(config);
		
	    GridPane.setColumnIndex(this,config.getColumnIndex());
	    GridPane.setRowIndex(this,config.getRowIndex());
	    
	}
	
	public ObjectProperty<ProductButtonConfig> configProperty() {
		assert(null != configProperty);
		return configProperty;
	}
	
	public ProductButtonConfig getConfig() {
		log.debug("getConfig()");
		return configProperty().getValue();
	}
	
	public void setConfig(final ProductButtonConfig buttonConfig) {
		assert(null != buttonConfig);
		configProperty.set(buttonConfig);
		final String name = buttonConfig.getProduct().getName();
		setText(name);
	}
	
	@FXML
	private void initialize() {
		initializeControls();
		initializeBindings();
	}
	
	private void initializeControls() {
	}
	
	private void initializeBindings() {
	}
	
	@FXML
	private void handleButton(ActionEvent event) {
		final Product product = new Product(getConfig().getProduct());
		log.debug("Product Selected: {}",product);
		observer.productSelected(product);
	}

	@FXML
	private void handleUpdate() {
		log.debug("handleUpdate()");
		observer.updateProductButton(this);
	}
	
	@FXML
	private void handleDelete() {
		log.debug("handleDelete()");
		observer.deleteProductButton(this);
	}
}
