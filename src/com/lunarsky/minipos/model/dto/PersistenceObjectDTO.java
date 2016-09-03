package com.lunarsky.minipos.model.dto;

public class PersistenceObjectDTO {

	private final PersistenceIdDTO id;
	
	protected PersistenceObjectDTO(final PersistenceIdDTO id) { 
		this.id = id; 
	}
	
	public PersistenceIdDTO getId() {
		return id;
	}

	public boolean hasId() {
		return id.hasId();
	}
	
}
