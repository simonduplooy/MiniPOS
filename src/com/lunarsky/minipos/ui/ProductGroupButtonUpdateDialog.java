package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.ProductGroupButtonConfigDTO;
import com.lunarsky.minipos.model.ui.PersistenceId;
import com.lunarsky.minipos.model.ui.ProductGroupButtonConfig;
import com.lunarsky.minipos.ui.validator.StringTextFieldValidator;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ProductGroupButtonUpdateDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private final static String WINDOW_TITLE = "Product Group";

	private final AppData appData;

	private ProductGroupButtonConfig buttonConfig;
	private StringTextFieldValidator nameValidator;
	private boolean wasSaved;

	@FXML
	private TextField nameTextField;
	@FXML
	private Button saveButton;

	public ProductGroupButtonUpdateDialog(final Stage parentStage, final ProductGroupButtonConfig buttonConfig) {
		assert(null != parentStage);
		assert(null != buttonConfig);

		this.appData = AppData.getInstance();
		
		//Create copy to not modify the original if cancelled
		this.buttonConfig = new ProductGroupButtonConfig(buttonConfig);

		UiUtil.createDialog(parentStage,WINDOW_TITLE,this,"ProductButtonGroupUpdateDialog.fxml");

	}
	
	public Stage getStage() {
		return (Stage)getScene().getWindow();
	}
	
	public boolean wasSaved() {
		return wasSaved;
	}
	
	public ProductGroupButtonConfig getButtonConfig() {
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
			final ProductGroupButtonConfigDTO configDTO = createButtonConfigFromControls().getDTO();
			@Override
			protected ProductGroupButtonConfigDTO call() throws EntityNotFoundException {
				return appData.getServerConnector().saveProductGroupButton(configDTO);
			}
			@Override
			protected void succeeded() {
				final ProductGroupButtonConfigDTO configDTO = getValue();
				buttonConfig.setDTO(configDTO);
				wasSaved = true;
				log.debug("SaveProductButtonGroup() Succeeded");
				close();
			}
			@Override
			protected void failed() {
				final Throwable throwable = getException();
				log.catching(Level.ERROR,throwable);
				final ExceptionAlert alert = new ExceptionAlert(AlertType.ERROR,UiConst.UNKNOWN_EXCEPTION,throwable);
				alert.showAndWait();
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
	
	private ProductGroupButtonConfig createButtonConfigFromControls() {
		final PersistenceId id = buttonConfig.getId();
		final PersistenceId parentId = buttonConfig.getParentId();
		final String name = nameTextField.getText();
		final Integer columnIdx = buttonConfig.getColumnIndex();
		final Integer rowIdx = buttonConfig.getRowIndex();
		final ProductGroupButtonConfig buttonConfig = new ProductGroupButtonConfig(id,parentId,name,columnIdx,rowIdx);
		return buttonConfig;
	}
	
	private void setButtonConfig(final ProductGroupButtonConfig buttonConfig) {
		assert(null != buttonConfig);
		this.buttonConfig = buttonConfig;
	}
	
	private void close() {
		getStage().close();
	}
	
}
