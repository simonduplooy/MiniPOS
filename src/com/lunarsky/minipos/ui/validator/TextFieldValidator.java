package com.lunarsky.minipos.ui.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextField;

public class TextFieldValidator {
	private static final Logger log = LogManager.getLogger();
	
	private final BooleanProperty validProperty;
	
	private final TextField textField;
	private final String validationRegex;
	private final Integer maxLength;
	private final Integer minLength;
		
	public TextFieldValidator(final TextField textField, final String validationRegex, final Integer minLength, final Integer maxLength) {
		assert(null != textField);
		assert(null != validationRegex);
		assert(null != maxLength);
		assert(null != minLength);
		
		this.textField = textField;
		this.validationRegex = validationRegex;
		this.maxLength = maxLength;
		this.minLength = minLength;
		
		//TODO Initialize
		validProperty = new SimpleBooleanProperty();

		textField.textProperty().addListener((observable,oldValue,newValue) -> handleTextChanged(oldValue,newValue));
		textField.focusedProperty().addListener((observable,oldValue,newValue) -> handleFocusChanged(newValue));
		
		//If the content is not valid at initialization it is cleared
		validate("",textField.getText());
	}
	
	public ReadOnlyBooleanProperty validProperty() {
		return validProperty;
	}
	
	public void close() {
	}
	
	
	private void handleFocusChanged(final Boolean hasFocus) {
		//Focus Lost
		if(!hasFocus) {
			final String text = textField.getText().trim();
			//The field was trimmed
			if(text.length() != textField.getText().length()) {
				//will call handleTextChanged
				textField.setText(text);
			}
		}
	}

	private void handleTextChanged(final String previousText, final String newText) {

		validate(previousText,newText);
		
	}
	
	private void validate(final String previousText, final String newText) {
		
		if(!contentValid(newText)) {
			log.debug("Invalid Content: [{}] Replace with: [{}}",newText,previousText);
			textField.setText(previousText);
		}
		
	}
	
	private boolean contentValid(final String text) {
		final int textLength = text.length();
		
		//if less than min length the content is valid but control is not
		if(textLength < minLength) {
			validProperty.set(false);
			return true;
		}

		if(textLength > maxLength) {
			validProperty.set(false);
			return false; 
			}
				
		if(!text.matches(validationRegex)) {
			validProperty.set(false);
			return false;
		}
		
		validProperty.set(true);
		return true;
	}
	
}
