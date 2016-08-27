package com.lunarsky.minipos.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.ProductButtonConfigDTO;
import com.lunarsky.minipos.model.dto.ProductDTO;
import com.lunarsky.minipos.model.ui.Product;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class ProductButtonUpdateDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String WINDOW_TITLE = "Product";
	
	private final AppData appData;
	
	private final PersistenceId id;
	private final PersistenceId parentId;
	private final Integer columnIdx;
	private final Integer rowIdx;
	private final Product product;

	private ProductButtonConfigDTO buttonConfig;
	
	private ObservableList<Product> productList;

	@FXML
	private ComboBox<Product> productComboBox;
	@FXML
	private Button saveButton;

	public ProductButtonUpdateDialog(final Stage parentStage, final ProductButtonConfigDTO buttonConfig) {
		this(parentStage,buttonConfig.getId(),buttonConfig.getParentId(),null,buttonConfig.getColumnIndex(),buttonConfig.getRowIndex());
	}
	
	public ProductButtonUpdateDialog(final Stage parentStage, final PersistenceId parentId, final Integer columnIdx, final Integer rowIdx) {
		this(parentStage,null,parentId,null,columnIdx,rowIdx);
	}
	
	public ProductButtonUpdateDialog(final Stage parentStage, final PersistenceId id, final PersistenceId parentId, final Product product, final Integer columnIdx, final Integer rowIdx) {
		assert(null != parentStage);
		//id can be null
		//parentId can be null
		//product can be null
		assert(null != columnIdx);
		assert(null != rowIdx);

		
		this.appData = AppData.getInstance();
		this.id = id;
		this.parentId = parentId;
		this.product = product;
		this.columnIdx = columnIdx;
		this.rowIdx = rowIdx;

		final Stage stage = UiUtil.createDialogStage(parentStage,WINDOW_TITLE); 
		Scene scene = new Scene(this);
		stage.setScene(scene);
		UiUtil.loadRootConstructNode(this,"ProductButtonUpdateDialog.fxml");
	}
	
	public Stage getStage() {
		return (Stage)getScene().getWindow();
	}
	
	//Can be null if canceled
	public ProductButtonConfigDTO getButtonConfig() {
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
		
		Task<List<ProductDTO>> task = new Task<List<ProductDTO>>() {
			@Override
			protected List<ProductDTO> call() {
				final List<ProductDTO> products = appData.getServerConnector().getProducts();
				return products;
			}
			@Override 
			protected void succeeded() {
				log.debug("GetProducts() Succeeded");

				final List<ProductDTO> productDTOList = getValue();
				final List<Product> list = new ArrayList<Product>();
				for(ProductDTO productDTO: productDTOList) {
					final Product product = new Product(productDTO);
					list.add(product);
				}
				productList = FXCollections.observableList(list);
				Collections.sort(productList);
				productComboBox.setItems(productList);
				
				if(null == product) {
					productComboBox.getSelectionModel().selectFirst();					
				} else {
					productComboBox.getSelectionModel().select(product);
				}
				getStage().sizeToScene();
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
		Task<ProductButtonConfigDTO> task = new Task<ProductButtonConfigDTO>() {
			final ProductButtonConfigDTO buttonConfig = createButtonConfigFromControls();
			@Override
			protected ProductButtonConfigDTO call() throws EntityNotFoundException {
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
	
	private ProductButtonConfigDTO createButtonConfigFromControls() {
		final Product product = productComboBox.getValue();
		final ProductButtonConfigDTO buttonConfig = new ProductButtonConfigDTO(id,parentId,product.createDTO(),columnIdx,rowIdx);
		return buttonConfig;
	}
	
	private void setButtonConfig(final ProductButtonConfigDTO buttonConfig) {
		assert(null != buttonConfig);
		this.buttonConfig = buttonConfig;
	}
	
	private void close() {
		getStage().close();
	}
	
}
