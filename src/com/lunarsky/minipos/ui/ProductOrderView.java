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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

public class ProductOrderView extends BorderPane implements ProductButtonGridPane.ProductSelectionObserver {
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
	private ScrollPane productScrollPane;
	private ProductButtonGridPane productGridPane;
	
	public ProductOrderView(final Account account) {
		assert(null != account);
		
		this.account = account;
		this.appData = AppData.getInstance();
        productSales = new ArrayList<ProductSale>();
        
        UiUtil.loadRootConstructNode(this,"ProductOrderView.fxml");

	}
		
	@FXML
	private void initialize() {
		initializeMembers();
		initializeControls();
		initializeAsync();
	}
	
	private void initializeMembers() {
		productGridPane = new ProductButtonGridPane(false);
		productGridPane.setProductSelectionObserver(this);
	}
	
	
	private void initializeControls() {

		productScrollPane.setContent(productGridPane);

		accountLabel.setText(account.getName());
		calculateTotal();
	}
	
	private void initializeAsync() {
		
	}
	
	//Implement ProductSelectionObserver
	public void handleProductSelected(final Product product) {
		log.debug("handleProductSelected() {}",product);
		final ProductSale sale = findProductSale(product);
		sale.increaseProductCount(1);
		calculateTotal();
	}
	
	@FXML
	private void handleBack(final ActionEvent event) {
		log.debug("handleBack()");
		final boolean backHandled = productGridPane.handleBack();
		if(!backHandled) {
			close();	
		}		
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

		ProductSale sale = null;
		
		for(ProductSale existingSale: productSales) {
			if(existingSale.getProduct().equals(product)) {
				sale = existingSale;
				break;
			}
		}
		
		if(null == sale) {
			sale = new ProductSale(product,0);
			productSales.add(sale);
		}
		
		return sale;
	}
	
	private void close() {
		appData.getViewManager().closeAccountView();
	}
}
