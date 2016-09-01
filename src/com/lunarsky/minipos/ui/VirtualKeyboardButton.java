package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.ui.virtualkeyboards.VirtualKeyboardDialog;

import javafx.scene.Scene;
import javafx.scene.control.Button;

public class VirtualKeyboardButton extends Button {
	private static final Logger log = LogManager.getLogger();
	
	private static final String BUTTON_TEXT = "Keyboard";
	
	private final Scene targetScene;
	private VirtualKeyboardDialog keyboardDialog;
	
	public VirtualKeyboardButton(final Scene targetScene) {
		assert(null != targetScene);
		
		this.targetScene = targetScene;
		
		setText(BUTTON_TEXT);
		setOnAction((event) -> showKeyboard());
		
	}
	
	public void close() {
		if(null != keyboardDialog) {
			keyboardDialog.close();
		}
	}
	
	private void showKeyboard() {
		if(null == keyboardDialog) {
			keyboardDialog = new VirtualKeyboardDialog(targetScene);
		}
		keyboardDialog.getStage().show();
	}

}
