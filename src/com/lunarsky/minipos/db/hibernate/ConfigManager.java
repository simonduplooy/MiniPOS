package com.lunarsky.minipos.db.hibernate;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.dto.PersistenceConfigDTO;

public class ConfigManager {
	private static final Logger log = LogManager.getLogger();
	
	private static final String DB_SERVER_PROPERTY = "Server";
	private static final String DB_PORT_PROPERTY = "Port";
	private static final String DB_DATABASE_PROPERTY = "Database";
	private static final String DB_USERNAME_PROPERTY = "Username";
	private static final String DB_PASSWORD_PROPERTY = "Password";
	
	private static final String DB_SERVER_DEFAULT = "localhost";
	private static final String DB_PORT_DEFAULT = "3306";
	private static final String DB_DATABASE_DEFAULT= "minipos";
	private static final String DB_USERNAME_DEFAULT = "minipos";
	private static final String DB_PASSWORD_DEFAULT = "minipos";
	
	
	//prevent instantiation
	private ConfigManager() {
	}
	
	public static synchronized PersistenceConfigDTO getConfig() {
		//On Windows HKEY_CURRENT_USER/Software/Javasoft/Prefs/com/lunarsky/minipos/db/hibernate
		Preferences configPref = Preferences.userNodeForPackage(ConfigManager.class);
		String server = configPref.get(DB_SERVER_PROPERTY, DB_SERVER_DEFAULT);
		String port = configPref.get(DB_PORT_PROPERTY, DB_PORT_DEFAULT);
		String database = configPref.get(DB_DATABASE_PROPERTY, DB_DATABASE_DEFAULT);
		String username = configPref.get(DB_USERNAME_PROPERTY, DB_USERNAME_DEFAULT);
		String password = configPref.get(DB_PASSWORD_PROPERTY, DB_PASSWORD_DEFAULT);

		PersistenceConfigDTO config = new PersistenceConfigDTO(server,port,database,username,password);
		return config;
	}
	
	public static synchronized void setConfig(PersistenceConfigDTO config) {
		
		Preferences configPref = Preferences.userNodeForPackage(ConfigManager.class);
		configPref.put(DB_SERVER_PROPERTY, config.getServer());
		configPref.put(DB_PORT_PROPERTY, config.getPort());
		configPref.put(DB_DATABASE_PROPERTY, config.getDatabase());
		configPref.put(DB_USERNAME_PROPERTY, config.getUsername());
		configPref.put(DB_PASSWORD_PROPERTY, config.getPassword());
	}
	
}
