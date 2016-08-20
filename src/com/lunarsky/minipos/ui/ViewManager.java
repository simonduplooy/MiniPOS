package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.Account;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.User;

import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class ViewManager {
	
	private static final Logger log = LogManager.getLogger();
	
	private static final String STYLESHEET_RESOURCE = "/resources/stylesheets/application.css";
	
	private final Stage primaryStage;
	private final AppData appData;
	//TODO Add customizable external stylesheet
	private final String styleSheet;
	
	public ViewManager(final Stage primaryStage, final AppData appData) {
		assert(null != primaryStage);
		assert(null != appData);

		this.primaryStage = primaryStage;
		this.appData = appData;
		
		styleSheet = getClass().getResource(STYLESHEET_RESOURCE).toExternalForm();

		initialize();
	}
		
	private void initialize() {
		final SplashView splashView = showSplashView();
		
		final Stage stage = getPrimaryStage();
		stage.setMaximized(true);
		stage.show();
		
		initializeAsync(splashView);
	}
		
	private void initializeAsync(final SplashView splashView) {
		
		splashView.setInitializing(true);
		
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				appData.getServerConnector().createPersistenceConnection();
				return null;
				}
			@Override
			protected void succeeded() {
				log.debug("CreatePersistenceConnection() Succeeded");
				splashView.setInitializing(false);
				showLoginDialog();
			}
			@Override
			protected void failed() {
				log.error("CreatePersistenceConnection() Failed");
				log.catching(Level.ERROR,getException());
				showDatabaseConfigDialog();
			}
		};

		Thread thread = new Thread(task);
		log.debug("Starting CreatePersistenceConnection() Task {}",thread);
		thread.start();	
	}
	
	public void close () {

	}
	
	private Stage getPrimaryStage() {
		assert(primaryStage != null);
		return primaryStage;
	}
	
	public void setStyleSheets(final Scene scene) {
		assert(null != styleSheet);
		scene.getStylesheets().add(styleSheet);
	}
	
	private void setDefaultLayout(final Scene scene, final Region region) {
		setStyleSheets(scene);
		region.setPrefSize(UiConsts.PREF_LAYOUT_WIDTH,UiConsts.PREF_LAYOUT_HEIGHT);
	}
	
	//Workaround Stage.setScene() resizes the stage if it is maximized. 
	private void setScene(final Parent parent) {
		final Stage stage = getPrimaryStage();
		Scene scene = stage.getScene();
		if(null == scene) {
			scene = new Scene(parent);
			stage.setScene(scene);
		} else {
			scene.setRoot(parent);
		}
	}
	
	private SplashView showSplashView() {

		SplashView splashView = new SplashView(appData);
		setScene(splashView);
		final Scene scene = getPrimaryStage().getScene();
		setDefaultLayout(scene,splashView);

		return splashView;
	}
	
	public void showAccountOverviewView() {
		final Stage stage = getPrimaryStage();
		AccountOverviewView accountView = new AccountOverviewView(appData,stage);
		setScene(accountView);
		final Scene scene = stage.getScene();
		setDefaultLayout(scene,accountView);
	}
	
	public void closeAccountOverviewView() {
		showSplashView();
	}

	private void showAccountView(final Account account) {
		final Stage stage = getPrimaryStage();
		AccountView accountView = new AccountView(appData,account);
		setScene(accountView);
		final Scene scene = stage.getScene();
		setDefaultLayout(scene,accountView);		
	}

	public void closeAccountView() {
		showAccountOverviewView();
	}
	
	public void accountSelected (final Account account) {
		showProductOrderView(account);
	}
	
	private void showProductOrderView(final Account account) {
		final Stage stage = getPrimaryStage();
		ProductOrderView view = new ProductOrderView(appData,account);
		setScene(view);
		final Scene scene = stage.getScene();
		setDefaultLayout(scene,view);	
	}
	
	public void closeProductOrderView() {
		
	}
	
	public void showProductConfigureView() {
		final Stage stage = getPrimaryStage();
		final ProductConfigureView view = new ProductConfigureView(appData,stage);
		setScene(view);
		final Scene scene = stage.getScene();
		setDefaultLayout(scene,view);	
	}
	
	public void closeProductConfigureView() {
		showSplashView();
	}
	
	public void showLoginDialog() {

		// Create the dialog Stage.
        Stage dialogStage = new Stage();
        LoginDialog loginDialog = new LoginDialog(appData,getPrimaryStage(),dialogStage);
        Scene scene = new Scene(loginDialog);
        setStyleSheets(scene);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
        final User user = loginDialog.getUser();
        if(null != user) {
        	log.debug(String.format("User Login: %s",user));
        	appData.setActiveUser(user);
        } else {
        	//TODO close application
        	//primaryStage.close();
        }
        

	}
	
	public void showDatabaseConfigDialog() {
        DatabaseConfigDialog dialog = new DatabaseConfigDialog(appData,getPrimaryStage());
        dialog.getStage().showAndWait();
        //TODO Try to Open Connection if it fails show the Dialog again

	}	

	public void showUserOverviewDialog() {
		UserOverviewDialog dialog = new UserOverviewDialog(appData,getPrimaryStage());
	    dialog.showAndWait();
	}	
	
	
	public void showRoleOverviewDialog() {
		RoleOverviewDialog dialog = new RoleOverviewDialog(appData,getPrimaryStage());
	    dialog.showAndWait();
	}
	
	public void showStockOverviewDialog() {
		StockOverviewDialog dialog = new StockOverviewDialog(appData,getPrimaryStage());
	    dialog.showAndWait();
	}
	
	public void showProductOverviewDialog() {
		ProductOverviewDialog dialog = new ProductOverviewDialog(appData,getPrimaryStage());
	    dialog.getStage().showAndWait();
	}
	
}
