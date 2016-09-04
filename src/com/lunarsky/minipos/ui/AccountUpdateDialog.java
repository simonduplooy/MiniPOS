package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.AccountDTO;
import com.lunarsky.minipos.model.dto.UserDTO;
import com.lunarsky.minipos.model.ui.Account;
import com.lunarsky.minipos.model.ui.User;
import com.lunarsky.minipos.ui.validator.StringTextFieldValidator;
import com.lunarsky.minipos.ui.virtualkeyboards.VirtualKeyboard;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AccountUpdateDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String WINDOW_TITLE_NEW_ACCOUNT = "New Account";
	private static final String WINDOW_TITLE_UPDATE_ACCOUNT = "Update Account";
	
	private final AppData appData;
	private Account account;
	private StringTextFieldValidator nameValidator; 
	
	private Service<AccountDTO> saveService; 
	
	@FXML
	private Label nameErrorLabel;
	@FXML
	private TextField nameTextField;
	@FXML
	private Button saveButton;
	@FXML
	private Button cancelButton;

	
	// account can be null to create a new Account
	public AccountUpdateDialog(final Stage parentStage, final Account account) {
		assert(null != parentStage);
			
		this.appData = AppData.getInstance();
		this.account = account;
		
		final String title = (null == account)?WINDOW_TITLE_NEW_ACCOUNT:WINDOW_TITLE_UPDATE_ACCOUNT;
		UiUtil.createDialog(parentStage,title,this,"AccountUpdateDialog.fxml");
	}
		
	@FXML
	public void initialize() {

		initializeService();
		initializeControls();

	}
	
	private void initializeService() {
		
		saveService = new Service<AccountDTO>() {
			@Override
			protected Task<AccountDTO> createTask() {
				return new Task<AccountDTO>() {
					@Override
					protected AccountDTO call() {
						AccountDTO account = getAccountFromControls().getDTO();
						final UserDTO user = appData.getActiveUser().getDTO();
						account = appData.getServerConnector().createAccount(user.getId(),account); 
						return account;
					}
					@Override
					protected void succeeded() {
						final AccountDTO accountDTO = getValue();
						log.debug("CreateAccount Succeeded: [{}]",account);
						setAccount(new Account(accountDTO));
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
	}
	
	private void initializeControls() {
		nameTextField.requestFocus();
		nameValidator = new StringTextFieldValidator(nameTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_TEXTFIELD_LENGTH);
		nameErrorLabel.setVisible(false);
		
		saveButton.disableProperty().bind(nameValidator.validProperty().not().or(saveService.runningProperty()));
		cancelButton.disableProperty().bind(saveService.runningProperty());

		final VirtualKeyboard keyboard = new VirtualKeyboard(getScene());
		setBottom(keyboard);
	}
	
	public Stage getStage() {
		return (Stage)getScene().getWindow();
	}
	
	private void setAccount(final Account account) {
		this.account = account;
	}
	
	public Account getAccount() {
		//can be null if cancelled
		return account;
	}
	
	private void close() {
		getStage().close();
		saveService.cancel();
	}
	
	private Account getAccountFromControls() {
		final String name = nameTextField.getText();
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
