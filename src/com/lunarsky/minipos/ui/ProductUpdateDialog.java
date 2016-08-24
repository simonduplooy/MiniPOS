package com.lunarsky.minipos.ui;

import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.common.exception.NameInUseException;
import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.ProductDTO;
import com.lunarsky.minipos.ui.validator.CurrencyTextFieldValidator;
import com.lunarsky.minipos.ui.validator.StringTextFieldValidator;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProductUpdateDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private final AppData appData;
	
	@FXML
	private Label nameErrorLabel;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField priceTextField;
	@FXML
	private Button saveButton;
	
	private StringTextFieldValidator nameValidator;
	private CurrencyTextFieldValidator priceValidator;
	
	private Service<ProductDTO> saveService;
	
	private final Stage stage;
	private ProductDTO product;
	 
	// product can be null to create a new Product
	public ProductUpdateDialog(final AppData appData, final Stage parentStage, final ProductDTO product) {
		assert(null != appData);
		assert(null != parentStage);
		
		this.appData = appData;
		this.product = product;

		stage = new Stage();
		stage.initOwner(parentStage);
		stage.initModality(Modality.WINDOW_MODAL);
		
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProductUpdateDialog.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
        	loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
		
		Scene scene = new Scene(this);
		appData.getViewManager().setStyleSheets(scene);
		stage.setScene(scene);

	}
	
	//Can be null if canceled
	public ProductDTO getProduct() {
		return product;
	}
	
	@FXML
	private void initialize() {
		createSaveService();
		initializeControls(getProduct());
	}
	
	private void initializeControls(final ProductDTO product) {
		if(null == product) {
			nameTextField.setText("");
			priceTextField.setText(Double.toString(0.0));
			
		} else {
			nameTextField.setText(product.getName());
			final String priceText = product.getPrice().toString();
			priceTextField.setText(priceText);
		}
		
		nameErrorLabel.managedProperty().bind(nameErrorLabel.visibleProperty());
		clearErrorMessages();
		
		nameValidator = new StringTextFieldValidator(nameTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_TEXTFIELD_LENGTH);
		priceValidator = new CurrencyTextFieldValidator(priceTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_TEXTFIELD_LENGTH);
		
		saveButton.disableProperty().bind(nameValidator.validProperty().and(priceValidator.validProperty()).not().or(saveService.runningProperty()));
	}

	public void createSaveService() {
		saveService = new Service<ProductDTO>() {
			@Override
			protected Task<ProductDTO> createTask() {
				final Task<ProductDTO> task = new Task<ProductDTO>() {
					final ProductDTO product = createProductFromControls();
					@Override
					protected ProductDTO call() throws EntityNotFoundException {
						return appData.getServerConnector().saveProduct(product);
					}
					@Override
					protected void succeeded() {
						log.debug("SaveProduct() Succeeded");
						final ProductDTO updatedProduct = getValue();
						setProduct(updatedProduct);
						getStage().close();
					}
					@Override
					protected void failed() {
						final Throwable t = getException();
						log.debug("SaveProduct() Failed");
						log.catching(Level.ERROR, t);
						if(t instanceof NameInUseException) {
							nameErrorLabel.setVisible(true);
						} else {
							throw new RuntimeException(t);
						}
					}
				};
				
				return task;
			}
		};
	}
	
	public Stage getStage() {
		assert(stage!=null);
		return stage;
	}
	
	private void clearErrorMessages() {
		nameErrorLabel.setVisible(false);
	}
	
	@FXML
	private void handleSave(ActionEvent event) {
		log.debug("handleSave");
		saveService.restart();
	}

	@FXML
	private void handleCancel(ActionEvent event) {
		close();
	}
	
	private void close() {
		getStage().close();
	}
	
	private ProductDTO createProductFromControls() {
		final PersistenceId id = (null == product) ? null : product.getId();
		final String name = nameTextField.getText();
		final String priceText = priceTextField.getText();
		final Double price = Double.parseDouble(priceText);
		
		final ProductDTO updatedProduct = new ProductDTO(id, name,price);
		return updatedProduct;
	}
	
	private void setProduct(final ProductDTO product) {
		assert(null != product);
		this.product = product;
	}
	
}
