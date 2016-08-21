package com.lunarsky.minipos.ui;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.Product;
import com.lunarsky.minipos.model.ProductButtonConfig;
import com.lunarsky.minipos.model.ProductButtonGroupConfig;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class ProductButtonGroupUpdateDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();

	private final AppData appData;
	private final Stage stage;
	
	private final PersistenceId id;
	private final PersistenceId parentId;
	private final String name;
	private final Integer columnIdx;
	private final Integer rowIdx;

	private ProductButtonGroupConfig buttonConfig;

	@FXML
	private TextField nameTextField;
	@FXML
	private Button saveButton;

	public ProductButtonGroupUpdateDialog(final AppData appData, final Stage parentStage, final ProductButtonGroupConfig buttonConfig) {
		this(appData,parentStage,buttonConfig.getId(),buttonConfig.getParentId(),null,buttonConfig.getColumnIndex(),buttonConfig.getRowIndex());
	}
	
	public ProductButtonGroupUpdateDialog(final AppData appData, final Stage parentStage, final PersistenceId parentId, final Integer columnIdx, final Integer rowIdx) {
		this(appData,parentStage,null,parentId,"",columnIdx,rowIdx);
	}
	
	public ProductButtonGroupUpdateDialog(final AppData appData, final Stage parentStage, final PersistenceId id, final PersistenceId parentId, final String name, final Integer columnIdx, final Integer rowIdx) {

		assert(null != appData);
		assert(null != parentStage);
		//id can be null
		//parentId can be null
		assert(null != name);
		assert(null != columnIdx);
		assert(null != rowIdx);

		
		this.appData = appData;
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.columnIdx = columnIdx;
		this.rowIdx = rowIdx;

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
	public ProductButtonGroupConfig getButtonConfig() {
		return buttonConfig;
	}
	
	@FXML
	private void initialize() {
		initializeControls();
		initializeServices();
		initializeAsync();
	}
	
	private void initializeControls() {
		//TODO Bind Save Service
		//saveButton.disableProperty().bind(saveService.runningProperty());
	}
	
	private void initializeServices() {
		
	}
	
	private void initializeAsync() {

	}
	
	@FXML
	private void handleSave(ActionEvent event) {
		
		//TODO Service
		Task<ProductButtonGroupConfig> task = new Task<ProductButtonGroupConfig>() {
			final ProductButtonGroupConfig buttonConfig = createButtonConfigFromControls();
			@Override
			protected ProductButtonGroupConfig call() throws EntityNotFoundException {
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
	
	private ProductButtonGroupConfig createButtonConfigFromControls() {
		final ProductButtonGroupConfig buttonConfig = new ProductButtonGroupConfig(id,parentId,name,columnIdx,rowIdx);
		return buttonConfig;
	}
	
	private void setButtonConfig(final ProductButtonGroupConfig buttonConfig) {
		assert(null != buttonConfig);
		this.buttonConfig = buttonConfig;
	}
	
	private void close() {
		stage.close();
	}
	
}
