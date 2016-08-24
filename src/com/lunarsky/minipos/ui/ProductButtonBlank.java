package com.lunarsky.minipos.ui;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.dto.ProductDTO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.layout.GridPane;

public class ProductButtonBlank extends Button {
	private static final Logger log = LogManager.getLogger();
	
	private final ProductButtonObserver observer;
	private final Integer columnIdx;
	private final Integer rowIdx;
	
	public ProductButtonBlank(final ProductButtonObserver observer, final Integer columnIdx, final Integer rowIdx) {
		assert(null != observer);
		assert(null != columnIdx);
		assert(null != rowIdx);
		
		this.observer = observer;
		this.columnIdx = columnIdx;
		this.rowIdx = rowIdx;
		
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(getClass().getResource("ProductButtonBlank.fxml"));
	    loader.setRoot(this);
	    loader.setController(this);
	    try {
	    	loader.load();
	    } catch (IOException e) {
	    	throw new RuntimeException(e);
	    }
	}
	
	@FXML
	private void initialize() {
	    GridPane.setColumnIndex(this,columnIdx);
	    GridPane.setRowIndex(this,rowIdx);
	}
	
	@FXML
	private void handleButton(ActionEvent event) {
	}

	@FXML
	private void handleAddProduct() {
		log.debug("handleAddProduct()");
		observer.createProductButton(columnIdx,rowIdx);
	}
	
	@FXML
	private void handleAddGroup() {
		log.debug("handleAddGroup()");
		observer.createProductButtonGroup(columnIdx,rowIdx);
	}
}
