package com.lunarsky.minipos.ui.validator;

import javafx.scene.control.TextField;

public class CurrencyTextFieldValidator extends TextFieldValidator {
	
	private static final String REGEX = "^\\d*(\\.\\d{0,2})?$";
	
	public CurrencyTextFieldValidator(final TextField textField, final Integer minLength, final Integer maxLength) {
		super(textField,REGEX,minLength,maxLength);
	}

}
