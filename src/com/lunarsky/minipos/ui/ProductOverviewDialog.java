package com.lunarsky.minipos.ui;

import java.text.NumberFormat;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.ui.Product;
import com.lunarsky.minipos.model.ui.ProductBase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ProductOverviewDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String WINDOW_TITLE = "Products";
	
	private final AppData appData;
	private ObservableList<ProductBase> productList;
	private ProductTreeView productTreeView;
	
	@FXML
	private ScrollPane productScrollPane;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField priceTextField;
	
	
	public ProductOverviewDialog(final Stage parentStage) {
		assert(null!=parentStage);
		
		this.appData = AppData.getInstance();
		
		UiUtil.createDialog(parentStage,WINDOW_TITLE,this,"ProductOverviewDialog.fxml");

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

	}
	
	private void initializeListeners() {
		
		getStage().setOnCloseRequest((event) -> close());

		productTreeView.selectedItemProperty().addListener((observable,oldValue,newValue)->handleSelectedProductChanged(newValue));
		
	}
	
	private void initializeAsync() {
	
	}

	private void releaseListeners() {

	}
	
	private void releaseBindings() {

	}
	

	public Stage getStage() {
		return (Stage)getScene().getWindow();
	}
	
	private List<ProductBase> getProductList() {
		assert(null != productList);
		return productList;
	}
	
	private void handleSelectedProductChanged(final TreeItem<ProductBase> selectedTreeItem) {
		assert(null != selectedTreeItem);
		final ProductBase productBase = selectedTreeItem.getValue();
		log.debug("handleSelectedProductCanged() {}",productBase);
		nameTextField.setText(productBase.getName());
		if(productBase instanceof Product) {
			final Product product = (Product)productBase;
			final Double price = product.getPrice();
			final String priceText = NumberFormat.getCurrencyInstance().format(price);
			priceTextField.setText(priceText);
		} else {
			priceTextField.setText("");
		}
	}
	
	@FXML
	private void handleAddRootGroup() {
		productTreeView.handleAddRootGroup();
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
