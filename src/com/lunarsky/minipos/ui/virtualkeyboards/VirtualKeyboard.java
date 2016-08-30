package com.lunarsky.minipos.ui.virtualkeyboards;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;

public class VirtualKeyboard extends VirtualKeyboardBase {

public VirtualKeyboard() {

	// Data for regular buttons; split into rows
	final String[][] unshifted = new String[][] {
		{ "`", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=" },
        { "q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "[", "]", "\\" },
        { "a", "s", "d", "f", "g", "h", "j", "k", "l", ";", "'" },
        { "z", "x", "c", "v", "b", "n", "m", ",", ".", "/" } };

    final String[][] shifted = new String[][] {
        { "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+" },
        { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "{", "}", "|" },
        { "A", "S", "D", "F", "G", "H", "J", "K", "L", ":", "\"" },
        { "Z", "X", "C", "V", "B", "N", "M", "<", ">", "?" } };

	final KeyCode[][] codes = new KeyCode[][] {
	    { KeyCode.BACK_QUOTE, KeyCode.DIGIT1, KeyCode.DIGIT2, KeyCode.DIGIT3,
	    	KeyCode.DIGIT4, KeyCode.DIGIT5, KeyCode.DIGIT6, KeyCode.DIGIT7,
	        KeyCode.DIGIT8, KeyCode.DIGIT9, KeyCode.DIGIT0, KeyCode.SUBTRACT,
	        KeyCode.EQUALS },
	    { KeyCode.Q, KeyCode.W, KeyCode.E, KeyCode.R, KeyCode.T, KeyCode.Y,
	        KeyCode.U, KeyCode.I, KeyCode.O, KeyCode.P, KeyCode.OPEN_BRACKET,
	        KeyCode.CLOSE_BRACKET, KeyCode.BACK_SLASH },
	    { KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.F, KeyCode.G, KeyCode.H,
	        KeyCode.J, KeyCode.K, KeyCode.L, KeyCode.SEMICOLON, KeyCode.QUOTE },
	    { KeyCode.Z, KeyCode.X, KeyCode.C, KeyCode.V, KeyCode.B, KeyCode.N,
	        KeyCode.M, KeyCode.COMMA, KeyCode.PERIOD, KeyCode.SLASH } };

	//for now target is removed
    final ReadOnlyObjectProperty<Node> target = null;        
    
    final VirtualKeyboardBase.Modifiers modifiers = getModifiers();
    
    final Button escape = createNonshiftableButton("Esc", KeyCode.ESCAPE, modifiers, target);
    final Button backspace = createNonshiftableButton("Backspace", KeyCode.BACK_SPACE, modifiers, target);
    final Button delete = createNonshiftableButton("Del", KeyCode.DELETE, modifiers, target);
    final Button enter = createNonshiftableButton("Enter", KeyCode.ENTER,  modifiers, target);
    final Button tab = createNonshiftableButton("Tab", KeyCode.TAB, modifiers, target);
	
    // Cursor keys, with graphic instead of text
    final Button cursorLeft = createCursorKey(KeyCode.LEFT, modifiers, target, 15.0, 5.0, 15.0, 15.0, 5.0, 10.0);
    final Button cursorRight = createCursorKey(KeyCode.RIGHT, modifiers, target, 5.0, 5.0, 5.0, 15.0, 15.0, 10.0);
    final Button cursorUp = createCursorKey(KeyCode.UP, modifiers, target, 10.0, 0.0, 15.0, 10.0, 5.0, 10.0);
    final Button cursorDown = createCursorKey(KeyCode.DOWN, modifiers, target, 10.0, 10.0, 15.0, 0.0, 5.0, 0.0);
	
    final VBox cursorUpDown = new VBox(2);
    cursorUpDown.getChildren().addAll(cursorUp, cursorDown);

    // "Extras" to go at the left or right end of each row of buttons.
    final Node[][] extraLeftButtons = new Node[][] { {escape}, {tab}, {modifiers.capsLockKey()}, {modifiers.shiftKey()} };
    final Node[][] extraRightButtons = new Node[][] { {backspace}, {delete}, {enter}, {modifiers.secondShiftKey()} };


    final VBox vbox = new VBox();
    setCenter(vbox);
    
	// build layout
	for (int row = 0; row < unshifted.length; row++) {
		HBox hbox = new HBox(5);
		hbox.setAlignment(Pos.CENTER);
		vbox.getChildren().add(hbox);

		hbox.getChildren().addAll(extraLeftButtons[row]);
		for (int k = 0; k < unshifted[row].length; k++) {
			hbox.getChildren().add( createShiftableButton(unshifted[row][k], shifted[row][k], codes[row][k], modifiers, target));
		}
		hbox.getChildren().addAll(extraRightButtons[row]);
	}
	
	final Button spaceBar = createNonshiftableButton(" ", KeyCode.SPACE, modifiers, target);
		spaceBar.setMaxWidth(Double.POSITIVE_INFINITY);
		HBox.setHgrow(spaceBar, Priority.ALWAYS);
	
		final HBox bottomRow = new HBox(5);
		bottomRow.setAlignment(Pos.CENTER);
		bottomRow.getChildren().addAll(modifiers.ctrlKey(), modifiers.altKey(), modifiers.metaKey(), spaceBar, cursorLeft, cursorUpDown, cursorRight);
		vbox.getChildren().add(bottomRow);    
	}
																																											 																			
}