package com.lunarsky.minipos.ui;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.Account;
import com.lunarsky.minipos.model.AppData;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class AccountView extends BorderPane {
	private static final Logger log = LogManager.getLogger();

	private final AppData appData;
	private final Account account;

	@FXML
	private Label accountLabel;
	
	public AccountView(final AppData appData, final Account account) {
		assert(null != appData);
		assert(null != account);
		
		this.appData = appData;
		this.account = account;
		
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AccountView.fxml"));
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
		initializeControls();
	}
	
	private void initializeControls() {
		accountLabel.setText(account.getName());
	}
	
	@FXML
	private void handlePay(final ActionEvent event) {
		log.debug("handlePay()");
	}
	
	@FXML
	private void handleBack(final ActionEvent event) {
		log.debug("handleBack()");
		close();
	}
	
	private void close() {
		appData.getViewManager().closeAccountView();
	}
}
