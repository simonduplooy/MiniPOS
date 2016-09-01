package com.lunarsky.minipos.ui.virtualkeyboards;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class VirtualKeyboardBase extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String KEYPAD_BUTTON_TEXT = "Keyboard";

	private final Modifiers modifiers;
	private final ToggleButton keyboardButton;
	

	public VirtualKeyboardBase() {
					
		modifiers = new Modifiers();
	
		//build the layout
		setPadding(new Insets(5));
		getStyleClass().add("virtual-keyboard");
		
		createButtons();
		
		final HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER_RIGHT);
		BorderPane.setMargin(hbox,new Insets(5,0,0,0));
		keyboardButton = new ToggleButton(KEYPAD_BUTTON_TEXT);
		hbox.getChildren().add(keyboardButton);
		setBottom(hbox);
		
		final Node centerNode = getCenter();
		centerNode.managedProperty().bind(centerNode.visibleProperty());
		centerNode.visibleProperty().bind(keyboardButton.selectedProperty());
		keyboardButton.setOnAction((event) -> handleShowKeyboardButton());

	}
	
	protected void handleShowKeyboardButton() {
		final Stage stage = (Stage)getScene().getWindow();
		if(!stage.isMaximized()) {
			stage.sizeToScene();
			stage.centerOnScreen();
		}
	}
	
	protected void createButtons() {
		
	}
	
	protected Modifiers getModifiers() {
		assert(null != modifiers);
		return modifiers;
	}
  
 
  // Creates a "regular" button that has an unshifted and shifted value
  protected Button createShiftableButton(final String unshifted, final String shifted,
		final KeyCode code, Modifiers modifiers, final ReadOnlyObjectProperty<Node> target) {
		final ReadOnlyBooleanProperty letter = new SimpleBooleanProperty( unshifted.length() == 1 && Character.isLetter(unshifted.charAt(0)));
		final StringBinding text = 
				Bindings.when(modifiers.shiftDown().or(modifiers.capsLockOn().and(letter)))
				.then(shifted)
				.otherwise(unshifted);
		Button button = createButton(text, code, modifiers, target);
		return button;
	}

	// Creates a button with fixed text not responding to Shift
	protected Button createNonshiftableButton(final String text, final KeyCode code, final Modifiers modifiers, final ReadOnlyObjectProperty<Node> target) {
		StringProperty textProperty = new SimpleStringProperty(text);
		Button button = createButton(textProperty, code, modifiers, target);
		return button;
	}
  
	// Creates a button with mutable text, and registers listener with it
	private Button createButton(final ObservableStringValue text, final KeyCode code, final Modifiers modifiers, final ReadOnlyObjectProperty<Node> target) {
		final Button button = new Button();
		button.textProperty().bind(text);
        
		// Important not to grab the focus from the target:
		button.setFocusTraversable(false);
    
		// Add a style class for css:
		button.getStyleClass().add("virtual-keyboard-button");
    
		button.setOnAction(new EventHandler<ActionEvent>() {
		  
			@Override
			public void handle(ActionEvent event) {

				final Node targetNode;
				if (target != null) {
					targetNode = target.get();
				} else {
					targetNode = getScene().getFocusOwner();
				}
        
				if (targetNode != null) {
					  final String character;
					  if (text.get().length() == 1) {
						  character = text.get();
					  } else {
						  character = KeyEvent.CHAR_UNDEFINED;
					  }
					  
					  final KeyEvent keyPressEvent = createKeyEvent(button, targetNode, KeyEvent.KEY_PRESSED, character, code, modifiers);
					  targetNode.fireEvent(keyPressEvent);
					  final KeyEvent keyReleasedEvent = createKeyEvent(button, targetNode, KeyEvent.KEY_RELEASED, character, code, modifiers);
					  targetNode.fireEvent(keyReleasedEvent);
					  if (character != KeyEvent.CHAR_UNDEFINED) {
						  final KeyEvent keyTypedEvent = createKeyEvent(button, targetNode, KeyEvent.KEY_TYPED, character, code, modifiers);
						  targetNode.fireEvent(keyTypedEvent);
					  }
					  modifiers.releaseKeys();
				  }
			  }
		  });
		  
		  return button;
		}
	
		// Utility method to create a KeyEvent from the Modifiers
		private KeyEvent createKeyEvent(Object source, EventTarget target,
				EventType<KeyEvent> eventType, String character, KeyCode code,
				Modifiers modifiers) {
			
			return new KeyEvent(source, target, eventType, character, code.toString(),
					code, modifiers.shiftDown().get(), modifiers.ctrlDown().get(),
					modifiers.altDown().get(), modifiers.metaDown().get());
		}
	  
		// Utility method for creating cursor keys:
		protected Button createCursorKey(KeyCode code, Modifiers modifiers, ReadOnlyObjectProperty<Node> target, Double... points) {
			Button button = createNonshiftableButton("", code, modifiers, target);
			final Polygon graphic = new Polygon();
			graphic.getPoints().addAll(points);
			graphic.setStyle("-fx-fill: -fx-mark-color;");
			button.setGraphic(graphic);
			button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			return button ;
		}
	  
		// Convenience class to bundle together the modifier keys and their selected state
		protected static class Modifiers {
			private final ToggleButton shift;
			private final ToggleButton shift2;
			private final ToggleButton ctrl;
			private final ToggleButton alt;
			private final ToggleButton meta;
			private final ToggleButton capsLock;
	
			Modifiers() {
				this.shift = createToggle("Shift");
				this.shift2 = createToggle("Shift");
				this.ctrl = createToggle("Ctrl");
				this.alt = createToggle("Alt");
				this.meta = createToggle("Meta");
				this.capsLock = createToggle("Caps");
	
				shift2.selectedProperty().bindBidirectional(shift.selectedProperty());
			}
	
			private ToggleButton createToggle(final String text) {
				final ToggleButton tb = new ToggleButton(text);
				// Add a style class for css:
				tb.getStyleClass().add("virtual-keyboard-button");
				tb.setFocusTraversable(false);
				return tb;
			}
	
			public Node shiftKey() {
				return shift;
			}
	
			public Node secondShiftKey() {
				return shift2;
			}
	
			public Node ctrlKey() {
				return ctrl;
			}
	
			public Node altKey() {
				return alt;
			}
	
			public Node metaKey() {
				return meta;
			}
	
			public Node capsLockKey() {
				return capsLock;
			}
	
			public BooleanProperty shiftDown() {
				return shift.selectedProperty();
			}
	
			public BooleanProperty ctrlDown() {
				return ctrl.selectedProperty();
			}
	
			public BooleanProperty altDown() {
				return alt.selectedProperty();
			}
	
			public BooleanProperty metaDown() {
				return meta.selectedProperty();
			}
	
			public BooleanProperty capsLockOn() {
				return capsLock.selectedProperty();
			}
	
			public void releaseKeys() {
				shift.setSelected(false);
				ctrl.setSelected(false);
				alt.setSelected(false);
				meta.setSelected(false);
			}
		}  
	}