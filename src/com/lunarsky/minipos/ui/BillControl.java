package com.lunarsky.minipos.ui;

import com.lunarsky.minipos.model.ui.SaleOrder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

public class BillControl extends VBox {

	//TODO should include a ScrollPane
	
	private final ObservableList<SaleOrder> orders;
	
	public BillControl() {
		
		orders = FXCollections.observableArrayList();
		
	}
	
	public void addOrder(final SaleOrder order) {
		orders.add(order);
		final SaleOrderControl orderControl = new SaleOrderControl(order);
		getChildren().add(orderControl);
	}
	
}
