package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.AppData;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

public class SplashView extends StackPane {
	private static final Logger log = LogManager.getLogger();
	
	@FXML
	private ProgressIndicator progressIndicator;
	@FXML
	private FlowPane itemFlowPane;
	
	public SplashView() {
		
		UiUtil.loadRootConstructNode(this,"SplashView.fxml");

		initialize();

	}
	
	private void initialize() {

		//TODO
		Button button = new Button("Database Config");
		button.setPrefWidth(USE_COMPUTED_SIZE);
		button.setMinWidth(USE_PREF_SIZE);
		button.setOnAction((event) -> AppData.getInstance().getViewManager().showDatabaseConfigDialog());
		itemFlowPane.getChildren().add(button);

		button = new Button("Login");
		button.setPrefWidth(USE_COMPUTED_SIZE);
		button.setMinWidth(USE_PREF_SIZE);
		button.setOnAction((event) -> AppData.getInstance().getViewManager().showLoginDialog());
		itemFlowPane.getChildren().add(button);

		button = new Button("Accounts");
		button.setPrefWidth(USE_COMPUTED_SIZE);
		button.setMinWidth(USE_PREF_SIZE);
		button.setOnAction((event) -> AppData.getInstance().getViewManager().showAccountOverviewView());
		itemFlowPane.getChildren().add(button);
		
		button = new Button("Users");
		button.setPrefWidth(USE_COMPUTED_SIZE);
		button.setMinWidth(USE_PREF_SIZE);
		button.setOnAction((event) -> AppData.getInstance().getViewManager().showUserOverviewDialog());
		itemFlowPane.getChildren().add(button);

		button = new Button("Products");
		button.setPrefWidth(USE_COMPUTED_SIZE);
		button.setMinWidth(USE_PREF_SIZE);
		button.setOnAction((event) -> AppData.getInstance().getViewManager().showProductOverviewDialog());
		itemFlowPane.getChildren().add(button);
		
		button = new Button("Product Configure");
		button.setPrefWidth(USE_COMPUTED_SIZE);
		button.setMinWidth(USE_PREF_SIZE);
		button.setOnAction((event) -> AppData.getInstance().getViewManager().showProductConfigureView());
		itemFlowPane.getChildren().add(button);
		
		progressIndicator.visibleProperty().bind(itemFlowPane.disabledProperty());
		setInitializing(false);
		
		initializeAsync();
		
	}
	
	private void initializeAsync() {
		
	}
	
	public void setInitializing(final boolean initializing) {
		itemFlowPane.setDisable(initializing);
	}
	
	private void handleShowPersistenceConfig() {
		
	}
	
	private void handleCreateUser() {
		
	}
	
}
