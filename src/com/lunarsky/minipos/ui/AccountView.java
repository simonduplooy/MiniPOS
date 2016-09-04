package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.ui.Account;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class AccountView extends BorderPane {
	private static final Logger log = LogManager.getLogger();

	private final AppData appData;
	private final Account account;

	@FXML
	private Label accountLabel;
	
	public AccountView(final Account account) {
		assert(null != account);
		
		this.appData = AppData.getInstance();
		this.account = account;
		
		UiUtil.loadRootConstructNode(this,"AccountView.fxml");
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
