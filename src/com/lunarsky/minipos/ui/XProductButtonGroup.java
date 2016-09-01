package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.dto.ProductGroupButtonConfigDTO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class XProductButtonGroup extends Button {
	private static final Logger log = LogManager.getLogger();
	
	private final ProductButtonObserver observer;
	private ProductGroupButtonConfigDTO config;
	
	public XProductButtonGroup(final ProductButtonObserver observer, final ProductGroupButtonConfigDTO config) {
		assert(null != observer);
		assert(null != config);
		
		this.observer = observer;
		this.config = config;
		
		UiUtil.loadRootConstructNode(this,"ProductButton.fxml");
	    
	    GridPane.setColumnIndex(this,config.getColumnIndex());
	    GridPane.setRowIndex(this,config.getRowIndex());
	    
	}
	
	public ProductGroupButtonConfigDTO getConfig() {
		assert(null != config);
		return config;
	}
	
	public void setConfig(final ProductGroupButtonConfigDTO config) {
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