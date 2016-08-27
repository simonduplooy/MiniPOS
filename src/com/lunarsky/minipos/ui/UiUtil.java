package com.lunarsky.minipos.ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UiUtil {

	public static Stage createDialogStage(final Stage parentStage,final String title) {
		final Stage stage = new Stage();
		stage.initOwner(parentStage);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setTitle(title);
		return stage;
	}
	
	public static void loadRootConstructNode(final Object controller, final String resourceLocation) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(controller.getClass().getResource(resourceLocation));
        loader.setRoot(controller);
        loader.setController(controller);
        try {
        	loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
	}
	
	private UiUtil() {
	}
}
