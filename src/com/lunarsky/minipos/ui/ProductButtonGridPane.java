package com.lunarsky.minipos.ui;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.PersistenceIdDTO;
import com.lunarsky.minipos.model.dto.ProductButtonConfigDTO;
import com.lunarsky.minipos.model.dto.ProductGroupButtonConfigDTO;
import com.lunarsky.minipos.model.ui.PersistenceId;
import com.lunarsky.minipos.model.ui.ProductButtonConfig;
import com.lunarsky.minipos.model.ui.ProductButtonConfigBase;
import com.lunarsky.minipos.model.ui.ProductGroupButtonConfig;

import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class ProductButtonGridPane extends GridPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String BUTTON_TEXT_BLANK_BUTTON = "Empty";
	private static final Double BUTTON_PREF_WIDTH = 200.0;
	private static final Double BUTTON_PREF_HEIGHT = 100.0;
	
	private final AppData appData;
	private final boolean editable;
	private final ProductButtonConfigBase rootConfig;
	private ProductButtonConfigBase parentConfig;
	//private List<ProductButtonBase> buttonList;
	
	//TODO this should be a binding to the tasks
	private boolean productButtonAsyncInitializeComplete;
	private boolean groupButtonAsyncInitializeComplete;
	
	/**************************************************************************
	 * Constructors
	 **************************************************************************/
	public ProductButtonGridPane(final boolean editable) {
		
		this.appData = AppData.getInstance();
		this.editable = editable;
		this.rootConfig = new ProductButtonConfigBase();
		
		initialize();
	}
	
	/**************************************************************************
	 * Initialize
	 **************************************************************************/
	private void initialize() {
		initializeMembers();
		initializeControls();
		initializeAsync();
	}
	
	private void initializeMembers() {
		parentConfig = rootConfig;
		//buttonList = new ArrayList<ProductButtonBase>();
	}
	
	private void initializeControls() {
		
		for(int columnIdx=0; columnIdx<UiConst.NO_PRODUCT_BUTTON_COLUMNS; columnIdx++) {
			final ColumnConstraints constraints = new ColumnConstraints();
			constraints.setPrefWidth(BUTTON_PREF_WIDTH);
			constraints.setMinWidth(USE_PREF_SIZE);
			constraints.setMaxWidth(Double.MAX_VALUE);
			constraints.setFillWidth(true);
			constraints.setHgrow(Priority.ALWAYS);
			getColumnConstraints().add(constraints);
		}
		
		for(int rowIdx=0; rowIdx< UiConst.NO_PRODUCT_BUTTON_ROWS; rowIdx++) {
			final RowConstraints constraints = new RowConstraints();
			constraints.setPrefHeight(BUTTON_PREF_HEIGHT);
			constraints.setMinHeight(USE_PREF_SIZE);
			constraints.setMaxHeight(Double.MAX_VALUE);
			constraints.setFillHeight(true);
			constraints.setVgrow(Priority.ALWAYS);
			getRowConstraints().add(constraints);
		}
		
		initializeProductPane();
	}
	
	private void initializeAsync() {
		//GetProductButtonGroups
		final Task<List<ProductGroupButtonConfigDTO>> buttonGroupTask = new Task<List<ProductGroupButtonConfigDTO>>() {
			@Override
			protected List<ProductGroupButtonConfigDTO> call() {
				final List<ProductGroupButtonConfigDTO> buttonGroupConfigList = appData.getServerConnector().getProductGroupButtons();
				return buttonGroupConfigList;
			}
			@Override
			protected void succeeded() {
				log.debug("getProductGroupButtons() Succeeded");
				final List<ProductGroupButtonConfigDTO>buttonGroupConfigList = getValue();
				createGroupButtons(buttonGroupConfigList);
				groupButtonInitializeComplete();
			}
			@Override
			protected void failed() {
				log.debug("getProductGroupButtons() Failed");
				final Throwable throwable = getException();
				log.catching(throwable);
				final ExceptionAlert alert = new ExceptionAlert(AlertType.ERROR,UiConst.UNKNOWN_EXCEPTION,throwable);
				alert.show();
			}
		};
		
		final Thread buttonGroupThread = new Thread(buttonGroupTask);
		buttonGroupThread.start();
		
		//GetProductButtons
		final Task<List<ProductButtonConfigDTO>> buttonTask = new Task<List<ProductButtonConfigDTO>>() {
			@Override
			protected List<ProductButtonConfigDTO> call() {
				final List<ProductButtonConfigDTO> buttonConfigList = appData.getServerConnector().getProductButtons();
				return buttonConfigList;
			}
			@Override
			protected void succeeded() {
				log.debug("getProductButtons() Succeeded");
				final List<ProductButtonConfigDTO> buttonConfigList = getValue();
				createProductButtons(buttonConfigList);
				productButtonInitializeComplete();
			}
			@Override
			protected void failed() {
				log.debug("getProductButtons() Failed");
				final Throwable throwable = getException();
				log.catching(throwable);
				final ExceptionAlert alert = new ExceptionAlert(AlertType.ERROR,UiConst.UNKNOWN_EXCEPTION,throwable);
				alert.show();
			}
		};
		
	final Thread buttonThread = new Thread(buttonTask);
	buttonThread.start();
	
	}
	
	private void productButtonInitializeComplete() {
		productButtonAsyncInitializeComplete = true;
		handleAsyncInitializeComplete();
	}
	
	private void groupButtonInitializeComplete() {
		groupButtonAsyncInitializeComplete = true;
		handleAsyncInitializeComplete();
	}
	
	private void handleAsyncInitializeComplete() {
		if(productButtonAsyncInitializeComplete && groupButtonAsyncInitializeComplete) {
			showButtons(getParentConfig().getId());
		}
	}
	
	/**************************************************************************
	 * Getters & Setters
	 **************************************************************************/
	public Stage getStage() {
		return (Stage) getScene().getWindow();
	}
	
	private ProductButtonConfigBase getParentConfig() {
		return parentConfig;
	}
	
	private void setParentConfig(final ProductButtonConfigBase config) {
		this.parentConfig = config;
	}
	
	/**************************************************************************
	 * EventHandlers
	 **************************************************************************/
	public boolean handleBack() {
		
		final ProductButtonConfigBase parentConfig = getParentConfig();
		
		//At the top of the tree
		if(!parentConfig.getId().hasId()) {
			return false;
		}
		
		showButtons(parentConfig.getParentId());
		return true;
	}

	private void handleAddGroup(final ProductButtonConfigBase config) {
		log.debug("handleAddGroup() {}",config);
		
		
		ProductGroupButtonConfig groupConfig = new ProductGroupButtonConfig(getParentConfig().getId(),
				"",
				config.getColumnIndex(),
				config.getRowIndex());
		
		final ProductGroupButtonUpdateDialog dialog = new ProductGroupButtonUpdateDialog(getStage(),groupConfig);
		dialog.getStage().showAndWait();
		
		if(dialog.wasSaved()) {
			groupConfig = dialog.getButtonConfig();
			final ProductGroupButton button = new ProductGroupButton(editable,groupConfig);
			button.setVisible(true);
			getChildren().add(button);
		}
	}
	

	private void handleAddProduct(final ProductButtonConfigBase config) {
		log.debug("handleAddProduct() {}",config);
		
		ProductButtonConfig buttonConfig = new ProductButtonConfig(getParentConfig().getId(),
				null,
				config.getColumnIndex(),
				config.getRowIndex());
		
		final ProductButtonUpdateDialog dialog = new ProductButtonUpdateDialog(getStage(),buttonConfig);
		dialog.getStage().showAndWait();
		
		if(dialog.wasSaved()) {
			buttonConfig = dialog.getButtonConfig();
			final ProductButton button = new ProductButton(editable,buttonConfig);
			//buttonList.add(button);
			button.setVisible(true);
			getChildren().add(button);
		}
	}
	
	private void handleDeleteButton(final ProductButtonBase button) {
		log.debug("handleDeleteButton() {}",button);
		
		final Task<Void> task = new Task<Void>() {
			private final PersistenceIdDTO idDTO = button.getConfig().getId().getDTO();
			
			@Override 
			protected Void call() {
				if(button instanceof ProductGroupButton) {
					appData.getServerConnector().deleteProductGroupButton(idDTO);
				} else if(button instanceof ProductButton) {
					appData.getServerConnector().deleteProductButton(idDTO);
				} else {
					throw new RuntimeException("Unknown Button Instance");
				}
				return null;
			}
			@Override
			protected void succeeded() {
				log.debug("handleDeleteButton() Succeeded");
				//buttonList.remove(button);
				getChildren().remove(button);
			}
			@Override
			protected void failed() {
				log.debug("handleDeleteButton() Failed");
				final Throwable throwable = getException();
				log.catching(throwable);
				final ExceptionAlert alert = new ExceptionAlert(AlertType.ERROR,UiConst.UNKNOWN_EXCEPTION,throwable);
				alert.showAndWait();
			}
		};
		
		final Thread thread = new Thread(task);
		thread.start();
	}
	
	private void handleGroupSelected(final ProductButtonConfigBase config) {
		log.debug("handleGroupSelected() {}",config);
		showButtons(config.getId());
	}
	
	/**************************************************************************
	 * Utilities
	 **************************************************************************/
	//TODO the following two are called when their tasks complete but from the same thread.
	//Is buttonList.add() safe, or can the thread be interrupted?
	private void createGroupButtons(final List<ProductGroupButtonConfigDTO> buttonGroupConfigList) {
		log.debug("createGroupButtons() >");

		for(ProductGroupButtonConfigDTO configDTO: buttonGroupConfigList) {
			final ProductGroupButtonConfig config = new ProductGroupButtonConfig(configDTO);
			final ProductGroupButton button = new ProductGroupButton(editable,config);
			log.debug("Adding {}",button);
			getChildren().add(button);
			//buttonList.add(button);
		}
		
		log.debug("createGroupButtons() <");
	}
	
	private void createProductButtons(final List<ProductButtonConfigDTO> buttonConfigList) {
		log.debug("createProductButtons() >");
		
		for(ProductButtonConfigDTO buttonConfig: buttonConfigList) {
			final ProductButtonConfig config = new ProductButtonConfig(buttonConfig);
			final ProductButton button = new ProductButton(editable,config);
			log.debug("Adding {}",button);
			getChildren().add(button);
			//buttonList.add(button);
		}
		
		log.debug("createProductButtons() <");
	}
	
	private void showButtons(final PersistenceId parentId) {
		
		ProductButtonConfigBase newParentConfig = null;
		
		final List<Node> nodeList = getChildren();
		
		for(Node node: nodeList) {
			final ProductButtonBase button = (ProductButtonBase)node;
			if(button instanceof ProductBlankButton) {
				continue;
			}
			
			final ProductButtonConfigBase config = button.getConfig();
			final PersistenceId configId = config.getId();
			final PersistenceId configParentId = config.getParentId();
			
			if(parentId.equals(configId)) {
				assert(null == newParentConfig);
				newParentConfig = config;
			}
			
			if(parentId.equals(configParentId)) {
				button.setVisible(true);
			} else {
				button.setVisible(false);
			}
		}
		
		if(null == newParentConfig) {
			newParentConfig = rootConfig;
		}
		
		setParentConfig(newParentConfig);
	}
	
	private void initializeProductPane() {
		
		final List<Node> nodeList = getChildren();
		nodeList.clear();

		if(editable) {
			for(int rowIdx=0; rowIdx< UiConst.NO_PRODUCT_BUTTON_ROWS; rowIdx++) {
				for(int columnIdx=0; columnIdx<UiConst.NO_PRODUCT_BUTTON_COLUMNS; columnIdx++) {
					final ProductButtonConfigBase config = new ProductButtonConfigBase(getParentConfig().getId(),columnIdx,rowIdx);
					final ProductBlankButton button = new ProductBlankButton(editable,config);
					nodeList.add(button);
				}
			}
		}
	}
	
	/**************************************************************************
	 * Inner Classes
	 **************************************************************************/
	private class ProductButtonBase extends Button {
		
		private final boolean editable;
		private final ProductButtonConfigBase config;
		
		protected ProductButtonBase(final boolean editable, final ProductButtonConfigBase config) {
			
			this.editable = editable;
			this.config = config;
			
			setVisible(false);
			
			getStyleClass().add("product-button");
			
			GridPane.setConstraints(this,config.getColumnIndex(),config.getRowIndex());
			GridPane.setFillWidth(this,true);
			GridPane.setFillHeight(this,true);
			
			setPrefWidth(BUTTON_PREF_WIDTH);
			setPrefHeight(BUTTON_PREF_HEIGHT);
			setMinWidth(USE_PREF_SIZE);
			setMinHeight(USE_PREF_SIZE);
			setMaxWidth(Double.MAX_VALUE);
			setMaxHeight(Double.MAX_VALUE);
			setWrapText(true);
		}
		
		protected boolean isEditable() {
			return editable;
		}
		
		protected ProductButtonConfigBase getConfig() {
			return config;
		}
		
	}
	
	private class ProductGroupButton extends ProductButtonBase {
		

		public ProductGroupButton(final boolean editable, final ProductGroupButtonConfig config) {
			super(editable,config);
			
			if(isEditable()) {
				final ContextMenu menu = new ContextMenu();
				final MenuItem deleteItem = new MenuItem(UiConst.CONTEXT_MENU_TEXT_DELETE);
				deleteItem.setOnAction((event) -> handleDeleteButton(this));
				
				menu.getItems().addAll(deleteItem);
				setContextMenu(menu);
			}
			
			textProperty().bind(config.nameProperty());
			
			setOnAction((event) -> handleGroupSelected(getConfig()));
		}
		
		@Override
		public String toString() {
			final ProductGroupButtonConfig config = (ProductGroupButtonConfig)getConfig();
			return String.format("product:[%s] id:[%s] parentId:[%s] :[%d] :[%d]",config.getName(),config.getId(),config.getParentId(),config.getColumnIndex(),config.getRowIndex());
		}
		
	}
	
	private class ProductButton extends ProductButtonBase {

		public ProductButton(final boolean editable, final ProductButtonConfig config) {
			super(editable,config);
			
			if(isEditable()) {
				final ContextMenu menu = new ContextMenu();
				final MenuItem deleteItem = new MenuItem(UiConst.CONTEXT_MENU_TEXT_DELETE);
				deleteItem.setOnAction((event) -> handleDeleteButton(this));
				
				menu.getItems().addAll(deleteItem);
				setContextMenu(menu);
			}
			
			textProperty().bind(config.getProduct().nameProperty());

		}
		
		@Override
		public String toString() {
			final ProductButtonConfig config = (ProductButtonConfig)getConfig();
			return String.format("product:[%s] id:[%s] parentId:[%s] :[%d] :[%d]",config.getProduct().getName(),config.getId(),config.getParentId(),config.getColumnIndex(),config.getRowIndex());
		}

	}
	
	private class ProductBlankButton extends ProductButtonBase {

		public ProductBlankButton(final boolean editable, final ProductButtonConfigBase config) {
			super(editable,config);
			
			setVisible(true);
			setText(BUTTON_TEXT_BLANK_BUTTON);
			setOpacity(0.5);
			
			if(isEditable()) {
				final ContextMenu menu = new ContextMenu();
				
				final MenuItem addGroupItem = new MenuItem(UiConst.CONTEXT_MENU_ADD_PRODUCT_GROUP);
				addGroupItem.setOnAction((event) -> handleAddGroup(getConfig()));
				final MenuItem addProductItem = new MenuItem(UiConst.CONTEXT_MENU_ADD_PRODUCT);
				addProductItem.setOnAction((event) -> handleAddProduct(getConfig()));
				
				menu.getItems().addAll(addGroupItem,addProductItem);
				setContextMenu(menu);
			}
		}
		
		@Override
		public String toString() {
			final ProductButtonConfigBase config = getConfig();
			return String.format("id:[%s] parentId:[%s] :[%d] :[%d]",config.getId(),config.getParentId(),config.getColumnIndex(),config.getRowIndex());
		}
	}
	

}
