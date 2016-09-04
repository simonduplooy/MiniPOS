package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.common.exception.NameInUseException;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.ProductGroupDTO;
import com.lunarsky.minipos.model.ui.Product;
import com.lunarsky.minipos.model.ui.ProductGroup;
import com.lunarsky.minipos.ui.validator.StringTextFieldValidator;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProductGroupUpdateDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String WINDOW_TITLE = "Product Group";
	
	private final AppData appData;
	private StringTextFieldValidator nameValidator;
	private Service<ProductGroupDTO> saveService;
	private ProductGroup group;
	private boolean wasSaved;
	
	@FXML
	private Label nameErrorLabel;
	@FXML
	private TextField nameTextField;
	@FXML
	private Button saveButton;
	
	public ProductGroupUpdateDialog(final Stage parentStage, final ProductGroup group) {
		assert(null != parentStage);
		assert(null != group);
		
		this.appData = AppData.getInstance();
		//Create a copy so that the original product is not updated in case the dialog is cancelled
		this.group = new ProductGroup(group);

		UiUtil.createDialog(parentStage,WINDOW_TITLE,this,"ProductGroupUpdateDialog.fxml");

	}
	
	public ProductGroup getGroup() {
		assert(null != group);
		return group;
	}
	
	@FXML
	private void initialize() {
		createSaveService();
		initializeMembers();
		initializeControls();
	}
	
	private void initializeMembers() {
	}
	
	private void initializeControls() {
		
		final ProductGroup group = getGroup();

		nameErrorLabel.managedProperty().bind(nameErrorLabel.visibleProperty());
		clearErrorMessages();
		
		nameValidator = new StringTextFieldValidator(nameTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_TEXTFIELD_LENGTH);
		
		nameTextField.textProperty().bindBidirectional(group.nameProperty());
		
		saveButton.disableProperty().bind(nameValidator.validProperty().not().or(saveService.runningProperty()));
	}

	public void createSaveService() {
		
		saveService = new Service<ProductGroupDTO>() {
			@Override
			protected Task<ProductGroupDTO> createTask() {
				final Task<ProductGroupDTO> task = new Task<ProductGroupDTO>() {
					final ProductGroupDTO groupDTO = getGroup().getDTO();
					@Override
					protected ProductGroupDTO call() throws EntityNotFoundException {
						return appData.getServerConnector().saveProductGroup(groupDTO);
					}
					@Override
					protected void succeeded() {
						log.debug("saveProductGroup() Succeeded");
						final ProductGroupDTO groupDTO = getValue();
						getGroup().setDTO(groupDTO);
						wasSaved = true;
						close();
					}
					@Override
					protected void failed() {
						final Throwable t = getException();
						log.debug("saveProductGroup() Failed");
						log.catching(Level.ERROR, t);
						if(t instanceof NameInUseException) {
							nameErrorLabel.setVisible(true);
						} else {
							throw new RuntimeException(t);
						}
					}
				};
				
				return task;
			}
		};
	}
	
	public Stage getStage() {
		return (Stage)getScene().getWindow();
	}
	
	public boolean wasSaved() {
		return wasSaved;
	}
	
	private void clearErrorMessages() {
		nameErrorLabel.setVisible(false);
	}
	
	@FXML
	private void handleSave(ActionEvent event) {
		log.debug("handleSave");
		saveService.restart();
	}

	@FXML
	private void handleCancel(ActionEvent event) {
		close();
	}
	
	private void close() {
		getStage().close();
	}
	
}
