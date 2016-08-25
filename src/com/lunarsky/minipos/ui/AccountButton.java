package com.lunarsky.minipos.ui;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.AccountDTO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class AccountButton extends Button {
	private static final Logger log = LogManager.getLogger();
	

	private final AccountDTO account;
	
	public AccountButton(final AccountDTO account) {
		assert(null != account);
		
		this.account = account;
		
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(getClass().getResource("AccountButton.fxml"));
	    loader.setRoot(this);
	    loader.setController(this);
	    try {
	    	loader.load();
	    } catch (IOException e) {
	    	throw new RuntimeException(e);
	    }
		
		setText(account.getName());
	}
	
	@FXML
	private void handleButton(ActionEvent event) {
		log.debug(String.format("Account Selected: %s",account));
		AppData.getInstance().getViewManager().accountSelected(account);
	}

}
