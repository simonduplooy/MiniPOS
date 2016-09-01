package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class XProductButtonBlank extends Button {
	private static final Logger log = LogManager.getLogger();
	
	private final ProductButtonObserver observer;
	private final Integer columnIdx;
	private final Integer rowIdx;
	
	public XProductButtonBlank(final ProductButtonObserver observer, final Integer columnIdx, final Integer rowIdx) {
		assert(null != observer);
		assert(null != columnIdx);
		assert(null != rowIdx);
		
		this.observer = observer;
		this.columnIdx = columnIdx;
		this.rowIdx = rowIdx;
		
		UiUtil.loadRootConstructNode(this,"ProductButtonBlank.fxml");

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
