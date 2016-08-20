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
		initializeAsync();
	}
	
	private void initializeControls() {

		final List<RowConstraints> rowConstraints = productGridPane.getRowConstraints();
		final List<ColumnConstraints> columnConstraints = productGridPane.getColumnConstraints();
		final int noRows = rowConstraints.size();
		final int noColumns = columnConstraints.size();
		
		for(int row=0;row<noRows;row++) {
			for(int column=0;column<noColumns;column++) {
				final ProductButtonBlank button = new ProductButtonBlank(this,column,row);
				productGridPane.getChildren().add(button);
			}
		}
	}

	private void initializeAsync() {
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
		//TODO Create Config and Save
		final ProductButtonGroupConfig config = new ProductButtonGroupConfig(null,"Drinks",columnIdx,rowIdx);
		final ProductButtonGroup button = new ProductButtonGroup(this,config);
		productGridPane.getChildren().add(button);
		
		final ColumnConstraints columnConstraints = productGridPane.getColumnConstraints().get(columnIdx);
		final RowConstraints rowConstraints = productGridPane.getRowConstraints().get(rowIdx);
		columnConstraints.setMinWidth(button.getMinWidth());
		rowConstraints.setMinHeight(button.getMinHeight());
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
