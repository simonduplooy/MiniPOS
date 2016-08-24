package com.lunarsky.minipos.ui;

import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.common.exception.NameInUseException;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.ProductGroupDTO;
import com.lunarsky.minipos.model.dto.ProductGroupDTO;
import com.lunarsky.minipos.model.ui.Product;
import com.lunarsky.minipos.model.ui.ProductGroup;
import com.lunarsky.minipos.ui.validator.CurrencyTextFieldValidator;
import com.lunarsky.minipos.ui.validator.StringTextFieldValidator;

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
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProductGroupUpdateDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private final AppData appData;
	
	@FXML
	private Label nameErrorLabel;
	@FXML
	private TextField nameTextField;

	@FXML
	private Button saveButton;
	
	private StringTextFieldValidator nameValidator;
	
	private Service<ProductGroupDTO> saveService;
	
	private final Stage stage;
	private ProductGroup group;
	 
	// product can be null to create a new Product
	public ProductGroupUpdateDialog(final AppData appData, final Stage parentStage, final ProductGroup group) {
		assert(null != appData);
		assert(null != parentStage);
		
		this.appData = appData;
		setGroup((null!=group)?group:new ProductGroup());

		stage = new Stage();
		stage.initOwner(parentStage);
		stage.initModality(Modality.WINDOW_MODAL);
		
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProductGroupUpdateDialog.fxml"));
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
	
	public ProductGroup getGroup() {
		assert(null != group);
		return group;
	}
	
	private void setGroup(final ProductGroup group) {
		assert(null != group);
		this.group = group;
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
					final ProductGroupDTO groupDTO = getGroup().createDTO();
					@Override
					protected ProductGroupDTO call() throws EntityNotFoundException {
						return appData.getServerConnector().saveProductGroup(groupDTO);
					}
					@Override
					protected void succeeded() {
						log.debug("saveProductGroup() Succeeded");
						final ProductGroupDTO groupDTO = getValue();
						final ProductGroup group = new ProductGroup(groupDTO);
						setGroup(group);
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
		assert(stage!=null);
		return stage;
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
