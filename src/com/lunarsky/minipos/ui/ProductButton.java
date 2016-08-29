package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.dto.ProductButtonConfigDTO;
import com.lunarsky.minipos.model.ui.Product;
import com.lunarsky.minipos.model.ui.ProductButtonConfig;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class ProductButton extends Button {
	private static final Logger log = LogManager.getLogger();
	
	private final ProductButtonObserver observer;
	private final ProductButtonConfig config;
	
	public ProductButton(final ProductButtonObserver observer, final ProductButtonConfig config) {
		assert(null != observer);
		assert(null != config);
		
		this.observer = observer;
		this.config = config;
		
	    UiUtil.loadRootConstructNode(this,"ProductButton.fxml");
	    
	    GridPane.setColumnIndex(this,config.getColumnIndex());
	    GridPane.setRowIndex(this,config.getRowIndex());
	    
	}
	
	public ProductButtonConfig getConfig() {
		log.debug("getConfig()");
		assert(null != config);
		return config;
	}
	
	
	@FXML
	private void initialize() {
		initializeControls();
	}
	
	private void initializeControls() {
		final String name = getConfig().getProduct().getName();
		setText(name);
	}
	
	@FXML
	private void handleButton(ActionEvent event) {
		final Product product = new Product(config.getProduct());
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
