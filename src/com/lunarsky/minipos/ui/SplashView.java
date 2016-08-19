package com.lunarsky.minipos.ui;

import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.AppData;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class SplashView extends StackPane {
	private static final Logger log = LogManager.getLogger();

	private final AppData appData;
	
	@FXML
	private ProgressIndicator progressIndicator;
	@FXML
	private HBox itemHBox;
	
	public SplashView(final AppData appData) {
		assert(null != appData);
		this.appData = appData;
		
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SplashView.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
        	loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
		
		initialize();

	}
	
	private void initialize() {

		
		//TODO
		Button button = new Button("Database Config");
		button.setPrefWidth(USE_COMPUTED_SIZE);
		button.setMinWidth(USE_PREF_SIZE);
		button.setOnAction((event) -> appData.getViewManager().showDatabaseConfigDialog());
		itemHBox.getChildren().add(button);

		button = new Button("Login");
		button.setPrefWidth(USE_COMPUTED_SIZE);
		button.setMinWidth(USE_PREF_SIZE);
		button.setOnAction((event) -> appData.getViewManager().showLoginDialog());
		itemHBox.getChildren().add(button);

		button = new Button("Accounts");
		button.setPrefWidth(USE_COMPUTED_SIZE);
		button.setMinWidth(USE_PREF_SIZE);
		button.setOnAction((event) -> appData.getViewManager().showAccountOverviewView());
		itemHBox.getChildren().add(button);
		
		button = new Button("Users");
		button.setPrefWidth(USE_COMPUTED_SIZE);
		button.setMinWidth(USE_PREF_SIZE);
		button.setOnAction((event) -> appData.getViewManager().showUserOverviewDialog());
		itemHBox.getChildren().add(button);

		button = new Button("Roles");
		button.setPrefWidth(USE_COMPUTED_SIZE);
		button.setMinWidth(USE_PREF_SIZE);
		button.setOnAction((event) -> appData.getViewManager().showRoleOverviewDialog());
		itemHBox.getChildren().add(button);

		button = new Button("Stock");
		button.setPrefWidth(USE_COMPUTED_SIZE);
		button.setMinWidth(USE_PREF_SIZE);
		button.setOnAction((event) -> appData.getViewManager().showStockOverviewDialog());
		itemHBox.getChildren().add(button);

		button = new Button("Products");
		button.setPrefWidth(USE_COMPUTED_SIZE);
		button.setMinWidth(USE_PREF_SIZE);
		button.setOnAction((event) -> appData.getViewManager().showProductOverviewDialog());
		itemHBox.getChildren().add(button);
		
		button = new Button("Product Configure");
		button.setPrefWidth(USE_COMPUTED_SIZE);
		button.setMinWidth(USE_PREF_SIZE);
		button.setOnAction((event) -> appData.getViewManager().showProductConfigureView());
		itemHBox.getChildren().add(button);
		
		progressIndicator.visibleProperty().bind(itemHBox.disabledProperty());
		setInitializing(false);
		
		initializeAsync();
		
	}
	
	private void initializeAsync() {
		
	}
	
	public void setInitializing(final boolean initializing) {
		itemHBox.setDisable(initializing);
	}
	
	private void handleShowPersistenceConfig() {
		
	}
	
	private void handleCreateUser() {
		
	}
	
}
