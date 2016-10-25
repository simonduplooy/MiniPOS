package com.lunarsky.minipos.ui;

import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.SaleOrderDTO;
import com.lunarsky.minipos.model.ui.Account;
import com.lunarsky.minipos.model.ui.Product;
import com.lunarsky.minipos.model.ui.ProductSale;
import com.lunarsky.minipos.model.ui.SaleOrder;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.converter.CurrencyStringConverter;

public class SaleView extends BorderPane implements ProductButtonGridPane.Observer {
	private static final Logger log = LogManager.getLogger();

	private final AppData appData;
	private final Account account;

	//These are a bill
	private final ObservableList<SaleOrder> orders;
	private final DoubleProperty totalProperty;
	private NumberBinding totalBinding;
	//
	
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
		this.totalProperty = new SimpleDoubleProperty();
       
        UiUtil.loadRootConstructNode(this,"SaleView.fxml");

	}
		
	@FXML
	private void initialize() {
		initializeMembers();
		initializeControls();
		initializeAsync();
	}
	
	private void initializeMembers() {

		activeOrder = new SaleOrder();
		
		productGridPane = new ProductButtonGridPane(false);
		billControl = new BillControl();
		
	}
	
	
	private void initializeControls() {

		accountLabel.setText(account.getName());

		orderScrollPane.setContent(billControl);
		
		productGridPane.setProductSelectionObserver(this);
		productScrollPane.setContent(productGridPane);

		final CurrencyStringConverter currencyConverter = new CurrencyStringConverter();
		Bindings.bindBidirectional(totalLabel.textProperty(),totalProperty,currencyConverter);
		
		billControl.heightProperty().addListener((ChangeListener<? super Number>)((observable,oldValue,newValue) -> handleOrderControlHeightChanged(newValue)));

	}
	
	private void initializeAsync() {
		//TODO Async
		final List<SaleOrderDTO> saleOrders = appData.getServerConnector().getSaleOrders(account.getId().getDTO());
		
		Collections.sort(saleOrders,(a,b)-> a.getCreationDate().compareTo(b.getCreationDate()));
		
		for(SaleOrderDTO orderDTO: saleOrders) {
			final SaleOrder order = new SaleOrder(orderDTO);
			addOrder(order);
		}
		
		addOrder(activeOrder);
	}
	
	private Stage getStage() {
		return (Stage)getScene().getWindow();
	}
	
	//Implement ProductSelectionObserver
	public void handleProductSelected(final Product product) {
		log.debug("handleProductSelected() {}",product);

		ProductSale sale = activeOrder.getProductSale(product);
		if(null != sale) {
			sale.increaseCount(1);
		} else {
			sale = new ProductSale(product,1);
			activeOrder.addSale(sale);
		}
	}

	final void addOrder(final SaleOrder order) {
		
		orders.add(order);
		
		if(null == totalBinding) {
			totalBinding = order.totalProperty().add(0.0);
		} else {
			totalBinding = totalBinding.add(order.totalProperty());
		}
		
		totalProperty.bind(totalBinding);
		
		billControl.addOrder(order);
	}

	
	private void handleOrderControlHeightChanged(final Number number) {
		orderScrollPane.setVvalue(orderScrollPane.getVmax());
	}
	
	@FXML
	private void handlePay() {
		log.debug("handlePay()");
		
		final PayDialog payDialog = new PayDialog(getStage(),totalProperty.getValue());
		payDialog.getStage().showAndWait();
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
		if(!activeOrder.getSales().isEmpty()) {
			//TODO Async
			final SaleOrderDTO orderDTO = activeOrder.getDTO();
			appData.getServerConnector().addSaleOrder(account.getId().getDTO(),orderDTO);
		}
		close();
	}
	
	private void close() {
		appData.getViewManager().closeSaleView();
	}
}
