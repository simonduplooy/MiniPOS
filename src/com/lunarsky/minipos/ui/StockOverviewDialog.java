package com.lunarsky.minipos.ui;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.StockItem;
import com.lunarsky.minipos.util.ErrorMessage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StockOverviewDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String WINDOW_TITLE = "Stock";
	
	private final AppData appData;
	private final Stage stage;
	
	private ObservableList<StockItem> stockItemList;
	private StockItem selectedStockItem;
	private ChangeListener<StockItem> selectedItemChangeListener;
	
	
	@FXML
	private ListView<StockItem> stockListView;
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
	
	
	public StockOverviewDialog(final AppData appData, final Stage parentStage) {
		assert(null!=appData);
		assert(null!=parentStage);
		
		this.appData = appData;
		
		stage = new Stage();
		stage.initOwner(parentStage);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setTitle(WINDOW_TITLE); 
		
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("StockOverviewDialog.fxml"));
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
		getStage().showAndWait();
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
			ListCell<StockItem> cell = new ListCell<StockItem>() {
				@Override
				protected void updateItem(StockItem StockItem, boolean empty) {
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
		
		selectedItemChangeListener = new ChangeListener<StockItem>() {
			@Override
			public void changed(ObservableValue<? extends StockItem> observable, StockItem oldValue, StockItem newValue) {
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
		Task<List<StockItem>> task = new Task<List<StockItem>>() {
			@Override
			protected List<StockItem> call() {
				List<StockItem> StockItems = appData.getServerConnector().getStock();
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
		final StockItem StockItem = getSelectedStockItem();
		updateStockItem(StockItem);
	}

	@FXML
	private void handleDuplicate() {		
		final StockItem StockItem = getSelectedStockItem().duplicate();
		log.debug("Duplicate StockItem {}",StockItem);
		updateStockItem(StockItem);
	}
	
	private void updateStockItem(final StockItem StockItem) {
		log.debug("Update StockItem {}",StockItem);
		
		StockItemUpdateDialog dialog = null;
		try {
			dialog = new StockItemUpdateDialog(appData,getStage(),StockItem);
		} catch (Exception e) {
			log.catching(Level.ERROR, e);
			ExceptionDialog.create(AlertType.ERROR, ErrorMessage.ERROR_CREATING_DIALOG_TEXT, e).show();
		}
		
		dialog.showAndWait();
		final StockItem updatedStockItem = dialog.getStockItem();
		
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
			private final StockItem StockItem = getSelectedStockItem();
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
	
	private void setSelectedStockItem(StockItem stockItem) {
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

	private StockItem getSelectedStockItem() {
		return selectedStockItem;
	}
	
	private Stage getStage() {
		assert(null!=stage);
		return stage;
	}
	
}
