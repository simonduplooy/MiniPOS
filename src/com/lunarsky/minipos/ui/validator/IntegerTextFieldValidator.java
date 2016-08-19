package com.lunarsky.minipos.ui.validator;

import javafx.scene.control.TextField;

public class IntegerTextFieldValidator extends TextFieldValidator {
	
	private static final String REGEX = "^[0-9]*$";
	
	public IntegerTextFieldValidator(final TextField textField, final Integer minLength, final Integer maxLength) {
		super(textField,REGEX,minLength,maxLength);
	}

}
