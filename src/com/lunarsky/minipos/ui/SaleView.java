package com.lunarsky.minipos.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.SaleOrderDTO;
import com.lunarsky.minipos.model.ui.Account;
import com.lunarsky.minipos.model.ui.Product;
import com.lunarsky.minipos.model.ui.ProductSale;
import com.lunarsky.minipos.model.ui.Sale;
import com.lunarsky.minipos.model.ui.SaleOrder;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.util.converter.CurrencyStringConverter;

public class SaleView extends BorderPane implements ProductButtonGridPane.Observer {
	private static final Logger log = LogManager.getLogger();

	private final AppData appData;
	private final Account account;
	private final ObservableList<SaleOrder> orders;
	private SaleOrder activeOrder;

	@FXML
	private Label accountLabel;
	@FXML
	private Label totalLabel;
	@FXML 
	private ScrollPane orderScrollPane;
	@FXML
	private ScrollPane productScrollPane;
	
	private BillControl billControl;
	private ProductButtonGridPane productGridPane;
	
	public SaleView(final Account account) {
		assert(null != account);
		
		this.account = account;
		this.appData = AppData.getInstance();
		this.orders = FXCollections.observableArrayList();
       
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
		billControl = new BillControl();
		//TODO
		//orderControl.heightProperty().addListener((ChangeListener<? super Number>)((observable,oldValue,newValue) -> handleOrderControlHeightChanged(newValue)));
		
	}
	
	
	private void initializeControls() {

		accountLabel.setText(account.getName());

		orderScrollPane.setContent(billControl);
		
		productGridPane.setProductSelectionObserver(this);
		productScrollPane.setContent(productGridPane);

		final CurrencyStringConverter currencyConverter = new CurrencyStringConverter();
		Bindings.bindBidirectional(totalLabel.textProperty(),order.totalProperty(),currencyConverter);	
	}
	
	private void initializeAsync() {
		//TODO Async
		final List<SaleOrderDTO> saleOrders = appData.getServerConnector().getSaleOrders(account.getId().getDTO());
		for(SaleOrderDTO orderDTO: saleOrders) {
			final SaleOrder order = new SaleOrder(orderDTO);
			//TODO this should be a bill
			for(Sale sale: order.getSales()) {
				this.order.addSale(sale);				
			}
		}
		
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
		if(!order.getSales().isEmpty()) {
			//TODO Async
			final SaleOrderDTO orderDTO = order.getDTO();
			appData.getServerConnector().addSaleOrder(account.getId().getDTO(),orderDTO);
		}
		close();
	}
	
	private void close() {
		appData.getViewManager().closeSaleView();
	}
}
