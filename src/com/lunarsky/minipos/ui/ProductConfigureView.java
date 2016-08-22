package com.lunarsky.minipos.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.Product;
import com.lunarsky.minipos.model.ProductButtonConfig;
import com.lunarsky.minipos.model.ProductButtonGroupConfig;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class ProductConfigureView extends BorderPane implements ProductButtonObserver {
	private static final Logger log = LogManager.getLogger();

	private final AppData appData;
	private final Stage stage;
	
	private PersistenceId parentId;
	private List<ProductButtonGroup> productButtonGroupList;
	private List<ProductButton> productButtonList;
	
	@FXML
	private GridPane productGridPane;
	
	public ProductConfigureView(final AppData appData, final Stage stage) {
		assert(null != appData);
		assert(null != stage);
		
		this.appData = appData;
		this.stage = stage;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProductConfigureView.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
        	loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
	}
		
	@FXML
	private void initialize() {
		initializeControls();
		initializeProductPane();
		initializeAsync();
	}
	
	private void initializeControls() {
	}

	private void initializeProductPane() {
		
		final List<Node> nodeList = productGridPane.getChildren();
		nodeList.clear();
		
		//TODO Only the last Constraints have an Effect
		for(int rowIdx=0; rowIdx< UiConst.NO_PRODUCT_BUTTON_ROWS; rowIdx++) {
			final RowConstraints rowConstraints = productGridPane.getRowConstraints().get(rowIdx);
			for(int columnIdx=0; columnIdx<UiConst.NO_PRODUCT_BUTTON_COLUMNS; columnIdx++) {
				final ColumnConstraints columnConstraints = productGridPane.getColumnConstraints().get(columnIdx);
				final ProductButtonBlank button = new ProductButtonBlank(this,columnIdx,rowIdx);
				rowConstraints.setPrefHeight(button.getPrefHeight());
				columnConstraints.setPrefWidth(button.getPrefWidth());
				nodeList.add(button);
			}
		}
	}
	
	private void initializeAsync() {
		
		//GetProductButtonGroups
		final Task<List<ProductButtonGroupConfig>> buttonGroupTask = new Task<List<ProductButtonGroupConfig>>() {
			@Override
			protected List<ProductButtonGroupConfig> call() {
				final List<ProductButtonGroupConfig> buttonGroupConfigList = appData.getServerConnector().getProductButtonGroups();
				return buttonGroupConfigList;
			}
			@Override
			protected void succeeded() {
				log.debug("getProductButtonGroups() Succeeded");
				final List<ProductButtonGroupConfig>buttonGroupConfigList = getValue();
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
		final Task<List<ProductButtonConfig>> buttonTask = new Task<List<ProductButtonConfig>>() {
			@Override
			protected List<ProductButtonConfig> call() {
				final List<ProductButtonConfig> buttonConfigList = appData.getServerConnector().getProductButtons();
				return buttonConfigList;
			}
			@Override
			protected void succeeded() {
				log.debug("getProductButtons() Succeeded");
				final List<ProductButtonConfig> buttonConfigList = getValue();
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
	
	private void createGroupButtons(final List<ProductButtonGroupConfig> buttonGroupConfigList) {
		assert(null == productButtonGroupList);
		
		productButtonGroupList = new ArrayList<ProductButtonGroup>();
		for(ProductButtonGroupConfig buttonConfig: buttonGroupConfigList) {
			final ProductButtonGroup button = new ProductButtonGroup(this,buttonConfig);
			productButtonGroupList.add(button);
		}
	}
	
	private void showGroupButtons() {
		final List<Node> nodeList = productGridPane.getChildren();
		
		for(ProductButtonGroup buttonGroup: productButtonGroupList) {
			final PersistenceId buttonParentId = buttonGroup.getConfig().getParentId();
			if(null == parentId) {
				if(null == buttonParentId) {
					nodeList.add(buttonGroup);
				}
			} else {
				if(null != buttonParentId) {
					if(parentId.equals(buttonParentId)) {
						nodeList.add(buttonGroup);
					}
				}
			}
		}
	}
	
	private void createProductButtons(final List<ProductButtonConfig> buttonConfigList) {
		assert(null == productButtonList);
		
		productButtonList = new ArrayList<ProductButton>();
		for(ProductButtonConfig buttonConfig: buttonConfigList) {
			final ProductButton button = new ProductButton(this,buttonConfig);
			productButtonList.add(button);
		}
	}
	
	private void showProductButtons() {
		final List<Node> nodeList = productGridPane.getChildren();
		
		for(ProductButton button: productButtonList) {
			final PersistenceId buttonParentId = button.getConfig().getParentId();
			if(null == parentId) {
				if(null == buttonParentId) {
					nodeList.add(button);
				}
			} else {
				if(null != buttonParentId) {
					if(parentId.equals(buttonParentId)) {
						nodeList.add(button);
					}
				}
			}
		}
	}
	
	private void refreshButtons() {
		initializeProductPane();
		showGroupButtons();
		showProductButtons();
	}
	
	@FXML
	private void handleDone(final ActionEvent event) {
		log.debug("handleDone()");
		close();
	}
	
	@FXML
	private void handleBack(final ActionEvent event) {
		log.debug("handleBack()");
		
		if(null == parentId) {
			return;
		}
		
		//Find the parent
		for(ProductButtonGroup buttonGroup: productButtonGroupList) {
			final ProductButtonGroupConfig config = buttonGroup.getConfig();
			final PersistenceId id = config.getId();
			if(parentId.equals(id)) {
				parentId = config.getParentId();
				break;
			}
		}
		
		refreshButtons();
	}
	
	//Implement ProductButtonObserver
	public void createProductButton(final Integer columnIdx, final Integer rowIdx) {
		log.debug("createProductButton()");
		
		final ProductButtonUpdateDialog dialog = new ProductButtonUpdateDialog(appData,stage,parentId,columnIdx,rowIdx);
		dialog.getStage().showAndWait();
		final ProductButtonConfig config = dialog.getButtonConfig();
		
		if(null != config) {
			final ProductButton button = new ProductButton(this,config);
			productButtonList.add(button);
			productGridPane.getChildren().add(button);
		}
	}
	
	public void updateProductButton(final ProductButton button) {
		log.debug("updateProductButton()");
		final ProductButtonConfig config = button.getConfig();
		final ProductButtonUpdateDialog dialog = new ProductButtonUpdateDialog(appData,stage,config);
		dialog.getStage().showAndWait();
		final ProductButtonConfig updatedConfig = dialog.getButtonConfig();
		if(null != updatedConfig) {
			button.setConfig(updatedConfig);
		}
	}
	
	public void productSelected(final Product product) {
		log.debug("productSelected {}", product);
	}
	
	public void deleteProductButton(final ProductButton button) {
		log.debug("deleteProductButton {}",button);

		final Task<Void> task = new Task<Void>() {
			final PersistenceId id = button.getConfig().getId();
			@Override
			protected Void call() {
				log.debug("deleteProductButton() {}",button);
				appData.getServerConnector().deleteProductButton(id);
				return null;
			}
			@Override
			protected void succeeded() {
				log.debug("deleteProductButton() Succeeded");
				productButtonList.remove(button);
				productGridPane.getChildren().remove(button);
			}
			@Override
			protected void failed() {
				final Throwable t = getException();
				log.catching(t);
				throw new RuntimeException(t);
			}
		};
		
		final Thread thread = new Thread(task);
		thread.start();
	}
	
	public void createProductButtonGroup(final Integer columnIdx, final Integer rowIdx) {
		log.debug("createProductButtonGroup()");
		
		final ProductButtonGroupConfig config = new ProductButtonGroupConfig(parentId,"",columnIdx,rowIdx);
		final ProductButtonGroupUpdateDialog dialog = new ProductButtonGroupUpdateDialog(appData,stage,config);
		dialog.getStage().showAndWait();
		final ProductButtonGroupConfig updatedConfig = dialog.getButtonConfig();

		//Cancelled
		if(updatedConfig == config) {
			return;
		}
		
		final ProductButtonGroup button = new ProductButtonGroup(this,updatedConfig);
		productButtonGroupList.add(button);
		productGridPane.getChildren().add(button);
	}
	
	public void updateProductButtonGroup(final ProductButtonGroup buttonGroup) {
		log.debug("updateProductButtonGroup()");
		
		final ProductButtonGroupConfig config = buttonGroup.getConfig();
		final ProductButtonGroupUpdateDialog dialog = new ProductButtonGroupUpdateDialog(appData,stage,config);
		dialog.getStage().showAndWait();
		final ProductButtonGroupConfig updatedConfig = dialog.getButtonConfig();
		//Cancelled
		if(updatedConfig == config) {
			return;
		}
		
		buttonGroup.setConfig(updatedConfig);
	}
	
	public void productButtonGroupSelected(final ProductButtonGroupConfig config) {
		log.debug("productButtonGroupSelected() ",config);
		
		parentId = config.getId();
		refreshButtons();
	}
	
	public void deleteProductButtonGroup(final ProductButtonGroup button) {
		log.debug("deleteProductButtonGroup {}",button);
		
		final Task<Void> task = new Task<Void>() {
			final PersistenceId id = button.getConfig().getId();
			
			@Override
			protected Void call() {
				log.debug("deleteProductButtonGroup() {}",button);
				appData.getServerConnector().deleteProductButtonGroup(id);
				return null;
			}
			@Override
			protected void succeeded() {
				log.debug("deleteProductButtonGroup() Succeeded");
				productButtonGroupList.remove(button);
				productGridPane.getChildren().remove(button);
			}
			@Override
			protected void failed() {
				final Throwable t = getException();
				log.catching(t);
				throw new RuntimeException(t);
			}
		};
		
		final Thread thread = new Thread(task);
		thread.start();
	}
	
	private void close() {
		appData.getViewManager().closeProductConfigureView();
	}
}
