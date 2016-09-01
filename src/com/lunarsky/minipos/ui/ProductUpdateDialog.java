package com.lunarsky.minipos.ui;

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
	
	private static final String WINDOW_TITLE = "Product";
	
	private final AppData appData;
	private StringTextFieldValidator nameValidator;
	private CurrencyTextFieldValidator priceValidator;
	private Service<ProductDTO> saveService;
	private final Product product;
	private boolean wasSaved;
	
	@FXML
	private Label nameErrorLabel;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField priceTextField;
	@FXML
	private Button saveButton;
	 
	public ProductUpdateDialog(final Stage parentStage, final Product product) {
		assert(null != parentStage);
		assert(null != product);
		
		this.appData = AppData.getInstance();
		//Create a copy so that the original product is not updated in case the dialog is cancelled
		this.product = product;

		UiUtil.createDialog(parentStage,WINDOW_TITLE,this,"ProductUpdateDialog.fxml");
	}
	
	public Product getProduct() {
		assert(null != product);
		return product;
	}
	
	@FXML
	private void initialize() {
		initializeMembers();
		initializeControls();
		initializeBindings();
		initializeListeners();
	}
	
	private void initializeMembers() {
		createSaveService();
		nameValidator = new StringTextFieldValidator(nameTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_TEXTFIELD_LENGTH);
		priceValidator = new CurrencyTextFieldValidator(priceTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_TEXTFIELD_LENGTH);
	}
	
	private void initializeControls() {
		clearErrorMessages();
	}

	private void initializeBindings() {
		nameErrorLabel.managedProperty().bind(nameErrorLabel.visibleProperty());
		
		final Product product = getProduct();
		nameTextField.textProperty().bindBidirectional(product.nameProperty());
		NumberStringConverter converter = new NumberStringConverter(UiConst.CURRENCY_FORMAT);
		final StringProperty priceTextProperty = priceTextField.textProperty();
		final DoubleProperty priceDoubleProperty = product.priceProperty();
		Bindings.bindBidirectional(priceTextProperty,priceDoubleProperty,converter);
		
		saveButton.disableProperty().bind(nameValidator.validProperty().and(priceValidator.validProperty()).not().or(saveService.runningProperty()));
	}
	
	private void initializeListeners() {
		getStage().setOnCloseRequest((event) -> close());
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
						getProduct().set(productDTO);
						wasSaved = true;
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
		return (Stage)getScene().getWindow();
	}
	
	public boolean wasSaved() {
		return wasSaved;
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
