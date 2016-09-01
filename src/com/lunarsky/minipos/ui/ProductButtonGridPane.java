package com.lunarsky.minipos.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.ProductButtonConfigDTO;
import com.lunarsky.minipos.model.dto.ProductGroupButtonConfigDTO;
import com.lunarsky.minipos.model.ui.ProductButtonConfig;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class ProductButtonGridPane extends GridPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String BUTTON_TEXT_BLANK_BUTTON = "Empty";
	
	private final AppData appData;
	private List<ProductGroupButton> productGroupButtonList;
	private List<ProductButton> productButtonList;
	
	/**************************************************************************
	 * Constructors
	 **************************************************************************/
	public ProductButtonGridPane() {
		
		this.appData = AppData.getInstance();
		UiUtil.loadRootConstructNode(this,"ProductGridPane.fxml");
	}
	
	/**************************************************************************
	 * Constructors
	 **************************************************************************/
	@FXML
	private void initialize() {
		initializeAsync();
	}
	
	private void initializeAsync() {
		//GetProductButtonGroups
		final Task<List<ProductGroupButtonConfigDTO>> buttonGroupTask = new Task<List<ProductGroupButtonConfigDTO>>() {
			@Override
			protected List<ProductGroupButtonConfigDTO> call() {
				final List<ProductGroupButtonConfigDTO> buttonGroupConfigList = appData.getServerConnector().getProductButtonGroups();
				return buttonGroupConfigList;
			}
			@Override
			protected void succeeded() {
				log.debug("getProductButtonGroups() Succeeded");
				final List<ProductGroupButtonConfigDTO>buttonGroupConfigList = getValue();
				createGroupButtons(buttonGroupConfigList);
				showGroupButtons();
			}
			@Override
			protected void failed() {
				log.debug("getProductButtonGroups() Failed");
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
			}
		};
		
	final Thread buttonThread = new Thread(buttonTask);
	buttonThread.start();
	}
	
	/**************************************************************************
	 * Utilities
	 **************************************************************************/
	private void createGroupButtons(final List<ProductGroupButtonConfigDTO> buttonGroupConfigList) {
		assert(null == productGroupButtonList);
		
		productGroupButtonList = new ArrayList<ProductGroupButton>();
		for(ProductGroupButtonConfigDTO buttonConfig: buttonGroupConfigList) {
			final ProductGroupButton button = new ProductGroupButton(buttonConfig);
			productGroupButtonList.add(button);
		}
	}
	
	private void showGroupButtons() {
		final List<Node> nodeList = getChildren();
		
		//TODO Fucked this up fix parentIds
		for(ProductGroupButton button: productGroupButtonList) {
			final PersistenceId parentId = button.getConfig().getParentId();
			if(null == parentId) {
				if(null == parentId) {
					nodeList.add(button);
				}
			} else {
				if(null != parentId) {
					if(parentId.equals(parentId)) {
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
		
		//TODO Fucked this up fix parentIds
		for(ProductButton button: productButtonList) {
			final PersistenceId parentId = button.getConfig().getParentId();
			if(null == parentId) {
				if(null == parentId) {
					nodeList.add(button);
				}
			} else {
				if(null != parentId) {
					if(parentId.equals(parentId)) {
						nodeList.add(button);
					}
				}
			}
		}
	}
	
	/**************************************************************************
	 * Inner Classes
	 **************************************************************************/
	// TODO NEED ProductButtonConfig ui class
	private class ProductGroupButton extends Button {
		
		private final ProductGroupButtonConfigDTO config;

		public ProductGroupButton(final ProductGroupButtonConfigDTO config) {
			assert(null != config);
			this.config = config;
		}
		
		public ProductGroupButtonConfigDTO getConfig() {
			assert(null != config);
			return config;
		}
		
	}
	
	private class ProductButton extends Button {

		private final ProductButtonConfig config;
		
		public ProductButton(final ProductButtonConfig config) {
			assert(null != config);
			this.config = config;
		}
		
		public ProductButtonConfig getConfig() {
			assert(null != config);
			return config;
		}
	}
	
	private class ProductBlankButton extends Button {

		public ProductBlankButton() {
			setText(BUTTON_TEXT_BLANK_BUTTON);
		}
	}
	

}
