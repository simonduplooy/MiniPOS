package com.lunarsky.minipos.ui;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.ui.Account;
import com.lunarsky.minipos.model.ui.User;
import com.sun.javafx.css.StyleManager;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class ViewManager {
	private static final Logger log = LogManager.getLogger();
	
	private static final String STYLESHEET_RESOURCE = "/resources/stylesheets/application.css";
	
	private final Stage primaryStage;
	
	public ViewManager(final Stage primaryStage) {
		assert(null != primaryStage);

		this.primaryStage = primaryStage;
		initialize();
	}
		
	public void initialize() {
		
		//TODO Add customizable external stylesheet
		Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
		StyleManager.getInstance().addUserAgentStylesheet(STYLESHEET_RESOURCE);
		
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
				AppData.getInstance().getServerConnector().createPersistenceConnection();
				return null;
				}
			@Override
			protected void succeeded() {
				log.debug("CreatePersistenceConnection() Succeeded");
				splashView.setInitializing(false);
				//showLoginDialog();
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
		assert(null != primaryStage);
		return primaryStage;
	}
		
	private void setDefaultLayout(final Region region) {
		region.setPrefSize(UiConst.PREF_LAYOUT_WIDTH,UiConst.PREF_LAYOUT_HEIGHT);
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
	
	public User getActiveUser() {
		final AppData appData = AppData.getInstance();
		User user = appData.getActiveUser();
		if(null == user) {
			showLoginDialog();
		}
		user = appData.getActiveUser();
		return user;
	}
	
	private SplashView showSplashView() {

		SplashView splashView = new SplashView();
		setDefaultLayout(splashView);
		setScene(splashView);

		return splashView;
	}
	
	public void showAccountOverviewView() {
		final Stage stage = getPrimaryStage();
		final AccountOverviewView accountView = new AccountOverviewView(stage);
		setScene(accountView);
		setDefaultLayout(accountView);
	}
	
	public void closeAccountOverviewView() {
		showSplashView();
	}

	private void showSaleView(final Account account) {
		SaleView saleView = new SaleView(account);
		setScene(saleView);
		setDefaultLayout(saleView);		
	}

	public void closeSaleView() {
		showAccountOverviewView();
	}
	
	public void accountSelected (final Account account) {
		showSaleView(account);
	}
		
	public void showProductConfigureView() {
		final Stage stage = getPrimaryStage();
		final ProductConfigureView view = new ProductConfigureView(stage);
		setScene(view);
		setDefaultLayout(view);	
	}
	
	public void closeProductConfigureView() {
		showSplashView();
	}
	
	public void showLoginDialog() {

        LoginDialog loginDialog = new LoginDialog(getPrimaryStage());
        loginDialog.getStage().showAndWait();

        final User user = loginDialog.getUser();
        if(null != user) {
        	log.debug(String.format("User Login: %s",user));
        	AppData.getInstance().setActiveUser(user);
        } else {
        	//TODO close application
        	//primaryStage.close();
        }
	}
	
	public void showDatabaseConfigDialog() {
        DatabaseConfigDialog dialog = new DatabaseConfigDialog(getPrimaryStage());
        dialog.getStage().show();
        //TODO Try to Open Connection if it fails show the Dialog again

	}	

	public void showUserOverviewDialog() {
		UserOverviewDialog dialog = new UserOverviewDialog(getPrimaryStage());
	    dialog.getStage().show();
	}	

	public void showProductOverviewDialog() {
		ProductOverviewDialog dialog = new ProductOverviewDialog(getPrimaryStage());
	    dialog.getStage().show();
	}
	
}
