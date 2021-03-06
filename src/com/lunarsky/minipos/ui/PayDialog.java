package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.ui.validator.CurrencyTextFieldValidator;
import com.lunarsky.minipos.ui.virtualkeyboards.NumericVirtualKeyboard;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.converter.CurrencyStringConverter;
import javafx.util.converter.NumberStringConverter;

public class PayDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String WINDOW_TITLE = "Payment";
	
	private final DoubleProperty totalProperty;
	private final DoubleProperty tenderProperty;
	private CurrencyTextFieldValidator tenderValidator;
	private final BooleanProperty tenderValidProperty;
	private final DoubleProperty changeProperty;
	
	@FXML
	private Label totalLabel;
	@FXML
	private TextField tenderTextField;
	@FXML
	private Label changeLabel;
	@FXML
	private BorderPane keypadBorderPane;
	@FXML
	private Button cashButton;
	@FXML
	private Button cardButton;
	
	
	public PayDialog(final Stage parentStage, final Double total) {
		totalProperty = new SimpleDoubleProperty(total);
		tenderProperty = new SimpleDoubleProperty(total);
		tenderValidProperty = new SimpleBooleanProperty();
		changeProperty = new SimpleDoubleProperty();
				
		UiUtil.createDialog(parentStage,WINDOW_TITLE,this,"PayDialog.fxml");
	}
	
	@FXML
	private void initialize() {
		
		final CurrencyStringConverter currencyConverter = new CurrencyStringConverter();
		Bindings.bindBidirectional(totalLabel.textProperty(),totalProperty,currencyConverter);

		tenderValidator = new CurrencyTextFieldValidator(tenderTextField,Const.MIN_REQUIRED_TEXTFIELD_LENGTH,Const.MAX_CURRENCY_TEXTFIELD_LENGTH);
		final NumberStringConverter doubleConverter = new NumberStringConverter(UiConst.CURRENCY_FORMAT);
		Bindings.bindBidirectional(tenderTextField.textProperty(),tenderProperty,doubleConverter);
		
		tenderValidProperty.bind(tenderProperty.greaterThanOrEqualTo(totalProperty));
		
		changeProperty.bind(tenderProperty.subtract(totalProperty));
		Bindings.bindBidirectional(changeLabel.textProperty(),changeProperty,currencyConverter);
		
		final NumericVirtualKeyboard keypad = new NumericVirtualKeyboard(getScene());
		keypadBorderPane.setCenter(keypad);
		
		//TODO Allow Multiple Payments?
		cashButton.disableProperty().bind(tenderValidProperty.not());
		cardButton.disableProperty().bind(tenderValidProperty.not());
		
	}

	
	public Stage getStage() {
		return (Stage)getScene().getWindow();
	}
	
	@FXML
	private void handleCash() {
		log.debug("handleCash()");
	}

	@FXML
	private void handleCard() {
		log.debug("handleCard()");
		
	}

	@FXML
	private void handleCancel() {
		log.debug("handleCancel()");
		close();
	}
	
	private void close() {
		getStage().close();
	}

}
