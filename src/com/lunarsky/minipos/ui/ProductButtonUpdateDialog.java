package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.ProductButtonConfigDTO;
import com.lunarsky.minipos.model.ui.Product;
import com.lunarsky.minipos.model.ui.ProductBase;
import com.lunarsky.minipos.model.ui.ProductButtonConfig;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ProductButtonUpdateDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String WINDOW_TITLE = "Product";
	
	private final AppData appData;

	private final ProductButtonConfig buttonConfig;
	private ProductTreeView productTreeView;
	private ChangeListener<TreeItem<ProductBase>> selectedItemChangeListener;
	private boolean wasSaved;
	
	@FXML
	private ScrollPane productScrollPane;
	@FXML
	private Button saveButton;

	
	public ProductButtonUpdateDialog(final Stage parentStage, final ProductButtonConfig buttonConfig) {
		assert(null != parentStage);
		assert(null != buttonConfig);
		
		this.appData = AppData.getInstance();
		this.buttonConfig = buttonConfig;

		UiUtil.createDialog(parentStage,WINDOW_TITLE,this,"ProductButtonUpdateDialog.fxml");
	}
	
	public Stage getStage() {
		return (Stage)getScene().getWindow();
	}
	
	//Can be null if canceled
	public ProductButtonConfig getButtonConfig() {
		return buttonConfig;
	}
	
	@FXML
	private void initialize() {
		initializeMembers();
		initializeControls();
		initializeBindings();
		initializeListeners();
		initializeAsync();
	}
	
	private void initializeMembers() {
		productTreeView = new ProductTreeView(appData);
	}
	
	private void initializeControls() {
	
		productScrollPane.setContent(productTreeView);
	}
	
	private void initializeBindings() {
		//TODO bind to SaveService
		saveButton.disableProperty().bind(Bindings.isNull(getButtonConfig().productProperty()));
	}
	
	private void initializeServices() {
		
	}
	
	private void initializeListeners() {
		
		getStage().setOnCloseRequest((event) -> close());
		
		selectedItemChangeListener = new ChangeListener<TreeItem<ProductBase>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<ProductBase>> observable, TreeItem<ProductBase> oldValue, TreeItem<ProductBase> newValue) {
				handleSelectedProductChanged(newValue);
			}
		};
		
		productTreeView.selectedItemProperty().addListener(selectedItemChangeListener);
	}
	
	private void initializeAsync() {
	
	}
	
	private void handleSelectedProductChanged(TreeItem<ProductBase> productTreeItem) {
		log.debug("handleSelectedProductChanged() {}", productTreeItem);
		
		final ProductBase productBase = productTreeItem.getValue();
		if(productBase instanceof Product) {
			final Product product = (Product)productBase;
			setProduct(product);
		} else {
			setProduct(null);
		}
		
	}
	
	private Product getProduct() {
		return buttonConfig.getProduct();
	}
	
	private void setProduct(final Product product) {
		buttonConfig.setProduct(product);
	}
	
	@FXML
	private void handleSave(ActionEvent event) {
		
		//TODO Service
		Task<ProductButtonConfigDTO> task = new Task<ProductButtonConfigDTO>() {
			final ProductButtonConfigDTO buttonConfig = getButtonConfig().getDTO();
			@Override
			protected ProductButtonConfigDTO call() throws EntityNotFoundException {
				return appData.getServerConnector().saveProductButton(buttonConfig);
			}
			@Override
			protected void succeeded() {
				log.debug("SaveProductButton() Succeeded");
				final ProductButtonConfigDTO configDTO = getValue();
				getButtonConfig().setDTO(configDTO);
				wasSaved = true;
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
	
	public boolean wasSaved() {
		return wasSaved;
	}
	
	private void close() {
		getStage().close();
	}
	
}
