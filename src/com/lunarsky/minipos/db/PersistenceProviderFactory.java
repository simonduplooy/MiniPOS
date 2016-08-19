package com.lunarsky.minipos.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.db.hibernate.HibernatePersistenceProvider;
import com.lunarsky.minipos.interfaces.PersistenceProvider;

public class PersistenceProviderFactory {
	private static final Logger log = LogManager.getLogger();
	
	//Prevent Instantiation
	private PersistenceProviderFactory() {
		log.error("PersistenceProviderFactory instantiated");
	}
	
	public static PersistenceProvider getPersistenceProvider() {
		HibernatePersistenceProvider persistenceProvider = new HibernatePersistenceProvider(); 
		return persistenceProvider;
	}
}
