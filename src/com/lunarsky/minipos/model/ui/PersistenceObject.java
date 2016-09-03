package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.dto.PersistenceIdDTO;

public class PersistenceObject {
	private static final Logger log = LogManager.getLogger();

	private PersistenceId id;
			
	protected PersistenceObject(final PersistenceId id) { 
		setId(id); 
	}
	
	protected PersistenceObject() {
		
	}
	
	public PersistenceId getId() {
		return id;
	}
	
	public void setId(final PersistenceId id) {
		this.id = id;
	}
	
	//PersistenceObjects are compared by id
	@Override
	public boolean equals(final Object object) {
		assert(object instanceof PersistenceObject);
		
		final PersistenceObject persistenceObject = (PersistenceObject)object;
		final PersistenceId persistenceId = (PersistenceId)persistenceObject.getId();
		
		final boolean match = id.equals(persistenceId);
		return match;
	}
	
}
