package com.lunarsky.minipos.model.dto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/* TODO 
 * This is MySQL specific, it should be generalized
 * Data Validation is Needed
 */
public class PersistenceConfigDTO {
	private static final Logger log = LogManager.getLogger();
	
	private final String server;
	private final String port;
	private final String database;
	private final String username;
	private final String password;
	
	public PersistenceConfigDTO(String server, String port, String database, String username, String password) {
		this.server = server;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
	}
	
	public String getServer() { return server; }
	public String getPort() { return port; }
	public String getDatabase() { return database; }
	public String getUsername() { return username; }
	public String getPassword() { return password; }	
}
