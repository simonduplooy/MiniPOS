package com.lunarsky.minipos.ui;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.ui.Account;
import com.lunarsky.minipos.model.ui.PersistenceId;
import com.lunarsky.minipos.model.ui.Product;
import com.lunarsky.minipos.model.ui.ProductSale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class ProductOrderView extends BorderPane {
	private static final Logger log = LogManager.getLogger();

	private final AppData appData;
	private final Account account;
	private final List<ProductSale> productSales;

	private PersistenceId parentId;

	
	@FXML
	private Label accountLabel;
	@FXML
	private ListView salesListView;
	@FXML
	private Label costLabel;
	@FXML
	private GridPane productGridPane;
	
	public ProductOrderView(final Account account) {
		assert(null != account);
		
		this.appData = AppData.getInstance();
		this.account = account;
		
        productSales = new ArrayList<ProductSale>();
		
        UiUtil.loadRootConstructNode(this,"ProductOrderView.fxml");

	}
		
	@FXML
	private void initialize() {
		initializeControls();
		initializeProductPane();
		initializeAsync();
	}
	
	private void initializeControls() {
		accountLabel.setText(account.getName());
		calculateTotal();
	}
	
	private void initializeProductPane() {
		productGridPane.getChildren().clear();
	}
	
	private void initializeAsync() {
		
	}
	
	@FXML
	private void handleBack(final ActionEvent event) {
		log.debug("handleBack()");
		
	}
	
	private void calculateTotal() {
		Double cost = 0.0;
		for(ProductSale sale: productSales) {
			cost += sale.getCost();
		}
		
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
		final String costText = currencyFormat.format(cost);
		costLabel.setText(costText);
	}
	
	private ProductSale findProductSale(final Product product) {
		for(ProductSale sale: productSales) {
			if(sale.getProduct().equals(product)) {
				return sale;
			}
		}
		return null;
	}
	
	private void close() {
		appData.getViewManager().closeAccountView();
	}
}
