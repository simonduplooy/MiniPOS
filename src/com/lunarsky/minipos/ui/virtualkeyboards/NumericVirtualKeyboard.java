package com.lunarsky.minipos.ui.virtualkeyboards;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NumericVirtualKeyboard extends VBox {
	private static final Logger log = LogManager.getLogger();
	
	public NumericVirtualKeyboard() {
		
		setSpacing(5);
		setPadding(new Insets(10));
		getStyleClass().add("virtual-keyboard");

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
		for (int row = 0; row < buttonText.length; row++) {
			HBox hbox = new HBox(5);
			hbox.setAlignment(Pos.CENTER);
			getChildren().add(hbox);
			
			for (int column = 0; column < buttonText[row].length; column++) {
				hbox.getChildren().add( createButton(buttonText[row][column], keyCodes[row][column]) );
			}
		}
	}
  
  // Creates a button with mutable text, and registers listener with it
  private Button createButton(final String text, final KeyCode code) {
	  
	  StringProperty textProperty = new SimpleStringProperty(text);  
	  
	    final Button button = new Button();
	    button.textProperty().bind(textProperty);
	        
	    // Important not to grab the focus from the target:
	    button.setFocusTraversable(false);
	    
	    // Add a style class for css:
	    button.getStyleClass().add("virtual-keyboard-button");
	    
	    button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
			
				final Node targetNode = getScene().getFocusOwner();
				
				if (targetNode != null) {
					final String character;
					if (textProperty.get().matches("^[0-9]$")) {
						character = textProperty.get();
					} else {
						character = KeyEvent.CHAR_UNDEFINED;
					}
					  
					final KeyEvent keyPressEvent = createKeyEvent(button, targetNode, KeyEvent.KEY_PRESSED, character, code);
					targetNode.fireEvent(keyPressEvent);
					final KeyEvent keyReleasedEvent = createKeyEvent(button, targetNode, KeyEvent.KEY_RELEASED, character, code);
					targetNode.fireEvent(keyReleasedEvent);
					if (character != KeyEvent.CHAR_UNDEFINED) {
						final KeyEvent keyTypedEvent = createKeyEvent(button, targetNode, KeyEvent.KEY_TYPED, character, code);
						targetNode.fireEvent(keyTypedEvent);
					}
				}
			}
		});
	    
	    return button;
  }

  // Utility method to create a KeyEvent from the Modifiers
  private KeyEvent createKeyEvent(Object source, EventTarget target, EventType<KeyEvent> eventType, String character, KeyCode code) {
	  KeyEvent keyEvent = new KeyEvent(source, target, eventType, character, code.toString(), code, false, false, false, false);
	  return keyEvent;
  }  
}