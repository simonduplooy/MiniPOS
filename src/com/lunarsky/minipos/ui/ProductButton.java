package com.lunarsky.minipos.ui;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.Product;
import com.lunarsky.minipos.model.ProductButtonConfig;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class ProductButton extends Button {
	private static final Logger log = LogManager.getLogger();
	
	private final ProductButtonObserver observer;
	private final Product product;
	
	public ProductButton(final ProductButtonObserver observer, final ProductButtonConfig config) {
		assert(null != observer);
		assert(null != config);
		
		this.observer = observer;
		this.product = config.getProduct();
		
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
		setText(product.getName());
	}
	
	@FXML
	private void handleButton(ActionEvent event) {
		log.debug("Product Selected: {}",product);
		observer.productSelected(product);
	}

	@FXML
	private void handleDelete() {
		log.debug("handleDelete()");
		observer.deleteProductButton(this);
	}
}
