package com.lunarsky.minipos.ui;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.Product;
import com.lunarsky.minipos.model.ProductButtonConfig;
import com.lunarsky.minipos.model.ProductButtonGroupConfig;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class ProductConfigureView extends BorderPane implements ProductButtonObserver {
	private static final Logger log = LogManager.getLogger();

	private final AppData appData;
	private final Stage stage;
	
	private PersistenceId parentId;
	
	@FXML
	private GridPane productGridPane;
	
	public ProductConfigureView(final AppData appData, final Stage stage) {
		assert(null != appData);
		assert(null != stage);
		
		this.appData = appData;
		this.stage = stage;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProductConfigureView.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
        	loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
	}
		
	@FXML
	private void initialize() {
		initializeControls();
		initializeProductPane();
		initializeAsync();
	}
	
	private void initializeControls() {
	}

	private void initializeAsync() {
		
		//TODO GetProductButtonGroups
		final Task<List<ProductButtonGroupConfig>> buttonGroupTask = new Task<List<ProductButtonGroupConfig>>() {
			@Override
			protected List<ProductButtonGroupConfig> call() {
				//appData.getServerConnector().getProductButtons();
				return null;
			}
			@Override
			protected void succeeded() {
				log.debug("Succeeded");
			}
			@Override
			protected void failed() {
				log.debug("Failed");
			}
		};
		
		//TODO GetProductButtonGroups
		final Task<List<ProductButtonConfig>> buttonTask = new Task<List<ProductButtonConfig>>() {
			@Override
			protected List<ProductButtonConfig> call() {
				final List<ProductButtonConfig> buttonConfigList = appData.getServerConnector().getProductButtons();
				return buttonConfigList;
			}
			@Override
			protected void succeeded() {
				log.debug("Succeeded");
				final List<ProductButtonConfig> buttonConfigList = getValue();
			}
			@Override
			protected void failed() {
				log.debug("Failed");
			}
		};
		
	final Thread buttonThread = new Thread(buttonTask);
	buttonThread.start();
		
	}
	
	@FXML
	private void handleDone(final ActionEvent event) {
		log.debug("handleDone()");
		close();
	}
	
	@FXML
	private void handleBack(final ActionEvent event) {
		log.debug("handleBack()");
		//TODO
	}
	
	private void initializeProductPane() {
		
		final List<ColumnConstraints> columnConstraintsList = productGridPane.getColumnConstraints();
		final List<RowConstraints> rowConstraintsList = productGridPane.getRowConstraints();
		
		columnConstraintsList.clear();
		rowConstraintsList.clear();
		
		for(int rowIdx=0; rowIdx< UiConst.NO_PRODUCT_BUTTON_ROWS; rowIdx++) {
			for(int columnIdx=0; columnIdx<UiConst.NO_PRODUCT_BUTTON_COLUMNS; columnIdx++) {
				final ProductButtonBlank button = new ProductButtonBlank(this,columnIdx,rowIdx);
				productGridPane.getChildren().add(button);
			}
		}
		
		//TODO CONFIGURE CONSTRAINTS
	}
	
	//Implement ProductButtonObserver
	public void createProductButton(final Integer columnIdx, final Integer rowIdx) {
		log.debug("createProductButton()");
		
		final ProductButtonUpdateDialog dialog = new ProductButtonUpdateDialog(appData,stage,parentId,columnIdx,rowIdx);
		dialog.getStage().showAndWait();
		final ProductButtonConfig config = dialog.getButtonConfig();
		
		if(null != config) {
			final ProductButton button = new ProductButton(this,config);
			productGridPane.getChildren().add(button);
		}
	}
	
	public void updateProductButton(final ProductButton button) {
		log.debug("updateProductButton()");
		final ProductButtonConfig config = button.getConfig();
		final ProductButtonUpdateDialog dialog = new ProductButtonUpdateDialog(appData,stage,config);
		dialog.getStage().showAndWait();
		final ProductButtonConfig updatedConfig = dialog.getButtonConfig();
		if(null != updatedConfig) {
			button.setConfig(updatedConfig);
		}
	}
	
	public void productSelected(final Product product) {
		log.debug("productSelected {}", product);
	}
	
	public void deleteProductButton(final ProductButton button) {
		log.debug("deleteProductButton {}",button);
		//TODO Delete from Database
		productGridPane.getChildren().remove(button);
	}
	
	public void createProductButtonGroup(final Integer columnIdx, final Integer rowIdx) {
		log.debug("createProductButtonGroup()");
		
		final Task<ProductButtonGroupConfig> task = new Task<ProductButtonGroupConfig>() {
			@Override
			protected ProductButtonGroupConfig call() {
				return null;
			}
			@Override
			protected void succeeded() {
				
			}
			@Override
			protected void failed() {
				
			}
		};
		
		final ProductButtonGroupConfig config = new ProductButtonGroupConfig(null,"Drinks",columnIdx,rowIdx);
		final ProductButtonGroup button = new ProductButtonGroup(this,config);
		productGridPane.getChildren().add(button);
	}
	
	public void productButtonGroupSelected(final PersistenceId id) {
		log.debug("productButtonGroupSelected() ",id);
	}
	
	public void deleteProductButtonGroup(final ProductButtonGroup button) {
		log.debug("deleteProductButtonGroup {}",button);
		//TODO Delete from Database
		productGridPane.getChildren().remove(button);
	}
	
	private void close() {
		appData.getViewManager().closeProductConfigureView();
	}
}
