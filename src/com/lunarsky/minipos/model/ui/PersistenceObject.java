package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;

public class PersistenceObject {
	private static final Logger log = LogManager.getLogger();

	private PersistenceId id;
		
	protected PersistenceObject() {
		this(null);
	}
	
	//id can be null, must be assigned by persistence provider
	protected PersistenceObject(final PersistenceId id) { 
		setId(id); 
	}
	
	public boolean hasId() { 
		return id != null; 
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

		if((null==object)||(null==id)) {
			return false;
		}
		
		assert(object instanceof PersistenceObject);
		final PersistenceObject persistenceObject = (PersistenceObject)object;
		final PersistenceId persistenceId = (PersistenceId)persistenceObject.getId();
		
		final boolean match = id.equals(persistenceId);
		return match;
	}
	
}
