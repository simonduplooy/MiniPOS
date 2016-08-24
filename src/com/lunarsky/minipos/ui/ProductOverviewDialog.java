package com.lunarsky.minipos.ui;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.ProductDTO;

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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProductOverviewDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
		
	private final AppData appData;
	private final Stage stage;
	
	//TODO Remove 
	private ObservableList<ProductDTO> productList;
	private ProductTreeView productTreeView;
	private ProductDTO selectedProduct;
	private ChangeListener<ProductDTO> selectedItemChangeListener;
	
	@FXML
	private ScrollPane productScrollPane;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField priceTextField;
	@FXML
	private Button editButton;
	@FXML
	private Button duplicateButton;
	@FXML
	private Button deleteButton;
	
	
	public ProductOverviewDialog(final AppData appData, final Stage parentStage) {
		assert(null!=appData);
		assert(null!=parentStage);
		
		this.appData = appData;
		
		stage = new Stage();
		stage.initOwner(parentStage);
		stage.initModality(Modality.WINDOW_MODAL);
		
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProductOverviewDialog.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
        	loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
        
        final Scene scene = new Scene(this);
		appData.getViewManager().setStyleSheets(scene);
		stage.setScene(scene);
	}
	
	@FXML
	private void initialize() {
		
		initializeControls();
		initializeBindings();
		initializeListeners();
		initializeAsync();
		
	}

	private void initializeControls() {
		
		final ProductTreeView productTreeView = new ProductTreeView(appData);
		productScrollPane.setContent(productTreeView);
		/*
		productListView.setCellFactory((listCell) -> {
			ListCell<Product> cell = new ListCell<Product>() {
				@Override
				protected void updateItem(Product Product, boolean empty) {
					super.updateItem(Product,empty);
					if(null!=Product) {
						setText(Product.getName());
					} else {
						setText("");
					}
				}
			};
			return cell;
		});
		*/
	}
	
	private void initializeBindings() {
		/*
		editButton.disableProperty().bind(productListView.getSelectionModel().selectedItemProperty().isNull());
		duplicateButton.disableProperty().bind(productListView.getSelectionModel().selectedItemProperty().isNull());
		deleteButton.disableProperty().bind(productListView.getSelectionModel().selectedItemProperty().isNull());
		*/
	}
	
	private void initializeListeners() {
		
		getStage().setOnCloseRequest((event) -> { close(); });
		
		selectedItemChangeListener = new ChangeListener<ProductDTO>() {
			@Override
			public void changed(ObservableValue<? extends ProductDTO> observable, ProductDTO oldValue, ProductDTO newValue) {
				setSelectedProduct(newValue);
			}
		};
		
		//productListView.getSelectionModel().selectedItemProperty().addListener(selectedItemChangeListener);
		
	}
	
	private void releaseListeners() {
		//productListView.getSelectionModel().selectedItemProperty().removeListener(selectedItemChangeListener);		
	}
	
	private void releaseBindings() {
		editButton.disableProperty().unbind();
		duplicateButton.disableProperty().unbind();
		deleteButton.disableProperty().unbind();
	}
	
	private void initializeAsync() {
		
		Task<List<ProductDTO>> task = new Task<List<ProductDTO>>() {
			@Override
			protected List<ProductDTO> call() {
				List<ProductDTO> products = appData.getServerConnector().getProducts();
				return products;
			}
			@Override
			protected void succeeded() {
				log.debug("GetProducts() Succeeded");
				productList = FXCollections.observableList(getValue());
				Collections.sort(productList);
				//productListView.setItems(productList);
			}
			@Override
			protected void failed() {
				log.error("GetProducts() Failed");
				final Throwable throwable = getException();
				log.catching(Level.ERROR,throwable);
				ExceptionDialog.create(AlertType.ERROR,"Could not retrieve Products",throwable);

			}
		};
		
		Thread thread = new Thread(task);
		log.debug("Starting GetProducts() Task {}",thread);
		thread.start();	
	}
	
	public Stage getStage() {
		assert(null!=stage);
		return stage;
	}
	
	@FXML
	private void handleAdd() {
		updateProduct(null);
	}	
	
	@FXML
	private void handleEdit() {
		final ProductDTO product = getSelectedProduct();
		updateProduct(product);
	}

	@FXML
	private void handleDuplicate() {		
		final ProductDTO product = getSelectedProduct().duplicate();
		log.debug("Duplicate Product {}",product);
		updateProduct(product);
	}
	
	private void updateProduct(final ProductDTO product) {
		log.debug("Update Product {}",product);

		final ProductUpdateDialog dialog = new ProductUpdateDialog(appData,getStage(),product);
		dialog.getStage().showAndWait();
		
		final ProductDTO updatedProduct = dialog.getProduct();
		
		//The Product was updated
		if(updatedProduct != product) {
			productList.remove(product);
			productList.add(updatedProduct);
			FXCollections.sort(productList);
			//productListView.getSelectionModel().select(updatedProduct);
		}
	}

	@FXML
	private void handleDelete() {

		Task<Void> task = new Task<Void>() {
			private final ProductDTO product = getSelectedProduct();
			@Override
			protected Void call() throws EntityNotFoundException {
				appData.getServerConnector().deleteProduct(product.getId());
				return null;
			}
			@Override
			protected void succeeded() {
				log.debug("DeleteProduct() Succeeded");
				productList.remove(product);
			}
			@Override
			protected void failed() {
				final Throwable t = getException();
				log.error("DeleteProduct() Failed");
				log.catching(Level.ERROR, t);
				ExceptionDialog.create(AlertType.ERROR, "Could not Delete Product", t).show();
			}
			
		};
		
		//productListView.getSelectionModel().clearSelection();
		
		Thread thread = new Thread(task);
		log.debug("Starting DeleteProduct() Task {}",thread);
		thread.start();
	}
	
	@FXML
	private void handleBack() {
		log.debug("Closing Dialog");
		getStage().close();
	}
	
	private void close() {
		
		releaseBindings();
		releaseListeners();
		
	}
	
	private void setSelectedProduct(ProductDTO product) {
		this.selectedProduct = product;
		if(null == product) {
			nameTextField.setText("");
			priceTextField.setText("");						
		} else {
			nameTextField.setText(product.getName());
			final String priceText = product.getPrice().toString();
			priceTextField.setText(priceText);			
		}
	}

	private ProductDTO getSelectedProduct() {
		return selectedProduct;
	}
		
}
