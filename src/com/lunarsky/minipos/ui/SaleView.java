package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.SaleOrderDTO;
import com.lunarsky.minipos.model.ui.Account;
import com.lunarsky.minipos.model.ui.Product;
import com.lunarsky.minipos.model.ui.ProductSale;
import com.lunarsky.minipos.model.ui.SaleOrder;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.util.converter.CurrencyStringConverter;

public class SaleView extends BorderPane implements ProductButtonGridPane.Observer {
	private static final Logger log = LogManager.getLogger();

	private final AppData appData;
	private final Account account;
	private SaleOrder order;

	@FXML
	private Label accountLabel;
	@FXML
	private Label totalLabel;
	@FXML 
	private ScrollPane orderScrollPane;
	@FXML
	private ScrollPane productScrollPane;
	
	private SaleOrderControl orderControl;
	private ProductButtonGridPane productGridPane;
	
	public SaleView(final Account account) {
		assert(null != account);
		
		this.account = account;
		this.appData = AppData.getInstance();
       
        UiUtil.loadRootConstructNode(this,"SaleView.fxml");

	}
		
	@FXML
	private void initialize() {
		initializeMembers();
		initializeControls();
		initializeAsync();
	}
	
	private void initializeMembers() {

		order = new SaleOrder();
		
		productGridPane = new ProductButtonGridPane(false);
		orderControl = new SaleOrderControl(order);
		orderControl.heightProperty().addListener((ChangeListener<? super Number>)((observable,oldValue,newValue) -> handleOrderControlHeightChanged(newValue)));
		
	}
	
	
	private void initializeControls() {

		accountLabel.setText(account.getName());

		orderScrollPane.setContent(orderControl);
		
		productGridPane.setProductSelectionObserver(this);
		productScrollPane.setContent(productGridPane);

		final CurrencyStringConverter currencyConverter = new CurrencyStringConverter();
		Bindings.bindBidirectional(totalLabel.textProperty(),order.totalProperty(),currencyConverter);	
	}
	
	private void initializeAsync() {
		
	}
	
	//Implement ProductSelectionObserver
	public void handleProductSelected(final Product product) {
		log.debug("handleProductSelected() {}",product);

		ProductSale sale = order.getProductSale(product);
		if(null != sale) {
			sale.increaseCount(1);
		} else {
			sale = new ProductSale(product,1);
			order.addSale(sale);
		}
	}
	
	private void handleOrderControlHeightChanged(final Number number) {
		orderScrollPane.setVvalue(orderScrollPane.getVmax());
	}
	
	@FXML
	private void handleBack() {
		log.debug("handleBack()");
		final boolean backHandled = productGridPane.handleBack();
		if(!backHandled) {
			handleDone();	
		}		
	}
	
	@FXML
	private void handleDone() {
		log.debug("handleDone()");
		saveOrder();
	}
	
	private void saveOrder() {
		//TODO Async
		final SaleOrderDTO dto = order.getDTO();
		close();
	}
	
	private void close() {
		appData.getViewManager().closeSaleView();
	}
}
