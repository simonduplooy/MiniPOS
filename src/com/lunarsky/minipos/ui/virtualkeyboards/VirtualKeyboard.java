package com.lunarsky.minipos.ui.virtualkeyboards;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class VirtualKeyboard extends VirtualKeyboardBase {

	public VirtualKeyboard(final Scene targetScene) {
		super(targetScene);
	}
	
	@Override
	protected void createButtons() {
		// Data for regular buttons; split into rows
		final String[][] unshifted = new String[][] {
			{ "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" },
	        { "q", "w", "e", "r", "t", "y", "u", "i", "o", "p" },
	        { "a", "s", "d", "f", "g", "h", "j", "k", "l"  },
	        { "z", "x", "c", "v", "b", "n", "m"  } };

	    final String[][] shifted = new String[][] {
	        { "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")" },
	        { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P" },
	        { "A", "S", "D", "F", "G", "H", "J", "K", "L" },
	        { "Z", "X", "C", "V", "B", "N", "M" } };

		final KeyCode[][] codes = new KeyCode[][] {
		    { KeyCode.DIGIT1, KeyCode.DIGIT2, KeyCode.DIGIT3,
		    	KeyCode.DIGIT4, KeyCode.DIGIT5, KeyCode.DIGIT6, KeyCode.DIGIT7,
		        KeyCode.DIGIT8, KeyCode.DIGIT9, KeyCode.DIGIT0 },
		    { KeyCode.Q, KeyCode.W, KeyCode.E, KeyCode.R, KeyCode.T, KeyCode.Y,
		        KeyCode.U, KeyCode.I, KeyCode.O, KeyCode.P },
		    { KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.F, KeyCode.G, KeyCode.H,
		        KeyCode.J, KeyCode.K, KeyCode.L },
		    { KeyCode.Z, KeyCode.X, KeyCode.C, KeyCode.V, KeyCode.B, KeyCode.N,
		        KeyCode.M } };

		//for now target is removed
	    final ReadOnlyObjectProperty<Node> target = null;        
	    
	    final VirtualKeyboardBase.Modifiers modifiers = getModifiers();

	    final VBox vbox = new VBox();
	    vbox.setPrefWidth(USE_COMPUTED_SIZE);
	    vbox.setMinWidth(USE_PREF_SIZE);
	    vbox.setMaxWidth(USE_PREF_SIZE);
	    BorderPane.setAlignment(vbox,Pos.BOTTOM_CENTER);
	    getChildren().add(vbox);
	    //getButtonPane().getChildren().add(vbox);
	    
		// build layout
		for (int row = 0; row < unshifted.length; row++) {
			HBox hbox = new HBox(5);
			hbox.setAlignment(Pos.CENTER);
			vbox.getChildren().add(hbox);

			for (int k = 0; k < unshifted[row].length; k++) {
				hbox.getChildren().add( createShiftableButton(unshifted[row][k], shifted[row][k], codes[row][k], modifiers, target));
			}
		}
		
		final Button spaceBar = createNonshiftableButton(" ", KeyCode.SPACE, modifiers, target);
			spaceBar.setMaxWidth(Double.POSITIVE_INFINITY);
			HBox.setHgrow(spaceBar, Priority.ALWAYS);
		
			final HBox bottomRow = new HBox(5);
			bottomRow.setAlignment(Pos.CENTER);
			bottomRow.getChildren().addAll(modifiers.shiftKey(), spaceBar, modifiers.secondShiftKey());
			vbox.getChildren().add(bottomRow);    	
	}

}