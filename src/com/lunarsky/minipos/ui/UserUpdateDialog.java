package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.common.exception.NameInUseException;
import com.lunarsky.minipos.common.exception.PasswordInUseException;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.UserDTO;
import com.lunarsky.minipos.model.ui.User;
import com.lunarsky.minipos.ui.validator.IntegerTextFieldValidator;
import com.lunarsky.minipos.ui.validator.StringTextFieldValidator;
import com.lunarsky.minipos.ui.validator.TextFieldValidator;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class UserUpdateDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private static String WINDOW_TITLE_ADD_USER = "Add User";
	private static String WINDOW_TITLE_UPDATE_USER = "Update User";
	private static String ERROR_TEXT_NAME_IN_USE = "User already exists";
	private static String ERROR_TEXT_PASSWORD_IN_USE = "Password already taken";
	
	private final AppData appData;
	private final User user;
	private TextFieldValidator nameValidator;
	private TextFieldValidator passwordValidator;
	private boolean wasSaved; 
	
	@FXML
	private TextField nameTextField;
	@FXML
	private PasswordField passwordPasswordField;
	@FXML
	private Button saveButton;
	
	
	/**************************************************************************
	 * Constructor
	 **************************************************************************/
	// user can be null to create a new User
	public UserUpdateDialog(final Stage parentStage, final User user) {
		assert(null != parentStage);
		
		this.appData = AppData.getInstance();
		//Create a copy to not modify the original if cancelled
		this.user = (null != user) ? new User(user) : new User("","");

		final String title = (null == user) ? WINDOW_TITLE_ADD_USER : WINDOW_TITLE_UPDATE_USER;
		UiUtil.createDialog(parentStage,title,this,"UserUpdateDialog.fxml");

	}
	
	/**************************************************************************
	 * Initialize
	 **************************************************************************/
	@FXML
	private void initialize() {
		initializeMembers();
		initializeControls();
		initializeBindings();
		initializeAsync();
		
	}
	
	private void initializeMembers() {
		nameValidator = new StringTextFieldValidator(nameTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_TEXTFIELD_LENGTH);
		passwordValidator = new IntegerTextFieldValidator(passwordPasswordField,Const.PASSWORD_LENGTH,Const.PASSWORD_LENGTH);
	}
	
	private void initializeControls() {
				
	}

	private void initializeBindings() {
		final User user = getUser();
		user.nameProperty().bindBidirectional(nameTextField.textProperty());
		user.passwordProperty().bindBidirectional(passwordPasswordField.textProperty());
		
		//TODO bind to service
		saveButton.disableProperty().bind(nameValidator.validProperty().and(passwordValidator.validProperty()).not());
	}
	
	private void initializeAsync() {

	}

	/**************************************************************************
	 * Getters & Setters
	 **************************************************************************/
	public Stage getStage() {
		return (Stage)getScene().getWindow();
	}
	
	public User getUser() {
		assert(null != user);
		return user;
	}
	
	public boolean wasSaved() {
		return wasSaved;
	}
		
	/**************************************************************************
	 * Event Handlers
	 **************************************************************************/
	@FXML
	private void handleSave(ActionEvent event) {
		
		Task<UserDTO> task = new Task<UserDTO>() {
			final UserDTO user = getUser().createDTO();
			@Override
			protected UserDTO call() {
				return appData.getServerConnector().saveUser(user);
			}
			@Override
			protected void succeeded() {
				log.debug("SaveUser() Succeeded");
				final UserDTO dto = getValue();
				getUser().set(dto);
				wasSaved = true;
				close();
			}
			@Override
			protected void failed() {
				final Throwable throwable = getException();
				log.debug("SaveUser() Failed");
				
				if(throwable instanceof NameInUseException) {
					log.debug("NameInUseException");
					final Alert alert = new ExceptionAlert(AlertType.ERROR,ERROR_TEXT_NAME_IN_USE,throwable);
					alert.showAndWait();
				} else if(throwable instanceof PasswordInUseException) {
					log.debug("NameInUseException");
					final Alert alert = new ExceptionAlert(AlertType.ERROR,ERROR_TEXT_PASSWORD_IN_USE,throwable);
					alert.showAndWait();
				} else {
					log.catching(Level.ERROR,throwable);
					throw new RuntimeException(throwable);
				}
			}
		};
		
		Thread thread = new Thread(task);
		log.debug("Creating SaveUser() Task {}",thread);
		thread.start();
	}

	@FXML
	private void handleCancel(ActionEvent event) {
		close();
	}
	
	/**************************************************************************
	 * Utilities
	 **************************************************************************/
	private void close() {
		getStage().close();
	}
	
}
