package com.lunarsky.minipos.ui;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.AccountDTO;
import com.lunarsky.minipos.model.dto.UserDTO;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class AccountOverviewView extends BorderPane {
	private static final Logger log = LogManager.getLogger();

	private final AppData appData;
	private final Stage stage;
	private final Service<List<AccountDTO>> loadService;
	
	@FXML
	private Label userLabel;
	@FXML
	private FlowPane accountFlowPane;
	
	public AccountOverviewView(final Stage stage) {
		assert(null != stage);
		this.appData = AppData.getInstance();
		this.stage = stage;
		
		UiUtil.loadRootConstructNode(this,"AccountOverviewView.fxml");
		
		loadService = new Service<List<AccountDTO>>() {
			@Override
			protected Task<List<AccountDTO>> createTask() {
				return new Task<List<AccountDTO>>() {
					
					final PersistenceId userId = appData.getActiveUser().getId();
					
					@Override
					protected List<AccountDTO> call() {
						final List<AccountDTO> account = appData.getServerConnector().getAccounts(userId); 
						return account;
					}
					@Override
					protected void succeeded() {
						final List<AccountDTO> accounts = getValue();
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
	
	private void setAccounts(final List<AccountDTO> accounts) {
		for(AccountDTO account: accounts) {
			log.debug("Adding Account {}",account);
			final AccountButton button = new AccountButton(account);
			accountFlowPane.getChildren().add(button);
		}
	}
	
	@FXML
	private void initialize() {
		final UserDTO user = appData.getActiveUser();
		userLabel.setText(user.getName());
	}
	
	@FXML
	private void handleNew(ActionEvent event) {
		log.debug("handleNew()");
		
		final AccountUpdateDialog dialog = new AccountUpdateDialog(stage,null);
		dialog.getStage().showAndWait();
		
		final AccountDTO account = dialog.getAccount();
		if(null != account) {
			log.debug("Adding Account {}",account);
			AccountButton button = new AccountButton(account);
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
