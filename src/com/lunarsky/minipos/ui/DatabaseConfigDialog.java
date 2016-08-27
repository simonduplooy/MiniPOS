package com.lunarsky.minipos.ui;

import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.PersistenceConfigDTO;
import com.lunarsky.minipos.ui.validator.IntegerTextFieldValidator;
import com.lunarsky.minipos.ui.validator.StringTextFieldValidator;
import com.lunarsky.minipos.ui.virtualkeyboards.VirtualKeyboard;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DatabaseConfigDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String WINDOW_TITLE = "Database Config";

	private final AppData appData;
	
	private StringTextFieldValidator serverValidator;
	private IntegerTextFieldValidator portValidator;
	private StringTextFieldValidator databaseValidator;
	private StringTextFieldValidator usernameValidator;
	private StringTextFieldValidator passwordValidator;
	
	private SimpleBooleanProperty enableButtonsProperty;
	
	@FXML
	private TextField serverTextField;
	@FXML
	private TextField portTextField;
	@FXML
	private TextField databaseTextField;
	@FXML
	private TextField usernameTextField;
	@FXML
	private TextField passwordTextField;
	@FXML
	private Button testButton;
	@FXML
	private Button saveButton;
	
	public DatabaseConfigDialog (final Stage parentStage) {
		assert(null!=parentStage);
		
		this.appData = AppData.getInstance();
		
		final Stage stage = UiUtil.createDialogStage(parentStage,WINDOW_TITLE); 
		Scene scene = new Scene(this);
		stage.setScene(scene);
        UiUtil.loadRootConstructNode(this,"DatabaseConfigDialog.fxml");
	}
	
	public Stage getStage() {
		return (Stage)getScene().getWindow();
	}
	
	@FXML
	private void initialize() {
		initializeControls();
		initializeAsync();
	}
	
	
	private void initializeControls() {
		serverValidator = new StringTextFieldValidator(serverTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_TEXTFIELD_LENGTH);
		portValidator = new IntegerTextFieldValidator(portTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_INTEGER_TEXTFIELD_LENGTH);
		databaseValidator = new StringTextFieldValidator(databaseTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_TEXTFIELD_LENGTH);
		usernameValidator = new StringTextFieldValidator(usernameTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_TEXTFIELD_LENGTH);
		passwordValidator = new StringTextFieldValidator(passwordTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_TEXTFIELD_LENGTH);
		
		//TODO disable on saveService.runningProperty() 
		enableButtonsProperty = new SimpleBooleanProperty();
		enableButtonsProperty.bind(serverValidator.validProperty().and(portValidator.validProperty()).and(databaseValidator.validProperty())
				.and(usernameValidator.validProperty()).and(passwordValidator.validProperty()).not());
		
		testButton.disableProperty().bind(enableButtonsProperty);
		//TODO disable until test passed
		saveButton.disableProperty().bind(enableButtonsProperty);		
				
		final VirtualKeyboard virtualKeyboard = new VirtualKeyboard(false);
		setBottom(virtualKeyboard);
	}
	
	private void initializeAsync() {
		
		Task<PersistenceConfigDTO> task = new Task<PersistenceConfigDTO>() {			
			@Override
			protected PersistenceConfigDTO call() {
				PersistenceConfigDTO config = appData.getServerConnector().getPersistenceConfig(); 
				return config;
			}
			@Override
			protected void succeeded() {
				log.debug("GetPeristenceConfig Task Succeeded");
				setPersistenceConfig(getValue());
			}
			@Override
			protected void failed() {
				log.debug("GetPeristenceConfig Task Failed");
				Throwable t = getException();
				log.catching(Level.ERROR,t);
				throw new RuntimeException(t);
			}
		};
		
		Thread thread = new Thread(task);
		log.debug("Starting GetPersistenceConfig Task {}",thread);
		thread.start();	
	}
	
	@FXML
	private void handleTest() {
		PersistenceConfigDTO config = getPersistenceConfig();
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				appData.getServerConnector().testPersistenceConnection(config);
				return null;
			}
			@Override
			protected void succeeded() {
				log.debug("TestPersistenceConfig Succeeded");
				final List<String> styles = testButton.getStyleClass();
				styles.remove("button-fail");
				styles.add("button-pass");
				//TODO
			}
			@Override
			protected void failed() {
				final List<String> styles = testButton.getStyleClass();
				styles.remove("button-pass");
				styles.add("button-fail");
				Throwable throwable = getException();
				log.catching(Level.DEBUG,throwable);
				//TODO
				ExceptionDialog.create(AlertType.INFORMATION,"Test Failed",throwable).showAndWait();
				
			}
		};
		
		Thread thread = new Thread(task);
		log.debug("Starting TestPersistenceConfig Task {}", thread);
		thread.start();

	}
	
	@FXML
	private void handleSave() {
		log.debug("Saving PersistenceConfig");
		//TODO TASK
		appData.getServerConnector().setPersistenceConfig(getPersistenceConfig());
	}
	
	@FXML
	private void handleBack() {
		log.debug("Back");
		close();
	}
	
	private void close() {
		getStage().close();
	}
	
	private void setPersistenceConfig(final PersistenceConfigDTO config) {
		serverTextField.setText(config.getServer());
		portTextField.setText(config.getPort());
		databaseTextField.setText(config.getDatabase());
		usernameTextField.setText(config.getUsername());
		passwordTextField.setText(config.getPassword());
	}
	
	private PersistenceConfigDTO getPersistenceConfig() {

		final String server = serverTextField.getText();
		final String port = portTextField.getText();
		final String database = databaseTextField.getText();
		final String username = usernameTextField.getText();
		final String password = passwordTextField.getText();
		
		PersistenceConfigDTO config = new PersistenceConfigDTO(server,port,database,username,password);
		return config;
	}
	
}
