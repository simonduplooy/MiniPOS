package com.lunarsky.minipos.ui;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.common.exception.NameInUseException;
import com.lunarsky.minipos.common.exception.PasswordInUseException;
import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.Role;
import com.lunarsky.minipos.model.User;
import com.lunarsky.minipos.ui.validator.IntegerTextFieldValidator;
import com.lunarsky.minipos.ui.validator.StringTextFieldValidator;
import com.lunarsky.minipos.ui.validator.TextFieldValidator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class UserUpdateDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private static String WINDOW_TITLE_ADD_USER = "Add User";
	private static String WINDOW_TITLE_UPDATE_USER = "Update User";

	private final AppData appData;
	private final Stage stage;
	private User user;
	
	private ObservableList<Role> roleList;
	
	@FXML
	private Label nameErrorLabel;
	@FXML
	private TextField nameTextField;
	@FXML
	private Label passwordErrorLabel;
	@FXML
	private PasswordField passwordPasswordField;
	@FXML
	private ComboBox<Role> roleComboBox;
	@FXML
	private Button saveButton;
	
	private TextFieldValidator nameValidator;
	private TextFieldValidator passwordValidator;
		
	// user can be null to create a new User
	public UserUpdateDialog(final AppData appData, final Stage parentStage, final User user) {
		assert(null != appData);
		assert(null != parentStage);
		
		this.appData = appData;
		this.user = user;

		stage = new Stage();
		stage.initOwner(parentStage);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setTitle((null == user) ? WINDOW_TITLE_ADD_USER : WINDOW_TITLE_UPDATE_USER); 
				
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("UserUpdateDialog.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
        	loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
		
		Scene scene = new Scene(this);
		scene.getStylesheets().add(getClass().getResource("/resources/stylesheets/application.css").toExternalForm());
		stage.setScene(scene);

	}
	
	public void showAndWait() {
		stage.showAndWait();
	}
	
	//Can be null if canceled
	public User getUser() {
		return user;
	}
	
	@FXML
	private void initialize() {
		initializeControls(getUser());
		initializeAsync();
		
	}
	
	private void initializeControls(final User user) {
		
		roleComboBox.setCellFactory((listCell) -> {
			ListCell<Role> cell = new ListCell<Role>() {
				@Override
				protected void updateItem(Role role, boolean empty) {
					super.updateItem(role,empty);
					if(null!=role) {
						setText(role.getName());
					} else {
						setText("");
					}
				}
			};
			return cell;
		});
		
		final StringConverter<Role> converter = new StringConverter<Role>() {
			@Override
			public String toString(final Role role) {
				return role.getName();
			}
			@Override
			public Role fromString(final String roleName) {
				return null;
			}
		};
		
		roleComboBox.setConverter(converter);
		
		if(null == user) {
			nameTextField.setText("");
			passwordPasswordField.setText("");
			roleComboBox.getSelectionModel().clearSelection();			
		} else {
			nameTextField.setText(user.getName());
			passwordPasswordField.setText(user.getPassword());
			//Role is initialized Async
		}
		
		clearErrorMessages();
		
		nameValidator = new StringTextFieldValidator(nameTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_TEXTFIELD_LENGTH);
		passwordValidator = new IntegerTextFieldValidator(passwordPasswordField,Const.PASSWORD_LENGTH,Const.PASSWORD_LENGTH);

		saveButton.disableProperty().bind(nameValidator.validProperty().and(passwordValidator.validProperty()).not());
	}

	private void initializeAsync() {
		
		Task<List<Role>> task = new Task<List<Role>>() {
			@Override
			protected List<Role> call() {
				List<Role> roles = appData.getServerConnector().getRoles();
				return roles;
			}
			@Override 
			protected void succeeded() {
				log.debug("GetRoles() Succeeded");
				roleList = FXCollections.observableList(getValue());
				Collections.sort(roleList);
				roleComboBox.setItems(roleList);
				
				final User user = getUser();
				if(null == user) {
					//TODO handle no Roles defined
					//TODO select default / last used?
					roleComboBox.getSelectionModel().selectFirst();					
				} else {
					final Role role = user.getRole();
					//This uses Role.compareTo()
					roleComboBox.getSelectionModel().select(role);
				}
			}
			@Override
			protected void failed() {
				log.error("GetRoles() failed");
				final Throwable t = getException();
				log.catching(Level.ERROR, t);
				ExceptionDialog.create(AlertType.ERROR, "Could not retrieve Roles", t).show();
			}
		};
		
		Thread thread = new Thread(task);
		log.debug("Starting Task GetRoles() {}",thread);
		thread.start();
	}
	
	@FXML
	private void handleSave(ActionEvent event) {

		clearErrorMessages();
		
		Task<User> task = new Task<User>() {
			final User user = createUserFromControls();
			@Override
			protected User call() throws NameInUseException, PasswordInUseException, EntityNotFoundException {
				return appData.getServerConnector().saveUser(user);
			}
			@Override
			protected void succeeded() {
				setUser(getValue());
				log.debug("SaveUser() Succeeded");
				getStage().close();
			}
			@Override
			protected void failed() {
				final Throwable t = getException();
				log.debug("SaveUser() Failed");
				
				if(t instanceof NameInUseException) {
					nameErrorLabel.setVisible(true);
				} else if(t instanceof PasswordInUseException) {
					passwordErrorLabel.setVisible(true);
				} else {
					log.catching(Level.ERROR,t);
					throw new RuntimeException(t);
				}
			}
		};
		
		Thread thread = new Thread(task);
		log.debug("Creating SaveUser() Task {}",thread);
		thread.start();
	}

	@FXML
	private void handleCancel(ActionEvent event) {
		//TODO cancel save task ?
		getStage().close();
	}
	
	private void clearErrorMessages() {
		nameErrorLabel.setVisible(false);
		passwordErrorLabel.setVisible(false);		
	}
	
	private User createUserFromControls() {
				
		final PersistenceId id = (null == user)? null : user.getId();
		final String name = nameTextField.getText();
		final String password = passwordPasswordField.getText();
		final Role role = roleComboBox.getValue();
		
		final User updatedUser = new User(id,name,password,role);
		return updatedUser;
	}
	
	private void setUser(final User user) {
		this.user = user;
	}
	
	private Stage getStage() {
		assert(stage!=null);
		return stage;
	}
	
}
