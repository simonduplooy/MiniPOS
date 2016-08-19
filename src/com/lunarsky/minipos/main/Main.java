package com.lunarsky.minipos.main;
	
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.util.Util;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
	
	private static final String APPLICATION_PACKAGE = "com.lunarsky.minipos";
	private static final Level LOG_LEVEL = Level.TRACE;
	private static final Logger log = LogManager.getLogger();
	
	private AppData appData;
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		//Set the log level for the whole application
		Util.setLogLevel(APPLICATION_PACKAGE, LOG_LEVEL);
		try {
			appData = new AppData(primaryStage);
		} catch(Exception e) {
			log.catching(e);
			throw(e);
		}
	}
	
	@Override
	public void stop() {
		if(appData != null) {
			appData.close();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
