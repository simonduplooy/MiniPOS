package com.lunarsky.minipos.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.ServerConnector;

public class ServerConnectorFactory {
	
	private static final Logger log = LogManager.getLogger();
	
	//Prevent instantiation 
	private ServerConnectorFactory() {
		log.error("ServerConnectorFactory instantiated");
	}

	static public ServerConnector createServerConnector() {
		LocalServerConnector serverConnector = new LocalServerConnector();
		return serverConnector;
	}
	
}
