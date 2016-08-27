package com.lunarsky.minipos.ui;

import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.common.exception.NameInUseException;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.ProductDTO;
import com.lunarsky.minipos.model.ui.Product;
import com.lunarsky.minipos.ui.validator.CurrencyTextFieldValidator;
import com.lunarsky.minipos.ui.validator.StringTextFieldValidator;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
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
import javafx.util.converter.NumberStringConverter;

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
	private Product product;
	 
	public ProductUpdateDialog(final Stage parentStage, final Product product) {
		assert(null != parentStage);
		assert(null != product);
		
		this.appData = AppData.getInstance();
		setProduct(product);

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
		stage.setScene(scene);

	}
	
	public Product getProduct() {
		assert(null != product);
		return product;
	}
	private void setProduct(final Product product) {
		assert(null != product);
		this.product = product;
	}
	
	@FXML
	private void initialize() {
		createSaveService();
		initializeMembers();
		initializeControls();
	}
	
	private void initializeMembers() {
	}
	
	private void initializeControls() {
		
		final Product product = getProduct();

		nameErrorLabel.managedProperty().bind(nameErrorLabel.visibleProperty());
		clearErrorMessages();
		
		nameValidator = new StringTextFieldValidator(nameTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_TEXTFIELD_LENGTH);
		priceValidator = new CurrencyTextFieldValidator(priceTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_TEXTFIELD_LENGTH);
		
		nameTextField.textProperty().bindBidirectional(product.nameProperty());
		NumberStringConverter converter = new NumberStringConverter(UiConst.CURRENCY_FORMAT);
		final StringProperty priceTextProperty = priceTextField.textProperty();
		final DoubleProperty priceDoubleProperty = product.priceProperty();
		
		log.debug("priceTextProperty [{}]",priceTextProperty.getValue());
		log.debug("priceDoubleProperty [{}]",priceDoubleProperty.getValue());
		
		Bindings.bindBidirectional(priceTextProperty,priceDoubleProperty,converter);
		
		log.debug("priceTextProperty [{}]",priceTextProperty.getValue());
		log.debug("priceDoubleProperty [{}]",priceDoubleProperty.getValue());
		
		saveButton.disableProperty().bind(nameValidator.validProperty().and(priceValidator.validProperty()).not().or(saveService.runningProperty()));
	}

	public void createSaveService() {
		saveService = new Service<ProductDTO>() {
			@Override
			protected Task<ProductDTO> createTask() {
				final Task<ProductDTO> task = new Task<ProductDTO>() {
					final ProductDTO productDTO = getProduct().createDTO();
					@Override
					protected ProductDTO call() throws EntityNotFoundException {
						return appData.getServerConnector().saveProduct(productDTO);
					}
					@Override
					protected void succeeded() {
						log.debug("saveProduct() Succeeded");
						final ProductDTO productDTO = getValue();
						final Product product = new Product(productDTO);
						setProduct(product);
						close();
					}
					@Override
					protected void failed() {
						final Throwable t = getException();
						log.debug("saveProduct() Failed");
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
	
}
