package com.lunarsky.minipos.ui;

import java.text.DateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.ui.ProductSale;
import com.lunarsky.minipos.model.ui.Sale;
import com.lunarsky.minipos.model.ui.SaleOrder;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.converter.CurrencyStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;

public class SaleOrderControl extends VBox {
	private static final Logger log = LogManager.getLogger();
	
	private SaleOrder order;
	private final Label dateTimeLabel;
	
	public SaleOrderControl(final SaleOrder saleOrder) {

		getStyleClass().add("sale-order-control");
		
		order = saleOrder;
		order.getSales().addListener((ListChangeListener.Change<? extends Sale> change) -> { handleOrderChanged(change);} );
		
		dateTimeLabel = new Label(DateFormat.getInstance().format(saleOrder.getCreationDate()));
		getChildren().add(dateTimeLabel);

	}
	
	public void handleOrderChanged(ListChangeListener.Change<? extends Sale> change) {
		log.debug("handleOrderChanged()");
		
		while(change.next()) {
			if(change.wasAdded()) {
				final Sale sale = order.getSales().get(change.getFrom());
				if(sale instanceof ProductSale) {
					final SaleControl saleControl = new ProductSaleControl((ProductSale)sale);
					getChildren().add(saleControl);
				}
			}
		}
	}
	
	private class SaleControl extends VBox {
		
		private final Sale sale;
		
		private SaleControl(final Sale sale) {
			this.sale = sale;
		}
		
		public Sale getSale() {
			return sale;
		}
		
	}
	
	private class ProductSaleControl extends SaleControl {
		
		private final DoubleProperty negativeDiscountProperty;
		
		public ProductSaleControl(final ProductSale sale) {
			super(sale);

			negativeDiscountProperty = new SimpleDoubleProperty();
			
			final CurrencyStringConverter currencyConverter = new CurrencyStringConverter();
			
			final VBox productLineVBox = new VBox();
			getChildren().add(productLineVBox);
			productLineVBox.getStyleClass().add("sale-item");
			productLineVBox.setOnContextMenuRequested((event)-> handleProductContextMenuRequested(event));
			
			final Label productDescriptionLabel = new Label();
			productLineVBox.getChildren().add(productDescriptionLabel);
			productDescriptionLabel.setWrapText(true);
			productDescriptionLabel.textProperty().bind(sale.descriptionProperty());
			productDescriptionLabel.setPrefHeight(USE_COMPUTED_SIZE);
			productDescriptionLabel.setMinHeight(USE_PREF_SIZE);
			productDescriptionLabel.setMaxHeight(USE_PREF_SIZE);
			productDescriptionLabel.setMaxWidth(Double.MAX_VALUE);
			
			final HBox productLineHBox = new HBox();
			productLineVBox.getChildren().add(productLineHBox);
			productLineHBox.setAlignment(Pos.CENTER_RIGHT);
			productLineHBox.setSpacing(5.0);
	
			final Label productCostLabel = new Label();
			productCostLabel.setMinWidth(USE_PREF_SIZE);
			productCostLabel.setMaxWidth(USE_PREF_SIZE);
			final StringProperty costProperty = new SimpleStringProperty();
			Bindings.bindBidirectional(costProperty,sale.getProduct().priceProperty(),currencyConverter);
			productCostLabel.textProperty().bind(Bindings.concat(sale.countProperty()," @ ",costProperty));
			productLineHBox.getChildren().add(productCostLabel);
			
			final Label productTotalLabel = new Label();
			productTotalLabel.getStyleClass().add("sale-item-subtotal");
			productTotalLabel.setMinWidth(USE_PREF_SIZE);
			productTotalLabel.setMaxWidth(USE_PREF_SIZE);
			Bindings.bindBidirectional(productTotalLabel.textProperty(),sale.subTotalProperty(),currencyConverter);
			productLineHBox.getChildren().add(productTotalLabel);
			
			final HBox discountLineHBox = new HBox();
			getChildren().add(discountLineHBox);
			discountLineHBox.setOnContextMenuRequested((event)-> handleDiscountContextMenuRequested(event));
			
			final Label discountDescriptionLabel = new Label("Discount");
			discountDescriptionLabel.setMaxWidth(Double.MAX_VALUE);
			HBox.setHgrow(discountDescriptionLabel,Priority.ALWAYS);
			discountLineHBox.getChildren().add(discountDescriptionLabel);
			
			final Label discountTotalLabel = new Label();
			discountTotalLabel.getStyleClass().add("sale-item-subtotal");
			discountTotalLabel.setMinWidth(USE_PREF_SIZE);
			discountTotalLabel.setMaxWidth(USE_PREF_SIZE);
			negativeDiscountProperty.bind(sale.discountProperty().multiply(-1.0));
			Bindings.bindBidirectional(discountTotalLabel.textProperty(),negativeDiscountProperty,currencyConverter);
			discountLineHBox.managedProperty().bind(discountLineHBox.visibleProperty());
			discountLineHBox.visibleProperty().bind(Bindings.notEqual(sale.discountProperty(),0.0,0.001));
			discountLineHBox.getChildren().add(discountTotalLabel);
			
		}
		
		private void handleProductContextMenuRequested(final ContextMenuEvent event) {
			final ContextMenu contextMenu = new ContextMenu();
			final MenuItem updateMenuItem = new MenuItem("Product Context Menu");
			contextMenu.getItems().add(updateMenuItem);
			contextMenu.show(getScene().getWindow(),event.getScreenX(),event.getScreenY());
		}
		
		private void handleDiscountContextMenuRequested(final ContextMenuEvent event) {
			final ContextMenu contextMenu = new ContextMenu();
			final MenuItem menuItem = new MenuItem("Discount Context Menu");
			contextMenu.getItems().add(menuItem);
			contextMenu.show(getScene().getWindow(),event.getScreenX(),event.getScreenY());
		}
		
	}
}
