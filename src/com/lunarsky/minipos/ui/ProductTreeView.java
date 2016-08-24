package com.lunarsky.minipos.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.ProductDTO;
import com.lunarsky.minipos.model.ui.UiProduct;
import com.lunarsky.minipos.model.ui.UiProductBase;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class ProductTreeView extends TreeView<UiProductBase> {
	private static final Logger log = LogManager.getLogger();

	private final AppData appData;
	private List<UiProductBase> productList;
	
	public ProductTreeView(final AppData appData) {
		if(null == appData) {throw new IllegalArgumentException();}
		this.appData = appData;
		
		initialize();
	}
	
	private void initialize() {
		initializeControls();
		initializeAsync();
	}
	
	private void initializeControls() {
		
		//TODO
		this.setPrefWidth(400f);
		
		setCellFactory((treeView) -> {
			final TreeCell<UiProductBase> cell = new TreeCell<UiProductBase>() {
				@Override
				protected void updateItem(UiProductBase product, boolean empty) {
					super.updateItem(product,empty);
					if(null!=product) {
						setText(product.getName());
					} else {
						setText("");
					}
				}
			};
			return cell;
		});
		
		final UiProductRoot root = new UiProductRoot("root");
		final TreeItem<UiProductBase> rootItem = new TreeItem<UiProductBase>(root);
		rootItem.setExpanded(true);
		setRoot(rootItem);
		setShowRoot(false);
	}
	
	private void initializeAsync() {
		//TODO Async
		
		productList = new ArrayList<UiProductBase>();
		
		List<ProductDTO> products = appData.getServerConnector().getProducts();
		for(ProductDTO product: products) {
			final UiProduct uiProduct = new UiProduct(product);
			productList.add(uiProduct);
			//TODO remove
			final TreeItem<UiProductBase> treeItem = new TreeItem<UiProductBase>(uiProduct);
			getRoot().getChildren().add(treeItem);
			
		}
	}
	
	private class UiProductRoot extends UiProductBase {

		private final String name;
		
		private UiProductRoot(final String name) {
			assert(null != name);
			this.name = name;
		}
		
		public String getName() {
			assert(null != name);
			return name;
		}
		
	}
}
