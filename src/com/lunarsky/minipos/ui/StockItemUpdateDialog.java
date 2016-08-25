package com.lunarsky.minipos.ui;

import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.StockItemDTO;
import com.lunarsky.minipos.ui.validator.DoubleTextFieldValidator;
import com.lunarsky.minipos.ui.validator.StringTextFieldValidator;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StockItemUpdateDialog extends VBox {
	private static final Logger log = LogManager.getLogger();
	
	private static String WINDOW_TITLE_ADD_STOCK_ITEM = "Add Stock Item";
	private static String WINDOW_TITLE_UPDATE_STOCK_ITEM = "Update Stock Item";
	
	private final AppData appData;
	
	@FXML
	private TextField nameTextField;
	@FXML
	private CheckBox trackStockLevelCheckBox;
	@FXML
	private TextField stockLevelTextField;
	@FXML
	private Button saveButton;
	
	private StringTextFieldValidator nameValidator;
	private DoubleTextFieldValidator stockLevelValidator;
	
	
	private final Stage stage;
	private StockItemDTO stockItem;
	
	// stockItem can be null to create a new StockItem
	public StockItemUpdateDialog(final AppData appData, final Stage parentStage, final StockItemDTO stockItem) {
		assert(null != appData);
		assert(null != parentStage);
		assert(null != stockItem);
		
		this.appData = appData;
		this.stockItem = stockItem;

		stage = new Stage();
		stage.initOwner(parentStage);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setTitle((null == stockItem) ? WINDOW_TITLE_ADD_STOCK_ITEM : WINDOW_TITLE_UPDATE_STOCK_ITEM); 
				
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("StockItemUpdateDialog.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
        	loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
		
		Scene scene = new Scene(this);
		stage.setScene(scene);

	}
	
	public void showAndWait() {
		stage.showAndWait();
	}
	
	//Can be null if canceled
	public StockItemDTO getStockItem() {
		return stockItem;
	}
	
	@FXML
	private void initialize() {
		initializeControls(getStockItem());
	}
	
	private void initializeControls(final StockItemDTO stockItem) {
		if(null==stockItem) {
			nameTextField.setText("");
			trackStockLevelCheckBox.setSelected(false);
			stockLevelTextField.setText(Double.toString(0.0));
			
		} else {
			nameTextField.setText(stockItem.getName());
			trackStockLevelCheckBox.setSelected(stockItem.getTrackStockLevel());
			final String stockLevelText = stockItem.getStockLevel().toString();
			stockLevelTextField.setText(stockLevelText);
		}
		
		//clearErrorMessages();
		
		nameValidator = new StringTextFieldValidator(nameTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_TEXTFIELD_LENGTH);
		stockLevelValidator = new DoubleTextFieldValidator(stockLevelTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_TEXTFIELD_LENGTH);
		
		saveButton.disableProperty().bind(nameValidator.validProperty().and(stockLevelValidator.validProperty()).not());
	}

	
	@FXML
	private void handleSave(ActionEvent event) {

		Task<StockItemDTO> task = new Task<StockItemDTO>() {
			final StockItemDTO stockItem = createStockItemFromControls();
			@Override
			protected StockItemDTO call() throws EntityNotFoundException {
				return appData.getServerConnector().saveStockItem(stockItem);
			}
			@Override
			protected void succeeded() {
				log.debug("SaveStockItem() Succeeded");
				final StockItemDTO updatedStockItem = getValue();
				setStockItem(updatedStockItem);
				getStage().close();
			}
			@Override
			protected void failed() {
				final Throwable t = getException();
				log.debug("SaveStockItem() Failed");
				log.catching(Level.ERROR, t);
				ExceptionDialog.create(AlertType.ERROR, "Could not Save Stock Item", t).show();
			}
		};
		
		Thread thread = new Thread(task);
		log.debug("Creating SaveStockItem() Task {}",thread);
		thread.start();
	}

	@FXML
	private void handleCancel(ActionEvent event) {
		//TODO cancel save task ?
		getStage().close();
	}
	
	private StockItemDTO createStockItemFromControls() {
		final PersistenceId id = (null == stockItem) ? null : stockItem.getId();
		final String name = nameTextField.getText();
		final boolean trackStockLevel = trackStockLevelCheckBox.isSelected();
		//TODO Validate
		final String stockLevelText = stockLevelTextField.getText();
		final Double stockLevel = Double.parseDouble(stockLevelText);
		
		final StockItemDTO updatedStockItem = new StockItemDTO(id, name, trackStockLevel, stockLevel);
		return updatedStockItem;
	}
	
	private void setStockItem(final StockItemDTO stockItem) {
		this.stockItem = stockItem;
	}
	
	private Stage getStage() {
		assert(stage!=null);
		return stage;
	}
	
}
