package com.lunarsky.minipos.ui;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.model.Account;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.User;
import com.lunarsky.minipos.ui.validator.StringTextFieldValidator;
import com.lunarsky.minipos.ui.virtualkeyboards.VirtualKeyboard;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AccountUpdateDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String WINDOW_TITLE_NEW_ACCOUNT = "New Account";
	private static final String WINDOW_TITLE_UPDATE_ACCOUNT = "Update Account";
	
	private final AppData appData;
	private final Stage stage;
	private Account account;
	private StringTextFieldValidator nameValidator; 
	
	private final Service<Account> saveService; 
	
	@FXML
	private Label nameErrorLabel;
	@FXML
	private TextField nameTextField;
	@FXML
	private Button saveButton;
	@FXML
	private Button cancelButton;

	
	// account can be null to create a new Account
	public AccountUpdateDialog(final AppData appData, final Stage parentStage, final Account account) {
		assert(null != appData);
		assert(null != parentStage);
			
		this.appData = appData;
		this.account = account;
		
		saveService = new Service<Account>() {
			@Override
			protected Task<Account> createTask() {
				return new Task<Account>() {
					@Override
					protected Account call() {
						Account account = getAccountFromControls();
						final User user = appData.getActiveUser();
						account = appData.getServerConnector().createAccount(user.getId(),account); 
						return account;
					}
					@Override
					protected void succeeded() {
						final Account account = getValue();
						log.debug("CreateAccount Succeeded: [{}]",account);
						setAccount(account);
						close();				
					}
					@Override
					protected void failed() {
						final Throwable t = getException();
						log.catching(t);
						//TODO
						//EntityNotFoundException,NameInUseException
						throw new RuntimeException(t);
					}

				};
			}
		};
		
		stage = new Stage();
		stage.initOwner(parentStage);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setTitle((null == account) ? WINDOW_TITLE_NEW_ACCOUNT : WINDOW_TITLE_UPDATE_ACCOUNT); 
				
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AccountUpdateDialog.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
        	loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
		
		Scene scene = new Scene(this);
		appData.getViewManager().setStyleSheets(scene);
		stage.setScene(scene);

	}
		
	@FXML
	public void initialize() {

		nameTextField.requestFocus();
		nameValidator = new StringTextFieldValidator(nameTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_TEXTFIELD_LENGTH);
		nameErrorLabel.setVisible(false);
		
		saveButton.disableProperty().bind(nameValidator.validProperty().not().or(saveService.runningProperty()));
		cancelButton.disableProperty().bind(saveService.runningProperty());

		final VirtualKeyboard keyboard = new VirtualKeyboard(true);
		setBottom(keyboard);
	}
	
	public Stage getStage() {
		assert(null != stage);
		return stage;
	}
	
	private void setAccount(final Account account) {
		this.account = account;
	}
	
	public Account getAccount() {
		//can be null if cancelled
		return account;
	}
	
	private void close() {
		stage.close();
		saveService.cancel();
	}
	
	private Account getAccountFromControls() {
		final String name = nameTextField.getText();
		final User user = appData.getActiveUser();
		Account account = new Account();
		account.setName(name);
		return account;
	}
	
	@FXML
	public void handleSave(ActionEvent event) {
		saveService.restart();
	}

	@FXML
	public void handleCancel(ActionEvent event) {
		close();
	}
}