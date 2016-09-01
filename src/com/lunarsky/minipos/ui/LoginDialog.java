package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.UserDTO;
import com.lunarsky.minipos.ui.validator.IntegerTextFieldValidator;
import com.lunarsky.minipos.ui.virtualkeyboards.NumericVirtualKeyboard;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginDialog extends VBox {
	private static final Logger log = LogManager.getLogger();

	private static final String WINDOW_TITLE = "Login";
	
	@FXML
	private Label errorLabel;
	@FXML
	private PasswordField passwordField;
	
	//TODO is there a way to get the stage without holding a reference?
	private final AppData appData;
	private final IntegerTextFieldValidator passwordValidator;
	private UserDTO user;
	
	public LoginDialog(final Stage parentStage) {
		assert(null != parentStage);
		
		this.appData = AppData.getInstance();
		
		UiUtil.createDialog(parentStage,WINDOW_TITLE,this,"LoginDialog.fxml"); 
		
		getStage().setOnCloseRequest((event) -> close());
		
        errorLabel.setVisible(false);
        
        passwordField.textProperty().addListener((observable,oldValue,newValue) -> handlePasswordChanged(newValue));
        
        passwordValidator = new IntegerTextFieldValidator(passwordField,Const.PASSWORD_LENGTH,Const.PASSWORD_LENGTH);
        passwordValidator.validProperty().addListener((observable,oldValue,newValue) -> handlePasswordComplete(newValue));
                
        final NumericVirtualKeyboard keyboard = new NumericVirtualKeyboard(getScene());
        getChildren().add(keyboard);
	}

	public Stage getStage() {
		return (Stage)getScene().getWindow();
	}
	
	private void handlePasswordChanged(final String text) {
		if(text.length() > 0) {
			errorLabel.setVisible(false);			
		}
	}
	
	private void handlePasswordComplete(final Boolean isValid) {

		if(isValid) {

			final String password = passwordField.getText();
			
			Task<UserDTO> task = new Task<UserDTO>() {
				@Override
				protected UserDTO call() {
					final UserDTO user = appData.getServerConnector().getUserWithPassword(password);
					return user;
				}
				
				@Override
				protected void succeeded() {
					final UserDTO user = getValue();
					setUser(user);
					close();
				}
				
				@Override
				protected void failed() {
					Throwable throwable = getException();
					if(throwable instanceof EntityNotFoundException) {
						errorLabel.setVisible(true);
						passwordField.clear();
					} else {
						//TODO is this the correct way to handle?
						log.catching(throwable);
						throw(new RuntimeException(throwable));
					}
				}
			};
			
			Thread thread = new Thread(task);
			thread.start();
		}
	}
	
	public UserDTO getUser() {
		return user;
	}

	private void setUser(final UserDTO user) {
		assert(null != user);
		this.user = user;
	}
	
	private void close() {
		log.debug("Closing Dialog");
		getStage().close();
		//TODO unbind
	}
	
}
