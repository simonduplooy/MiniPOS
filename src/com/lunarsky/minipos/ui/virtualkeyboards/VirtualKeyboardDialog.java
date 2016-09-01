package com.lunarsky.minipos.ui.virtualkeyboards;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class VirtualKeyboardDialog extends BorderPane {
	
	private final VirtualKeyboard virtualKeyboard;
	private final Node targetNode;
	
	public VirtualKeyboardDialog(final Scene targetScene) {
		
		//TODO make window non-focussable, but there seems to be an issue in Javafx
		//http://stackoverflow.com/questions/33151460/javafx-stop-new-window-stealing-focus
		targetNode = targetScene.getFocusOwner();
		
		final Stage stage = new Stage();
		//stage.initOwner(null);
		stage.initModality(Modality.NONE);
		stage.setAlwaysOnTop(true);
		//stage.initStyle(StageStyle.UTILITY);
		stage.setResizable(false);
		
		
		final Scene scene = new Scene(this);
		stage.setScene(scene);
		
		virtualKeyboard = new VirtualKeyboard(targetScene);
		setCenter(virtualKeyboard);
			
		stage.setOnShown((event) -> positionStage());
	}
	
	public Stage getStage() {
		return (Stage)getScene().getWindow();
	}
	
	public void close() {
		getStage().close();
	}

	private VirtualKeyboard getVirtualKeyboard() {
		assert(null != virtualKeyboard);
		return virtualKeyboard;
	}
	
	private void positionStage() {
		
		targetNode.requestFocus();
		
		final Stage stage = getStage();

		//TODO place on current screen
		//Screen.getScreensForRectangle();
        Rectangle2D screenRect = Screen.getPrimary().getVisualBounds();
        final Double screenWidth = screenRect.getWidth();
        final Double screenHeight = screenRect.getHeight();

        final Double stageWidth = stage.getWidth();
        final Double stageHeight = stage.getHeight();
        
        final Double xPos = (screenWidth - stageWidth)/2.0;
        final Double yPos = (screenHeight - stageHeight);
        
		stage.setX(xPos);
		stage.setY(yPos);
	}
	
}
