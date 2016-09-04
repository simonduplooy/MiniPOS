package com.lunarsky.minipos.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.ProductButtonConfigDTO;
import com.lunarsky.minipos.model.dto.ProductGroupButtonConfigDTO;
import com.lunarsky.minipos.model.ui.PersistenceId;
import com.lunarsky.minipos.model.ui.ProductButtonConfig;
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

public class ProductButtonGridPane extends GridPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String BUTTON_TEXT_BLANK_BUTTON = "Empty";
	private static final Double BUTTON_PREF_WIDTH = 200.0;
	private static final Double BUTTON_PREF_HEIGHT = 100.0;
	
	private final AppData appData;
	private final boolean editable;
	private PersistenceId activeParentId;
	private List<ProductGroupButton> productGroupButtonList;
	private List<ProductButton> productButtonList;
	
	/**************************************************************************
	 * Constructors
	 **************************************************************************/
	public ProductButtonGridPane(final boolean editable) {
		
		this.appData = AppData.getInstance();
		this.editable = editable;
		
		initialize();
	}
	
	/**************************************************************************
	 * Initialize
	 **************************************************************************/
	private void initialize() {
		initializeControls();
		initializeAsync();
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
		
		buildProductPane();
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
				showGroupButtons();
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
				showProductButtons();
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
	
	/**************************************************************************
	 * EventHandlers
	 **************************************************************************/
	private void handleAddGroup(final PersistenceId parentId) {
		log.debug("handleAddGroup() {}",parentId);
	}
	
	
	/**************************************************************************
	 * Utilities
	 **************************************************************************/
	private void createGroupButtons(final List<ProductGroupButtonConfigDTO> buttonGroupConfigList) {
		assert(null == productGroupButtonList);
		
		productGroupButtonList = new ArrayList<ProductGroupButton>();
		for(ProductGroupButtonConfigDTO configDTO: buttonGroupConfigList) {
			final ProductGroupButtonConfig config = new ProductGroupButtonConfig(configDTO);
			final ProductGroupButton button = new ProductGroupButton(config);
			productGroupButtonList.add(button);
		}
	}
	
	private void showGroupButtons() {
		final List<Node> nodeList = getChildren();
		
		for(ProductGroupButton button: productGroupButtonList) {
			final PersistenceId parentId = button.getConfig().getParentId();
			if(null == activeParentId) {
				if(null == parentId) {
					nodeList.add(button);
				}
			} else {
				if(null != parentId) {
					if(activeParentId.equals(parentId)) {
						nodeList.add(button);
					}
				}
			}
		}
	}
	
	private void createProductButtons(final List<ProductButtonConfigDTO> buttonConfigList) {
		assert(null == productButtonList);
		
		productButtonList = new ArrayList<ProductButton>();
		for(ProductButtonConfigDTO buttonConfig: buttonConfigList) {
			final ProductButtonConfig config = new ProductButtonConfig(buttonConfig);
			final ProductButton button = new ProductButton(config);
			productButtonList.add(button);
		}
	}
	
	private void showProductButtons() {
		final List<Node> nodeList = getChildren();
		
		for(ProductButton button: productButtonList) {
			final PersistenceId parentId = button.getConfig().getParentId();
			if(null == activeParentId) {
				if(null == parentId) {
					nodeList.add(button);
				}
			} else {
				if(null != parentId) {
					if(activeParentId.equals(parentId)) {
						nodeList.add(button);
					}
				}
			}
		}
	}
	
	private void buildProductPane() {
		
		final List<Node> nodeList = getChildren();
		nodeList.clear();

		if(editable) {
			for(int rowIdx=0; rowIdx< UiConst.NO_PRODUCT_BUTTON_ROWS; rowIdx++) {
				for(int columnIdx=0; columnIdx<UiConst.NO_PRODUCT_BUTTON_COLUMNS; columnIdx++) {
						final ProductBlankButton button = new ProductBlankButton(columnIdx,rowIdx);
						nodeList.add(button);
				}
			}
		}
	}
	/**************************************************************************
	 * Inner Classes
	 **************************************************************************/
	private class ProductButtonBase extends Button {
		
		protected ProductButtonBase(final int columnIdx,final int rowIdx) {
			getStyleClass().add("product-button");
			GridPane.setConstraints(this,columnIdx,rowIdx);
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
		
	}
	
	private class ProductGroupButton extends ProductButtonBase {
		
		private final ProductGroupButtonConfig config;

		public ProductGroupButton(final ProductGroupButtonConfig config) {
			super(config.getColumnIndex(),config.getRowIndex());

			this.config = config;
			
			final ContextMenu menu = new ContextMenu();
			final MenuItem addGroupItem = new MenuItem(UiConst.CONTEXT_MENU_ADD_PRODUCT_GROUP);
			addGroupItem.setOnAction((event) -> handleAddGroup(null));
			
			menu.getItems().addAll(addGroupItem);
			setContextMenu(menu);
			
			setText(config.getName());
		}
		
		public ProductGroupButtonConfig getConfig() {
			assert(null != config);
			return config;
		}
		
	}
	
	private class ProductButton extends ProductButtonBase {

		private final ProductButtonConfig config;
		
		public ProductButton(final ProductButtonConfig config) {
			super(config.getColumnIndex(),config.getRowIndex());
			this.config = config;
			setText(config.getProduct().getName());

		}
		
		public ProductButtonConfig getConfig() {
			assert(null != config);
			return config;
		}
	}
	
	private class ProductBlankButton extends ProductButtonBase {

		public ProductBlankButton(final int columnIdx,final int rowIdx) {
			super(columnIdx,rowIdx);
			setText(BUTTON_TEXT_BLANK_BUTTON);
			setOpacity(0.5);
			
			final ContextMenu menu = new ContextMenu();
			final MenuItem addGroupItem = new MenuItem(UiConst.CONTEXT_MENU_ADD_PRODUCT_GROUP);
			addGroupItem.setOnAction((event) -> handleAddGroup(null));
			
			menu.getItems().addAll(addGroupItem);
			setContextMenu(menu);
		}
	}
	

}
