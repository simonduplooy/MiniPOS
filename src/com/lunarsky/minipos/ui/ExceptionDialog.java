package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ExceptionDialog {
	private static final Logger log = LogManager.getLogger();
	
	private static final String TITLE_TEXT = "";
	private static final String DETAIL_LABEL_TEXT = "Detail:";

	//Prevent instantiation
	private ExceptionDialog() {
	}
	
	public static Alert create(AlertType alertType, String message, Throwable throwable) {
		
		Alert alert = new Alert(alertType);
		alert.setTitle(TITLE_TEXT);
		alert.setHeaderText(null);
		alert.setContentText(message);
		
		// Create expandable Exception.
		/*
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		String exceptionText = sw.toString();
		*/

		//Get the root cause
		//TODO is there an easier way?
		String exceptionText;
		do {
			exceptionText = (throwable.getClass()+" "+throwable.getMessage());
			throwable = throwable.getCause();
		} while (null != throwable);
		
		Label label = new Label(DETAIL_LABEL_TEXT);

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);
		
		return alert;
	}
}
