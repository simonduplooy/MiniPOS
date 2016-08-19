package com.lunarsky.minipos.ui.validator;

import javafx.scene.control.TextField;

public class StringTextFieldValidator extends TextFieldValidator {
	
	private static final String REGEX = "^\\p{Print}*$";
	
	public StringTextFieldValidator(final TextField textField, final Integer minLength, final Integer maxLength) {
		super(textField,REGEX,minLength,maxLength);
	}

	
}
