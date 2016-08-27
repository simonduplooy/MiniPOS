package com.lunarsky.minipos.ui;

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
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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
	private StockItemDTO stockItem;
	
	// stockItem can be null to create a new StockItem
	public StockItemUpdateDialog(final Stage parentStage, final StockItemDTO stockItem) {
		assert(null != parentStage);
		assert(null != stockItem);
		
		this.appData = AppData.getInstance();
		this.stockItem = stockItem;

		final String title = (null == stockItem) ? WINDOW_TITLE_ADD_STOCK_ITEM : WINDOW_TITLE_UPDATE_STOCK_ITEM;
		final Stage stage = UiUtil.createDialogStage(parentStage,title); 
		final Scene scene = new Scene(this);
		stage.setScene(scene);
		UiUtil.loadRootConstructNode(this,"StockItemUpdateDialog.fxml");

	}
	
	public Stage getStage() {
		return (Stage)getScene().getWindow();
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
	
}
