package com.lunarsky.minipos.ui;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.db.hibernate.HibernatePersistenceId;
import com.lunarsky.minipos.model.Account;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.Product;
import com.lunarsky.minipos.model.ProductSale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class ProductOrderView extends BorderPane implements ProductButtonObserver {
	private static final Logger log = LogManager.getLogger();

	private final AppData appData;
	private final Account account;
	private final List<ProductSale> productSales;

	@FXML
	private Label accountLabel;
	@FXML
	private Label costLabel;
	@FXML
	private GridPane productGridPane;
	
	public ProductOrderView(final AppData appData, final Account account) {
		assert(null != appData);
		assert(null != account);
		
		this.appData = appData;
		this.account = account;
		
        productSales = new ArrayList<ProductSale>();
		
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProductOrderView.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
        	loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }        
	}
		
	@FXML
	private void initialize() {
		initializeControls();
		initializeAsync();
	}
	
	private void initializeControls() {
		accountLabel.setText(account.getName());
	}
	
	private void initializeAsync() {
		//TODO
		
		calculateTotal();
	}
	
	@FXML
	private void handleDone(final ActionEvent event) {
		log.debug("handleDone()");
		close();
	}
	
	//Implement ProductButtonObserver
	public void productSelected(final Product product) {
		log.debug("productSelected {}", product);
		
		ProductSale sale = findProductSale(product);
		if(null == sale) {
			sale = new ProductSale(product);
			log.debug("New Sale {}",sale);
			productSales.add(sale);
		} else {
			sale.addSale();
			log.debug("Add to Sale {}",sale);
		}
		
		calculateTotal();
	}
	
	public void createProductButton(final Integer columnIdx, final Integer rowIdx) {
	}
	
	public void createProductButtonGroup(final Integer columnIdx, final Integer rowIdx) {
	}
	
	public void deleteProductButton(final ProductButton button) {
	}
	
	public void deleteProductButtonGroup(final ProductButtonGroup button) {
	}
	
	private void calculateTotal() {
		Double cost = 0.0;
		for(ProductSale sale: productSales) {
			cost += sale.getCost();
		}
		
		final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
		final String costText = decimalFormat.format(cost);
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
