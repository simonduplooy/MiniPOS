package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PayDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String WINDOW_TITLE = "Payment";
	
	public PayDialog(final Stage parentStage) {
		UiUtil.createDialog(parentStage,WINDOW_TITLE,this,"PayDialog.fxml"); 
	}
	
	public Stage getStage() {
		return (Stage)getScene().getWindow();
	}
	
}
