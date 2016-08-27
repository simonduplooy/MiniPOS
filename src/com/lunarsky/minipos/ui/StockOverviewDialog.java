package com.lunarsky.minipos.ui;

import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.StockItemDTO;
import com.lunarsky.minipos.util.ErrorMessage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StockOverviewDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String WINDOW_TITLE = "Stock";
	
	private final AppData appData;
	private ObservableList<StockItemDTO> stockItemList;
	private StockItemDTO selectedStockItem;
	private ChangeListener<StockItemDTO> selectedItemChangeListener;
	
	
	@FXML
	private ListView<StockItemDTO> stockListView;
	@FXML
	private TextField nameTextField;
	@FXML
	private CheckBox trackStockLevelCheckBox;
	@FXML
	private TextField stockLevelTextField;
	@FXML
	private Button editButton;
	@FXML
	private Button duplicateButton;
	@FXML
	private Button deleteButton;
	
	
	public StockOverviewDialog(final Stage parentStage) {
		assert(null!=parentStage);
		
		this.appData = AppData.getInstance();
		
		final Stage stage = UiUtil.createDialogStage(parentStage,WINDOW_TITLE); 
		final Scene scene = new Scene(this);
		stage.setScene(scene);
		UiUtil.loadRootConstructNode(this,"StockOverviewDialog.fxml");
	}
	
	public Stage getStage() {
		return (Stage)getScene().getWindow();
	}
	
	@FXML
	private void initialize() {
		
		initializeControls();
		initializeBindings();
		initializeListeners();
		initializeAsync();
		
	}

	private void initializeControls() {
		
		stockListView.setCellFactory((listCell) -> {
			ListCell<StockItemDTO> cell = new ListCell<StockItemDTO>() {
				@Override
				protected void updateItem(StockItemDTO StockItem, boolean empty) {
					super.updateItem(StockItem,empty);
					if(null!=StockItem) {
						setText(StockItem.getName());
					} else {
						setText("");
					}
				}
			};
			return cell;
		});
		
	}
	
	private void initializeBindings() {
		editButton.disableProperty().bind(stockListView.getSelectionModel().selectedItemProperty().isNull());
		duplicateButton.disableProperty().bind(stockListView.getSelectionModel().selectedItemProperty().isNull());
		deleteButton.disableProperty().bind(stockListView.getSelectionModel().selectedItemProperty().isNull());
		
	}
	
	private void initializeListeners() {
		
		getStage().setOnCloseRequest((event) -> { close(); });
		
		selectedItemChangeListener = new ChangeListener<StockItemDTO>() {
			@Override
			public void changed(ObservableValue<? extends StockItemDTO> observable, StockItemDTO oldValue, StockItemDTO newValue) {
				setSelectedStockItem(newValue);
			}
		};
		
		stockListView.getSelectionModel().selectedItemProperty().addListener(selectedItemChangeListener);
		
	}
	
	private void releaseListeners() {
		stockListView.getSelectionModel().selectedItemProperty().removeListener(selectedItemChangeListener);		
	}
	
	private void releaseBindings() {
		editButton.disableProperty().unbind();
		duplicateButton.disableProperty().unbind();
		deleteButton.disableProperty().unbind();
	}
	
	private void initializeAsync() {
		Task<List<StockItemDTO>> task = new Task<List<StockItemDTO>>() {
			@Override
			protected List<StockItemDTO> call() {
				List<StockItemDTO> StockItems = appData.getServerConnector().getStock();
				return StockItems;
			}
			@Override
			protected void succeeded() {
				log.debug("GetStockItems() Succeeded");
				stockItemList = FXCollections.observableList(getValue());
				Collections.sort(stockItemList);
				stockListView.setItems(stockItemList);
			}
			@Override
			protected void failed() {
				log.error("GetStockItems() Failed");
				final Throwable throwable = getException();
				log.catching(Level.ERROR,throwable);
				ExceptionDialog.create(AlertType.ERROR,"Could not retrieve StockItems",throwable);

			}
		};
		
		Thread thread = new Thread(task);
		log.debug("Starting GetStockItems() Task {}",thread);
		thread.start();		
	}
	
	@FXML
	private void handleAdd() {
		updateStockItem(null);
	}	
	
	@FXML
	private void handleEdit() {
		final StockItemDTO StockItem = getSelectedStockItem();
		updateStockItem(StockItem);
	}

	@FXML
	private void handleDuplicate() {		
		final StockItemDTO StockItem = getSelectedStockItem().duplicate();
		log.debug("Duplicate StockItem {}",StockItem);
		updateStockItem(StockItem);
	}
	
	private void updateStockItem(final StockItemDTO StockItem) {
		log.debug("Update StockItem {}",StockItem);
		
		final StockItemUpdateDialog dialog = new StockItemUpdateDialog(getStage(),StockItem);
		dialog.getStage().showAndWait();
		final StockItemDTO updatedStockItem = dialog.getStockItem();
		
		//The StockItem was updated
		if(updatedStockItem != StockItem) {
			stockItemList.remove(StockItem);
			stockItemList.add(updatedStockItem);
			FXCollections.sort(stockItemList);
			stockListView.getSelectionModel().select(updatedStockItem);
		}
	}

	@FXML
	private void handleDelete() {

		Task<Void> task = new Task<Void>() {
			private final StockItemDTO StockItem = getSelectedStockItem();
			@Override
			protected Void call() throws EntityNotFoundException {
				appData.getServerConnector().deleteStockItem(StockItem.getId());
				return null;
			}
			@Override
			protected void succeeded() {
				log.debug("DeleteStockItem() Succeeded");
				stockItemList.remove(StockItem);
			}
			@Override
			protected void failed() {
				final Throwable t = getException();
				log.error("DeleteStockItem() Failed");
				log.catching(Level.ERROR, t);
				ExceptionDialog.create(AlertType.ERROR, "Could not Delete StockItem", t).show();
			}
			
		};
		
		//StockItemListView.getSelectionModel().select(null);
		stockListView.getSelectionModel().clearSelection();
		
		Thread thread = new Thread(task);
		log.debug("Starting DeleteStockItem() Task {}",thread);
		thread.start();
	}
	
	@FXML
	private void handleOK() {
		log.debug("Closing Dialog");
		getStage().close();
	}
	
	private void close() {
		
		releaseBindings();
		releaseListeners();
		
	}
	
	private void setSelectedStockItem(StockItemDTO stockItem) {
		this.selectedStockItem = stockItem;
		if(null==stockItem) {
			nameTextField.setText("");
			trackStockLevelCheckBox.setSelected(false);
			stockLevelTextField.setText("");						
		} else {
			nameTextField.setText(stockItem.getName());
			trackStockLevelCheckBox.setSelected(stockItem.getTrackStockLevel());
			final String stockLevelText = stockItem.getStockLevel().toString();
			stockLevelTextField.setText(stockLevelText);			
		}
	}

	private StockItemDTO getSelectedStockItem() {
		return selectedStockItem;
	}
	
}
