package com.lunarsky.minipos.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.ProductDTO;
import com.lunarsky.minipos.model.dto.ProductGroupDTO;
import com.lunarsky.minipos.model.ui.Product;
import com.lunarsky.minipos.model.ui.ProductBase;
import com.lunarsky.minipos.model.ui.ProductGroup;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

public class ProductTreeView extends TreeView<ProductBase> {
	private static final Logger log = LogManager.getLogger();

	private final AppData appData;
	private ObjectProperty<TreeItem<ProductBase>> selectedItemProperty;
	
	public ProductTreeView(final AppData appData) {
		assert(null != appData);
		this.appData = appData;
		
		initialize();
	}
	
	private void initialize() {
		initializeMembers();
		initializeControls();
		initializeAsync();
	}
	
	private void initializeMembers() {
		selectedItemProperty = new SimpleObjectProperty<TreeItem<ProductBase>>();
		selectedItemProperty.bind(getSelectionModel().selectedItemProperty());
	}
	
	private void initializeControls() {
		
		final ContextMenu menu = new ContextMenu();
		MenuItem item = new MenuItem("Add Group");
		item.setOnAction((event) -> handleAddGroup());
		menu.getItems().add(item);
		setContextMenu(menu);
		
		//TODO
		this.setPrefWidth(400f);
		
		setCellFactory((treeView) -> {
			final TreeCell<ProductBase> cell = new TreeCell<ProductBase>() {
				@Override
				protected void updateItem(ProductBase product, boolean empty) {
					super.updateItem(product,empty);
					if(null!=product) {
						setText(product.getName());
						if(product instanceof ProductGroup) {
							final ContextMenu contextMenu = new ContextMenu();
							MenuItem addGroupItem = new MenuItem("Add Group");
							addGroupItem.setOnAction((event) -> handleAddGroup());
							MenuItem addProductItem = new MenuItem("Add Product");			
							MenuItem updateItem = new MenuItem("Update");
							MenuItem deleteItem = new MenuItem("Delete");
							contextMenu.getItems().addAll(addGroupItem,addProductItem,updateItem,deleteItem);
							
							setContextMenu(contextMenu);
						}
					} else {
						setText("");
					}
				}
			};
			return cell;
		});

		final ProductRoot root = new ProductRoot("root");
		final TreeItem<ProductBase> rootItem = new TreeItem<ProductBase>(root);
		rootItem.setExpanded(true);
		setRoot(rootItem);
		setShowRoot(false);
		//TODO
		getSelectionModel().select(rootItem);
	}
	
	private void initializeAsync() {
		//TODO Async
		
		final List<TreeItem<ProductBase>> treeList = new ArrayList<TreeItem<ProductBase>>();
		
		List<ProductGroupDTO> groups = appData.getServerConnector().getProductGroups();
		for(ProductGroupDTO groupDTO: groups) {
			final ProductGroup group = new ProductGroup(groupDTO);
			final TreeItem<ProductBase> treeItem = new TreeItem<ProductBase>(group);
			treeList.add(treeItem);
		}
		
		List<ProductDTO> products = appData.getServerConnector().getProducts();
		for(ProductDTO productDTO: products) {
			final Product product = new Product(productDTO);
			final TreeItem<ProductBase> treeItem = new TreeItem<ProductBase>(product);
			treeList.add(treeItem);
		}
		
		addChildren(getRoot(),treeList);
	}
	
	public ObjectProperty<TreeItem<ProductBase>> selectedItemProperty() {
		assert(null != selectedItemProperty);
		return selectedItemProperty;
	}
	
	private void addChildren(TreeItem<ProductBase> parent, final List<TreeItem<ProductBase>> itemList) {
		final PersistenceId parentId = parent.getValue().getId();
		
		for(TreeItem<ProductBase> product: itemList) {
			final PersistenceId productParentId = product.getValue().getParentId();
			if(null == parentId) {
				if(null == productParentId) {
					parent.getChildren().add(product);
					addChildren(product,itemList);
				}
			} else {
				if(parentId.equals(productParentId)) {
					parent.getChildren().add(product);
					addChildren(product,itemList);
				}
			}
		}
	}
	
	private void handleAddGroup() {
		log.debug("handleAddGroup()");
		TreeItem<ProductBase> selectedTreeItem = selectedItemProperty().getValue();
		final Stage stage = (Stage)getScene().getWindow();
		final ProductGroup group = new ProductGroup(selectedTreeItem.getValue().getId());
		final ProductGroupUpdateDialog dialog = new ProductGroupUpdateDialog(appData,stage,group);
		dialog.getStage().showAndWait();
	}
	
	private class ProductRoot extends ProductBase {

		private final String name;
		
		private ProductRoot(final String name) {
			assert(null != name);
			this.name = name;
		}
		
		public String getName() {
			assert(null != name);
			return name;
		}
		
		public PersistenceId getParentId() {
			return null;
		}
		
	}
}
