package com.lunarsky.minipos.main;
	
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.ui.ExceptionAlert;
import com.lunarsky.minipos.util.Util;

import javafx.application.Application;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


public class Main extends Application {
	private static final Logger log = LogManager.getLogger();
	
	private static final String APPLICATION_PACKAGE = "com.lunarsky.minipos";
	private static final Level LOG_LEVEL = Level.TRACE;
	
	private static final String ERROR_TEXT_UNHANDLED_EXCEPTION = "An Unhandled Error Occured";
	
	private AppData appData;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//Set the log level for the whole application
		Util.setLogLevel(APPLICATION_PACKAGE, LOG_LEVEL);
		
		//FX Application Thread default Exception Handler
		Thread.currentThread().setUncaughtExceptionHandler((thread,throwable) -> handleUiException(thread,throwable));		
		//Default Exception Hanler for all other threads
		Thread.setDefaultUncaughtExceptionHandler((thread,throwable) -> handleException(thread,throwable));
		
		appData = AppData.createInstance(primaryStage);


	}
	
	@Override
	public void stop() {
		if(appData != null) {
			appData.close();
		}
	}
	
	/*
	public static void main(String[] args) {
		launch(args);
	}
	*/
	
	private void handleUiException(final Thread thread, final Throwable throwable) {
		handleException(thread,throwable);
		final ExceptionAlert alert = new ExceptionAlert(AlertType.ERROR,ERROR_TEXT_UNHANDLED_EXCEPTION,throwable);
		alert.showAndWait();
	}
	
	private void handleException(final Thread thread, final Throwable throwable) {
		log.catching(Level.ERROR,throwable);
	}
}
