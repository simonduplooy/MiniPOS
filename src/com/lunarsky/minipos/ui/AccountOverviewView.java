package com.lunarsky.minipos.ui;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.Account;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.User;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class AccountOverviewView extends BorderPane {
	private static final Logger log = LogManager.getLogger();

	private final AppData appData;
	private final Stage stage;
	private final Service<List<Account>> loadService;
	
	@FXML
	private Label userLabel;
	@FXML
	private FlowPane accountFlowPane;
	
	public AccountOverviewView(final AppData appData, final Stage stage) {
		assert(null != appData);
		assert(null != stage);
		this.appData = appData;
		this.stage = stage;
		
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AccountOverviewView.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
        	loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
        
		loadService = new Service<List<Account>>() {
			@Override
			protected Task<List<Account>> createTask() {
				return new Task<List<Account>>() {
					
					final PersistenceId userId = appData.getActiveUser().getId();
					
					@Override
					protected List<Account> call() {
						final List<Account> account = appData.getServerConnector().getAccounts(userId); 
						return account;
					}
					@Override
					protected void succeeded() {
						final List<Account> accounts = getValue();
						log.debug("GetAccounts Succeeded: {} accounts",accounts.size());
						setAccounts(accounts);
					}
					@Override
					protected void failed() {
						final Throwable t = getException();
						log.catching(t);
						throw new RuntimeException(t);
					}

				};
			}
		};
		
		loadService.start();
        
	}
	
	private void setAccounts(final List<Account> accounts) {
		for(Account account: accounts) {
			log.debug("Adding Account {}",account);
			final AccountButton button = new AccountButton(appData,account);
			accountFlowPane.getChildren().add(button);
		}
	}
	
	@FXML
	private void initialize() {
		final User user = appData.getActiveUser();
		userLabel.setText(user.getName());
	}
	
	@FXML
	private void handleNew(ActionEvent event) {
		log.debug("handleNew()");
		
		final AccountUpdateDialog dialog = new AccountUpdateDialog(appData,stage,null);
		dialog.getStage().showAndWait();
		
		final Account account = dialog.getAccount();
		if(null != account) {
			log.debug("Adding Account {}",account);
			AccountButton button = new AccountButton(appData,account);
			accountFlowPane.getChildren().add(button);
		}
	}

	@FXML
	private void handleAdd(ActionEvent event) {
		log.debug("handleAdd()");
	}

	@FXML
	private void handleBack(ActionEvent event) {
		log.debug("handleBack()");
		appData.getViewManager().closeAccountOverviewView();
	}
		
}
