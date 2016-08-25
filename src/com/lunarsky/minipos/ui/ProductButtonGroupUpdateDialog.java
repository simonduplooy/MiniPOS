package com.lunarsky.minipos.ui;

import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.ProductGroupButtonConfigDTO;
import com.lunarsky.minipos.ui.validator.StringTextFieldValidator;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProductButtonGroupUpdateDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();

	private final AppData appData;
	private final Stage stage;

	private ProductGroupButtonConfigDTO buttonConfig;

	@FXML
	private TextField nameTextField;
	private StringTextFieldValidator nameValidator;
	@FXML
	private Button saveButton;

	public ProductButtonGroupUpdateDialog(final AppData appData, final Stage parentStage, final ProductGroupButtonConfigDTO buttonConfig) {

		assert(null != appData);
		assert(null != parentStage);
		//buttonConfig can be null

		this.appData = appData;
		this.buttonConfig = buttonConfig;

		stage = new Stage();
		stage.initOwner(parentStage);
		stage.initModality(Modality.WINDOW_MODAL); 
				
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProductButtonGroupUpdateDialog.fxml"));
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
	
	public Stage getStage() {
		assert(null != stage);
		return stage;
	}
	
	//Can be null if canceled
	public ProductGroupButtonConfigDTO getButtonConfig() {
		return buttonConfig;
	}
	
	@FXML
	private void initialize() {
		initializeControls();
		initializeServices();
		initializeAsync();
	}
	
	private void initializeControls() {
		if(null == buttonConfig) {
			nameTextField.setText("");
		} else {
			nameTextField.setText(buttonConfig.getName());
		}
		
		nameValidator = new StringTextFieldValidator(nameTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_TEXTFIELD_LENGTH);
		saveButton.disableProperty().bind(nameValidator.validProperty().not());
		//TODO Bind Save Service
		//saveButton.disableProperty().bind(saveService.runningProperty());
		
		nameTextField.requestFocus();
	}
	
	private void initializeServices() {
		
	}
	
	private void initializeAsync() {

	}
	
	@FXML
	private void handleSave(ActionEvent event) {
		
		//TODO Service
		Task<ProductGroupButtonConfigDTO> task = new Task<ProductGroupButtonConfigDTO>() {
			final ProductGroupButtonConfigDTO buttonConfig = createButtonConfigFromControls();
			@Override
			protected ProductGroupButtonConfigDTO call() throws EntityNotFoundException {
				return appData.getServerConnector().saveProductButtonGroup(buttonConfig);
			}
			@Override
			protected void succeeded() {
				setButtonConfig(getValue());
				log.debug("SaveProductButtonGroup() Succeeded");
				close();
			}
			@Override
			protected void failed() {
				final Throwable t = getException();
				log.catching(Level.ERROR,t);
				throw new RuntimeException(t);
			}
		};
		
		Thread thread = new Thread(task);
		log.debug("Creating SaveProductButtonGroup() Task {}",thread);
		thread.start();
	}

	@FXML
	private void handleCancel(ActionEvent event) {
		close();
	}
	
	private ProductGroupButtonConfigDTO createButtonConfigFromControls() {
		final PersistenceId id = buttonConfig.getId();
		final PersistenceId parentId = buttonConfig.getParentId();
		final String name = nameTextField.getText();
		final Integer columnIdx = buttonConfig.getColumnIndex();
		final Integer rowIdx = buttonConfig.getRowIndex();
		final ProductGroupButtonConfigDTO buttonConfig = new ProductGroupButtonConfigDTO(id,parentId,name,columnIdx,rowIdx);
		return buttonConfig;
	}
	
	private void setButtonConfig(final ProductGroupButtonConfigDTO buttonConfig) {
		assert(null != buttonConfig);
		this.buttonConfig = buttonConfig;
	}
	
	private void close() {
		stage.close();
	}
	
}
