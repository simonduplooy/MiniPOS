package com.lunarsky.minipos.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.AccountDTO;
import com.lunarsky.minipos.model.dto.ProductButtonConfigDTO;
import com.lunarsky.minipos.model.dto.ProductGroupButtonConfigDTO;
import com.lunarsky.minipos.model.dto.ProductSaleDTO;
import com.lunarsky.minipos.model.ui.Product;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class ProductOrderView extends BorderPane implements ProductButtonObserver {
	private static final Logger log = LogManager.getLogger();

	private final AppData appData;
	private final AccountDTO account;
	private final List<ProductSaleDTO> productSales;

	private PersistenceId parentId;
	private List<ProductButtonGroup> productButtonGroupList;
	private List<ProductButton> productButtonList;
	
	@FXML
	private Label accountLabel;
	@FXML
	private ListView salesListView;
	@FXML
	private Label costLabel;
	@FXML
	private GridPane productGridPane;
	
	public ProductOrderView(final AccountDTO account) {
		assert(null != account);
		
		this.appData = AppData.getInstance();
		this.account = account;
		
        productSales = new ArrayList<ProductSaleDTO>();
		
        UiUtil.loadRootConstructNode(this,"ProductOrderView.fxml");

	}
		
	@FXML
	private void initialize() {
		initializeControls();
		initializeProductPane();
		initializeAsync();
	}
	
	private void initializeControls() {
		accountLabel.setText(account.getName());
		calculateTotal();
	}
	
	private void initializeProductPane() {
		productGridPane.getChildren().clear();
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
	
	private void createGroupButtons(final List<ProductGroupButtonConfigDTO> buttonGroupConfigList) {
		assert(null == productButtonGroupList);
		
		productButtonGroupList = new ArrayList<ProductButtonGroup>();
		for(ProductGroupButtonConfigDTO buttonConfig: buttonGroupConfigList) {
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
	
	private void createProductButtons(final List<ProductButtonConfigDTO> buttonConfigList) {
		assert(null == productButtonList);
		
		productButtonList = new ArrayList<ProductButton>();
		for(ProductButtonConfigDTO buttonConfig: buttonConfigList) {
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
	private void handleBack(final ActionEvent event) {
		log.debug("handleBack()");
		
		if(null == parentId) {
			close();
			return;
		}
		
		//Find the parent
		for(ProductButtonGroup buttonGroup: productButtonGroupList) {
			final ProductGroupButtonConfigDTO config = buttonGroup.getConfig();
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
	}
	
	public void productSelected(final Product product) {
		log.debug("productSelected {}", product);
		
		ProductSaleDTO sale = findProductSale(product);
		if(null == sale) {
			sale = new ProductSaleDTO(product);
			log.debug("New Sale {}",sale);
			productSales.add(sale);
		} else {
			sale.addSale();
			log.debug("Add to Sale {}",sale);
		}
		
		calculateTotal();
	}
	
	public void updateProductButton(final ProductButton button) {
	}
	
	public void deleteProductButton(final ProductButton button) {
	}
	
	public void createProductButtonGroup(final Integer columnIdx, final Integer rowIdx) {
	}
	
	public void updateProductButtonGroup(final ProductButtonGroup button) {
	}
	
	public void productButtonGroupSelected(final ProductGroupButtonConfigDTO config) {
		log.debug("productButtonGroupSelected() {}",config);
		
		parentId = config.getId();
		refreshButtons();
	}
	
	public void deleteProductButtonGroup(final ProductButtonGroup button) {
	}
	
	private void calculateTotal() {
		Double cost = 0.0;
		for(ProductSaleDTO sale: productSales) {
			cost += sale.getCost();
		}
		
		final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
		final String costText = decimalFormat.format(cost);
		costLabel.setText(costText);
	}
	
	private ProductSaleDTO findProductSale(final Product product) {
		for(ProductSaleDTO sale: productSales) {
			if(sale.getProduct().equals(product)) {
				return sale;
			}
		}
		return null;
	}
	
	private void close() {
		appData.getViewManager().closeAccountView();
	}
}
