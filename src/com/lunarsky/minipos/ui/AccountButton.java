package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.AccountDTO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

//TODO MAKE INNERCLASS
public class AccountButton extends Button {
	private static final Logger log = LogManager.getLogger();
	

	private final AccountDTO account;
	
	public AccountButton(final AccountDTO account) {
		assert(null != account);
		this.account = account;
		
		UiUtil.loadRootConstructNode(this,"AccountButton.fxml");
		setText(account.getName());
	}
	
	@FXML
	private void handleButton(ActionEvent event) {
		log.debug(String.format("Account Selected: %s",account));
		AppData.getInstance().getViewManager().accountSelected(account);
	}

}
