package com.lunarsky.minipos.model;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.ServerConnector;
import com.lunarsky.minipos.server.ServerConnectorFactory;
import com.lunarsky.minipos.ui.ViewManager;

import javafx.stage.Stage;

public class AppData {
	private static final Logger log = LogManager.getLogger();
	
	private ViewManager viewManager;
	private ServerConnector serverConnector;
	
	private User activeUser;
	
	public AppData(final Stage primaryStage) throws IOException {
		serverConnector = ServerConnectorFactory.createServerConnector();
		viewManager = new ViewManager(primaryStage,this);
	}
	
	public void close() {
		if(null != viewManager) { viewManager.close(); }
		if(null != serverConnector) { serverConnector.close(); }
	}
	
	
	public ServerConnector getServerConnector() {
		assert(null!= serverConnector);
		return serverConnector;
	}
	
	public ViewManager getViewManager() {
		assert(null != viewManager);
		return viewManager;
	}
	
	public User getActiveUser() {
		assert(null != activeUser);
		return activeUser;
	}
	
	public void setActiveUser(final User user) {
		assert(null != user);
		activeUser = user;
	}
	
}
