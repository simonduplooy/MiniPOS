package com.lunarsky.minipos.model.dto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;

public class PersistenceObjectDTO {
	private static final Logger log = LogManager.getLogger();

	private PersistenceId id;
		
	protected PersistenceObjectDTO() {
		this(null);
	}
	//id can be null, must be assigned by persistence provider
	protected PersistenceObjectDTO(final PersistenceId id) { 
		setId(id); 
	}
	
	public boolean hasId() { 
		return id != null; 
	}
	
	public PersistenceId getId() {
		return id;
	}
	
	public void setId(final PersistenceId id) {
		assert(null == this.id);
		this.id = id;
	}
}
