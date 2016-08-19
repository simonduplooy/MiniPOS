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
	private final String name;
	
	public ProductButtonGroup(final ProductButtonObserver observer, final ProductButtonGroupConfig config) {
		assert(null != observer);
		assert(null != config);
		
		this.observer = observer;
		this.name = config.getName();
		
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(getClass().getResource("ProductButton.fxml"));
	    loader.setRoot(this);
	    loader.setController(this);
	    try {
	    	loader.load();
	    } catch (IOException e) {
	    	throw new RuntimeException(e);
	    }
	    
	    GridPane.setColumnIndex(this,config.getColumn());
	    GridPane.setRowIndex(this,config.getRow());
	    
	}
	
	@FXML
	private void initialize() {
		setText(name);
	}
	
	@FXML
	private void handleButton(ActionEvent event) {
		log.debug("Product Group Selected: {}",name);
	}

	@FXML
	private void handleDelete() {
		log.debug("handleDelete()");
		observer.deleteProductButtonGroup(this);
	}
}
