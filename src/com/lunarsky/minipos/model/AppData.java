package com.lunarsky.minipos.model;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.ServerConnector;
import com.lunarsky.minipos.model.dto.UserDTO;
import com.lunarsky.minipos.server.ServerConnectorFactory;
import com.lunarsky.minipos.ui.ViewManager;

import javafx.stage.Stage;

public class AppData {
	private static final Logger log = LogManager.getLogger();
	
	//Singleton(ish)
	private static AppData instance;
	
	private Stage primaryStage;
	private ViewManager viewManager;
	private ServerConnector serverConnector;
	
	private static UserDTO activeUser;

	
	public static AppData getInstance() {
		assert(null != instance);
		return instance;
	}
	
	//Use createInstance as ViewManager needs primary stage during initialization
	public static AppData createInstance(final Stage primaryStage) {
		assert(null == instance);		
		instance = new AppData(primaryStage);
		return instance; 
	}
	
	private AppData(final Stage primaryStage) {
		assert(null != primaryStage);
		this.primaryStage = primaryStage;
		serverConnector = ServerConnectorFactory.createServerConnector();
		viewManager = new ViewManager(primaryStage);
	}	
	
	public Stage getPrimaryStage() {
		assert(null != primaryStage);
		return primaryStage;
	}
	
	public ServerConnector getServerConnector() {
		assert(null!= serverConnector);
		return serverConnector;
	}
	
	public ViewManager getViewManager() {
		assert(null != viewManager);
		return viewManager;
	}
	
	public UserDTO getActiveUser() {
		assert(null != activeUser);
		return activeUser;
	}
	
	public void setActiveUser(final UserDTO user) {
		assert(null != user);
		activeUser = user;
	}
	
	public void close() {
		if(null != viewManager) { viewManager.close(); }
		if(null != serverConnector) { serverConnector.close(); }
	}
	
}
