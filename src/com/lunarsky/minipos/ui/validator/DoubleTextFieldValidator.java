package com.lunarsky.minipos.ui.validator;

import javafx.scene.control.TextField;

public class DoubleTextFieldValidator extends TextFieldValidator {
	
	private static final String REGEX = "^[,\\d]*(\\.\\d+)?$";
	
	public DoubleTextFieldValidator(final TextField textField, final Integer minLength, final Integer maxLength) {
		super(textField,REGEX,minLength,maxLength);
	}

}
