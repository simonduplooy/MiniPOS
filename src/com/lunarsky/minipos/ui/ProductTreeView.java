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
		initializeLayout();
		initializeMembers();
		initializeControls();
		initializeAsync();
	}
	
	private void initializeLayout() {
		
	}
	
	private void initializeMembers() {
		selectedItemProperty = new SimpleObjectProperty<TreeItem<ProductBase>>();
		selectedItemProperty.bind(getSelectionModel().selectedItemProperty());
	}
	
	private void initializeControls() {
		
		final ContextMenu menu = new ContextMenu();
		final MenuItem addGroupItem = new MenuItem("Add Root Group");
		addGroupItem.setOnAction((event) -> handleAddRootGroup());
		final MenuItem addProductItem = new MenuItem("Add Root Product");
		addProductItem.setOnAction((event) -> handleAddRootProduct());
		menu.getItems().addAll(addProductItem,addGroupItem);
		setContextMenu(menu);
		
		setCellFactory((treeView) -> {
			final TreeCell<ProductBase> cell = new TreeCell<ProductBase>() {
				@Override
				protected void updateItem(ProductBase product, boolean empty) {
					super.updateItem(product,empty);
					if(null!=product) {
						setText(product.getName());
						if(product instanceof ProductGroup) {
							final ContextMenu contextMenu = createGroupContextMenu();
							setContextMenu(contextMenu);
						} else if (product instanceof Product){
							final ContextMenu contextMenu = createProductContextMenu();
							setContextMenu(contextMenu);
						}
						
					} else {
						setText("");
						setContextMenu(null);
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
	
	private ContextMenu createProductContextMenu() {
		final ContextMenu contextMenu = new ContextMenu();
		final MenuItem updateItem = new MenuItem("Update");
		updateItem.setOnAction((event) -> handleUpdateProduct());
		final MenuItem deleteItem = new MenuItem("Delete");
		deleteItem.setOnAction((event) -> handleDeleteProduct());
		contextMenu.getItems().addAll(updateItem,deleteItem);
		return contextMenu;
	}

	private ContextMenu createGroupContextMenu() {
		final ContextMenu contextMenu = new ContextMenu();
		final MenuItem addGroupItem = new MenuItem("Add Group");
		addGroupItem.setOnAction((event) -> handleAddGroup());
		final MenuItem addProductItem = new MenuItem("Add Product");
		addProductItem.setOnAction((event) -> handleAddProduct());
		final MenuItem updateItem = new MenuItem("Update");
		updateItem.setOnAction((event) -> handleUpdateGroup());
		final MenuItem deleteItem = new MenuItem("Delete");
		deleteItem.setOnAction((event) -> handleDeleteGroup());
		contextMenu.getItems().addAll(addGroupItem,addProductItem,updateItem,deleteItem);
		return contextMenu;
	}

	//TODO alot of duplicate code here
	private void handleAddRootGroup() {
		log.debug("handleAddRootGroup()");
		final TreeItem<ProductBase> rootItem = getRoot();
		handleAddGroup(rootItem);
	}
	
	private void handleAddGroup() {
		log.debug("handleAddGroup()");
		final TreeItem<ProductBase> selectedTreeItem = selectedItemProperty().getValue();
		handleAddGroup(selectedTreeItem);
	}
	
	private void handleAddGroup(final TreeItem<ProductBase> parentTreeItem) {
		final ProductGroup group = new ProductGroup(parentTreeItem.getValue().getId());
		final ProductGroup updatedGroup = handleUpdateGroup(group);
		if(updatedGroup != group) {
			final TreeItem<ProductBase> newTreeItem = new TreeItem<ProductBase>(updatedGroup);
			parentTreeItem.getChildren().add(newTreeItem);			
		}
	}
	
	private void handleUpdateGroup() {
		log.debug("handleUpdateGroup()");
		final TreeItem<ProductBase> selectedTreeItem = selectedItemProperty().getValue();
		final ProductGroup group = (ProductGroup)selectedTreeItem.getValue();
		final ProductGroup updatedGroup = handleUpdateGroup(group);
		if(updatedGroup != group) {
			selectedTreeItem.setValue(updatedGroup);
		}
	}
	
	private ProductGroup handleUpdateGroup(final ProductGroup group) {
		final Stage stage = (Stage)getScene().getWindow();
		final ProductGroupUpdateDialog dialog = new ProductGroupUpdateDialog(stage,group);
		dialog.getStage().showAndWait();
		final ProductGroup updatedGroup = dialog.getGroup();
		return updatedGroup;
	}
	
	private void handleDeleteGroup() {
		log.debug("handleDeleteGroup()");
		//TODO Async
		final TreeItem<ProductBase> selectedTreeItem = selectedItemProperty().getValue();
		final ProductGroup group = (ProductGroup)selectedTreeItem.getValue();
		appData.getServerConnector().deleteProductGroup(group.getId());
		selectedTreeItem.getParent().getChildren().remove(selectedTreeItem);
	}
	
	private void handleAddRootProduct() {
		log.debug("handleAddRootProduct()");
		final TreeItem<ProductBase> rootItem = getRoot();
		handleAddProduct(rootItem);
	}
	
	private void handleAddProduct() {
		log.debug("handleAddProduct()");
		final TreeItem<ProductBase> selectedTreeItem = selectedItemProperty().getValue();
		handleAddProduct(selectedTreeItem);
	}
	
	private void handleAddProduct(final TreeItem<ProductBase>  parentTreeItem) {

		final Product product = new Product(parentTreeItem.getValue().getId());
		final Product updatedProduct = handleUpdateProduct(product);
		if(updatedProduct != product) {
			final TreeItem<ProductBase> newTreeItem = new TreeItem<ProductBase>(updatedProduct);
			parentTreeItem.getChildren().add(newTreeItem);
		}
	}
	
	private void handleUpdateProduct() {
		log.debug("handleUpdateProduct()");
		final TreeItem<ProductBase> selectedTreeItem = selectedItemProperty().getValue();
		final Product product = (Product)selectedTreeItem.getValue();
		final Product updatedProduct = handleUpdateProduct(product);
		if(updatedProduct != product) {
			selectedTreeItem.setValue(updatedProduct);
		}

	}
	
	private Product handleUpdateProduct(final Product product) {
		final Stage stage = (Stage)getScene().getWindow();
		final ProductUpdateDialog dialog = new ProductUpdateDialog(stage,product);
		dialog.getStage().showAndWait();
		final Product updatedProduct = dialog.getProduct();
		return updatedProduct;
	}
	
	private void handleDeleteProduct() {
		log.debug("handleDeleteProduct()");
		//TODO Async
		final TreeItem<ProductBase> selectedTreeItem = selectedItemProperty().getValue();
		final Product product = (Product)selectedTreeItem.getValue();
		appData.getServerConnector().deleteProduct(product.getId());
		selectedTreeItem.getParent().getChildren().remove(selectedTreeItem);
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
