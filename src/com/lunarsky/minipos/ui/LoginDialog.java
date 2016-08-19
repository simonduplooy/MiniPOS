package com.lunarsky.minipos.ui;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.User;
import com.lunarsky.minipos.ui.validator.IntegerTextFieldValidator;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginDialog extends VBox {
	private static final Logger log = LogManager.getLogger();

	private static final String WINDOW_TITLE = "Login";
	
	@FXML
	private Label errorLabel;
	@FXML
	private PasswordField passwordField;
	@FXML
	private GridPane keypadGridPane;
	
	//TODO is there a way to get the stage without holding a reference?
	private final AppData appData;
	private final Stage stage;
	private final IntegerTextFieldValidator passwordValidator;
	private User user;
	
	public LoginDialog(final AppData appData, final Stage parentStage, final Stage dialogStage) {
		assert(null != appData);
		assert(null != parentStage);
		assert(null != dialogStage);
		
		this.appData = appData;
		this.stage = dialogStage;
		
		dialogStage.setTitle(WINDOW_TITLE); 
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(parentStage);
		dialogStage.setResizable(false);
		
		dialogStage.setOnCloseRequest((event) -> close());
		
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LoginDialog.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
        	loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }

        errorLabel.setVisible(false);
        
        passwordField.textProperty().addListener((observable,oldValue,newValue) -> handlePasswordChanged(newValue));
        
        //TODO Fix magic numbers
        passwordValidator = new IntegerTextFieldValidator(passwordField,Const.PASSWORD_LENGTH,Const.PASSWORD_LENGTH);
        passwordValidator.validProperty().addListener((observable,oldValue,newValue) -> handlePasswordComplete(newValue));
                
        keypadGridPane.add(new KeypadButton(passwordField,"7",KeyCode.DIGIT7),0,0);
        keypadGridPane.add(new KeypadButton(passwordField,"8",KeyCode.DIGIT8),1,0);
        keypadGridPane.add(new KeypadButton(passwordField,"9",KeyCode.DIGIT9),2,0);
        keypadGridPane.add(new KeypadButton(passwordField,"4",KeyCode.DIGIT4),0,1);
        keypadGridPane.add(new KeypadButton(passwordField,"5",KeyCode.DIGIT5),1,1);
        keypadGridPane.add(new KeypadButton(passwordField,"6",KeyCode.DIGIT6),2,1);
        keypadGridPane.add(new KeypadButton(passwordField,"1",KeyCode.DIGIT1),0,2);
        keypadGridPane.add(new KeypadButton(passwordField,"2",KeyCode.DIGIT2),1,2);
        keypadGridPane.add(new KeypadButton(passwordField,"3",KeyCode.DIGIT3),2,2);
        final KeypadButton button = new KeypadButton(passwordField,"0",KeyCode.DIGIT0);
        GridPane.setColumnSpan(button,2);
        keypadGridPane.add(button,0,3);
        keypadGridPane.add(new KeypadButton(passwordField,"<",KeyCode.BACK_SPACE),2,3);
	}

	private void handlePasswordChanged(final String text) {
		if(text.length() > 0) {
			errorLabel.setVisible(false);			
		}
	}
	
	private void handlePasswordComplete(final Boolean isValid) {

		if(isValid) {

			final String password = passwordField.getText();
			
			Task<User> task = new Task<User>() {
				@Override
				protected User call() {
					final User user = appData.getServerConnector().getUserWithPassword(password);
					return user;
				}
				
				@Override
				protected void succeeded() {
					final User user = getValue();
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
	
	public User getUser() {
		return user;
	}

	private void setUser(final User user) {
		assert(null != user);
		this.user = user;
	}
	
	private void close() {
		log.debug("Closing Dialog");
		stage.close();
		//TODO unbind
	}
	
	private class KeypadButton extends Button {
		private final Node targetNode;
		private final KeyCode keyCode;

		public KeypadButton(final Node targetNode, final String text, KeyCode keyCode) {
			super(text);
			this.targetNode = targetNode;
			this.keyCode = keyCode;
			
    		getStyleClass().add("virtual-keyboard-button");
    		setFocusTraversable(false);
    		setMaxWidth(Double.POSITIVE_INFINITY);
    		
    		GridPane.setFillHeight(this,true);
    		GridPane.setFillWidth(this,true);
    		
    		setOnAction(this::handleKeyPressed);
		}
		
		private void handleKeyPressed(ActionEvent event) {
			String character = getText();
			if(!character.matches("^[0-9]$")) {
				character = KeyEvent.CHAR_UNDEFINED;
			}
			final KeyEvent keyPressEvent = createKeyEvent(this, targetNode, KeyEvent.KEY_PRESSED, character, keyCode);
			targetNode.fireEvent(keyPressEvent);
			final KeyEvent keyReleasedEvent = createKeyEvent(this, targetNode, KeyEvent.KEY_RELEASED, character, keyCode);
			targetNode.fireEvent(keyReleasedEvent);
			if (character != KeyEvent.CHAR_UNDEFINED) {
				final KeyEvent keyTypedEvent = createKeyEvent(this, targetNode, KeyEvent.KEY_TYPED, character, keyCode);
				targetNode.fireEvent(keyTypedEvent);
			}
		}
		
	  // Utility method to create a KeyEvent from the Modifiers
	  private KeyEvent createKeyEvent(Object source, EventTarget target,
	      EventType<KeyEvent> eventType, String character, KeyCode code) {
	    return new KeyEvent(source, target, eventType, character, code.toString(), code, false, false, false, false);
	  
		}
	}
	
}
