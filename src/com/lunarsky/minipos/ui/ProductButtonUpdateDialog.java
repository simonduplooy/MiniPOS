package com.lunarsky.minipos.ui;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.common.exception.NameInUseException;
import com.lunarsky.minipos.common.exception.PasswordInUseException;
import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.Product;
import com.lunarsky.minipos.model.ProductButtonConfig;
import com.lunarsky.minipos.model.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class ProductButtonUpdateDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();

	private final AppData appData;
	private final Stage stage;
	
	private final PersistenceId id;
	private final PersistenceId parentId;
	private final Integer columnIdx;
	private final Integer rowIdx;
	private final Product product;

	private ProductButtonConfig buttonConfig;
	
	private ObservableList<Product> productList;

	@FXML
	private ComboBox<Product> productComboBox;
	@FXML
	private Button saveButton;

	public ProductButtonUpdateDialog(final AppData appData, final Stage parentStage, final ProductButtonConfig buttonConfig) {
		this(appData,parentStage,buttonConfig.getId(),buttonConfig.getParentId(),null,buttonConfig.getColumnIndex(),buttonConfig.getRowIndex());
	}
	
	public ProductButtonUpdateDialog(final AppData appData, final Stage parentStage, final PersistenceId parentId, final Integer columnIdx, final Integer rowIdx) {
		this(appData,parentStage,null,parentId,null,columnIdx,rowIdx);
	}
	
	public ProductButtonUpdateDialog(final AppData appData, final Stage parentStage, final PersistenceId id, final PersistenceId parentId, final Product product, final Integer columnIdx, final Integer rowIdx) {

		assert(null != appData);
		assert(null != parentStage);
		//id can be null
		//parentId can be null
		//product can be null
		assert(null != columnIdx);
		assert(null != rowIdx);

		
		this.appData = appData;
		this.id = id;
		this.parentId = parentId;
		this.product = product;
		this.columnIdx = columnIdx;
		this.rowIdx = rowIdx;

		stage = new Stage();
		stage.initOwner(parentStage);
		stage.initModality(Modality.WINDOW_MODAL); 
				
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProductButtonUpdateDialog.fxml"));
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
	public ProductButtonConfig getButtonConfig() {
		return buttonConfig;
	}
	
	@FXML
	private void initialize() {
		initializeControls();
		initializeServices();
		initializeAsync();
	}
	
	private void initializeControls() {
		
		productComboBox.setCellFactory((listCell) -> {
			ListCell<Product> cell = new ListCell<Product>() {
				@Override
				protected void updateItem(Product product, boolean empty) {
					super.updateItem(product,empty);
					if(null != product) {
						setText(product.getName());
					} else {
						setText("");
					}
				}
			};
			return cell;
		});
		
		final StringConverter<Product> converter = new StringConverter<Product>() {
			@Override
			public String toString(final Product product) {
				return product.getName();
			}
			@Override
			public Product fromString(final String productName) {
				return null;
			}
		};
		
		productComboBox.setConverter(converter);
		
		//TODO Bind Save Service
		//saveButton.disableProperty().bind(saveService.runningProperty());
	}
	
	private void initializeServices() {
		
	}
	
	private void initializeAsync() {
		
		Task<List<Product>> task = new Task<List<Product>>() {
			@Override
			protected List<Product> call() {
				final List<Product> products = appData.getServerConnector().getProducts();
				return products;
			}
			@Override 
			protected void succeeded() {
				log.debug("GetProducts() Succeeded");
				productList = FXCollections.observableList(getValue());
				Collections.sort(productList);
				productComboBox.setItems(productList);
				
				if(null == product) {
					productComboBox.getSelectionModel().selectFirst();					
				} else {
					productComboBox.getSelectionModel().select(product);
				}
				stage.sizeToScene();
			}
			@Override
			protected void failed() {
				final Throwable t = getException();
				log.catching(Level.ERROR, t);
				throw new RuntimeException(t);
			}
		};
		
		Thread thread = new Thread(task);
		log.debug("Starting Task GetProducts() {}",thread);
		thread.start();
	}
	
	@FXML
	private void handleSave(ActionEvent event) {
		
		//TODO Service
		Task<ProductButtonConfig> task = new Task<ProductButtonConfig>() {
			final ProductButtonConfig buttonConfig = createButtonConfigFromControls();
			@Override
			protected ProductButtonConfig call() throws EntityNotFoundException {
				return appData.getServerConnector().saveProductButton(buttonConfig);
			}
			@Override
			protected void succeeded() {
				setButtonConfig(getValue());
				log.debug("SaveProductButton() Succeeded");
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
		log.debug("Creating SaveProductButton() Task {}",thread);
		thread.start();
	}

	@FXML
	private void handleCancel(ActionEvent event) {
		close();
	}
	
	private ProductButtonConfig createButtonConfigFromControls() {
		final Product product = productComboBox.getValue();
		final ProductButtonConfig buttonConfig = new ProductButtonConfig(id,parentId,product,columnIdx,rowIdx);
		return buttonConfig;
	}
	
	private void setButtonConfig(final ProductButtonConfig buttonConfig) {
		assert(null != buttonConfig);
		this.buttonConfig = buttonConfig;
	}
	
	private void close() {
		stage.close();
	}
	
}
