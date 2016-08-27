package com.lunarsky.minipos.ui;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.ui.Product;
import com.lunarsky.minipos.model.ui.ProductBase;
import com.lunarsky.minipos.model.ui.ProductGroup;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProductOverviewDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String WINDOW_TITLE = "Products";
	
	private final AppData appData;
	private ObjectProperty<TreeItem<ProductBase>> selectedTreeItemProperty;
	private ObservableList<ProductBase> productList;
	private ProductTreeView productTreeView;
	
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
	
	
	public ProductOverviewDialog(final Stage parentStage) {
		assert(null!=parentStage);
		
		this.appData = AppData.getInstance();
		
		final Stage stage = UiUtil.createDialogStage(parentStage,WINDOW_TITLE); 
		final Scene scene = new Scene(this);
		stage.setScene(scene);
		UiUtil.loadRootConstructNode(this,"ProductOverviewDialog.fxml");

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
		productList = FXCollections.observableArrayList();
	}
	
	private void initializeControls() {
		
		productTreeView = new ProductTreeView(appData);
		productScrollPane.setContent(productTreeView);
		


	}
	
	private void initializeBindings() {
		
		selectedTreeItemProperty =  productTreeView.selectedItemProperty();
		
		/*
		editButton.disableProperty().bind(productListView.getSelectionModel().selectedItemProperty().isNull());
		duplicateButton.disableProperty().bind(productListView.getSelectionModel().selectedItemProperty().isNull());
		deleteButton.disableProperty().bind(productListView.getSelectionModel().selectedItemProperty().isNull());
		*/
	}
	
	private void initializeListeners() {
		
		getStage().setOnCloseRequest((event) -> close());
		/*
		selectedItemChangeListener = new ChangeListener<ProductDTO>() {
			@Override
			public void changed(ObservableValue<? extends ProductDTO> observable, ProductDTO oldValue, ProductDTO newValue) {
				setSelectedProduct(newValue);
			}
		};
		*/
		//productListView.getSelectionModel().selectedItemProperty().addListener(selectedItemChangeListener);
		
	}
	
	private void initializeAsync() {
	
	}

	private void releaseListeners() {
		//productListView.getSelectionModel().selectedItemProperty().removeListener(selectedItemChangeListener);		
	}
	
	private void releaseBindings() {
		editButton.disableProperty().unbind();
		duplicateButton.disableProperty().unbind();
		deleteButton.disableProperty().unbind();
	}
	

	public Stage getStage() {
		return (Stage)getScene().getWindow();
	}
	
	private List<ProductBase> getProductList() {
		assert(null != productList);
		return productList;
	}
	
	@FXML
	private void handleAdd() {
		final ProductBase base = selectedTreeItemProperty.getValue().getValue();
		if(base instanceof ProductGroup) {
			final ProductGroup group = (ProductGroup)base;
			final PersistenceId parentId = group.getId();
			final ProductGroup newGroup = new ProductGroup(parentId);
			updateProductGroup(newGroup);
		}
	}	
	
	@FXML
	private void handleEdit() {
		final ProductBase base = selectedTreeItemProperty.getValue().getValue();
		if(base instanceof ProductGroup) {
			final ProductGroup group = (ProductGroup)base;
			updateProductGroup(group);
		}
	}

	@FXML
	private void handleDuplicate() {
		/*
		final ProductDTO product = getSelectedProduct().duplicate();
		log.debug("Duplicate Product {}",product);
		updateProduct(product);
		*/
	}
	
	private void updateProduct(final Product product) {
		log.debug("updateProduct() {}",product);

		final ProductUpdateDialog dialog = new ProductUpdateDialog(getStage(),product);
		dialog.getStage().showAndWait();
		
		final Product updatedProduct = dialog.getProduct();
		
		//The Product was updated
		if(updatedProduct != product) {
			//productTreeView.getSelectionModel().select();
		}
	}

	private void updateProductGroup(final ProductGroup group) {
		log.debug("updateProductGroup() {}",group);

		final ProductGroupUpdateDialog dialog = new ProductGroupUpdateDialog(getStage(),group);
		dialog.getStage().showAndWait();
		
		final ProductGroup updatedGroup = dialog.getGroup();
		
		//The Product was updated
		/*
		if(updatedProduct != product) {
			//productListView.getSelectionModel().select(updatedProduct);
		}
		*/
	}
	@FXML
	private void handleDelete() {
		/*
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
		*/
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

	
	
}
