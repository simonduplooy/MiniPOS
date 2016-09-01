package com.lunarsky.minipos.ui.virtualkeyboards;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

public class PasswordVirtualKeyboard extends VirtualKeyboardBase {
	private static final Logger log = LogManager.getLogger();
	
	public PasswordVirtualKeyboard(final Scene targetScene) {
		super(targetScene);
	}
	
	@Override
	protected void createButtons() {
		
		//for now target is removed
	    final ReadOnlyObjectProperty<Node> target = null;  
	    final VirtualKeyboardBase.Modifiers modifiers = getModifiers();
		
		// Data for regular buttons; split into rows
		final String[][] buttonText = new String[][] {
	        { "7", "8", "9" },
	        { "4", "5", "6" },
	        { "1", "2", "3" },
	        { "0", ".", "<" } };

	    final KeyCode[][] keyCodes = new KeyCode[][] {
	        { KeyCode.DIGIT7, KeyCode.DIGIT8, KeyCode.DIGIT9 },
	        { KeyCode.DIGIT4, KeyCode.DIGIT5, KeyCode.DIGIT6 },
	        { KeyCode.DIGIT1, KeyCode.DIGIT2, KeyCode.DIGIT3 },
	        { KeyCode.DIGIT0, KeyCode.PERIOD, KeyCode.BACK_SPACE } };
        
		// build layout
	    final GridPane gridPane = new GridPane();
	    gridPane.setAlignment(Pos.CENTER);
	    getChildren().add(gridPane);
		for (int row = 0; row < buttonText.length; row++) {
			for (int column = 0; column < buttonText[row].length; column++) {
				final Button button = createNonshiftableButton(buttonText[row][column], keyCodes[row][column], modifiers, target);
				button.setMaxWidth(Double.MAX_VALUE);
				gridPane.add(button,column,row);
			}
		}
	}
}