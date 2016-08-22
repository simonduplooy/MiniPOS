package com.lunarsky.minipos.ui;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.ProductButtonGroupConfig;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class ProductButtonGroup extends Button {
	private static final Logger log = LogManager.getLogger();
	
	private final ProductButtonObserver observer;
	private ProductButtonGroupConfig config;
	
	public ProductButtonGroup(final ProductButtonObserver observer, final ProductButtonGroupConfig config) {
		assert(null != observer);
		assert(null != config);
		
		this.observer = observer;
		this.config = config;
		
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(getClass().getResource("ProductButton.fxml"));
	    loader.setRoot(this);
	    loader.setController(this);
	    try {
	    	loader.load();
	    } catch (IOException e) {
	    	throw new RuntimeException(e);
	    }
	    
	    GridPane.setColumnIndex(this,config.getColumnIndex());
	    GridPane.setRowIndex(this,config.getRowIndex());
	    
	}
	
	public ProductButtonGroupConfig getConfig() {
		assert(null != config);
		return config;
	}
	
	public void setConfig(final ProductButtonGroupConfig config) {
		assert(null != config);
		this.config = config;
		setText(config.getName());
	}
	
	@FXML
	private void initialize() {
		setConfig(config);
	}
	
	@FXML
	private void handleButton(ActionEvent event) {
		log.debug("handleButton()");
		observer.productButtonGroupSelected(getConfig());
	}

	@FXML
	private void handleUpdate() {
		log.debug("handleUpdate()");
		observer.updateProductButtonGroup(this);
	}
	
	@FXML
	private void handleDelete() {
		log.debug("handleDelete()");
		observer.deleteProductButtonGroup(this);
	}
}
