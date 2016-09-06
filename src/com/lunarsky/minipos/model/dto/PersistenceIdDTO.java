package com.lunarsky.minipos.model.dto;

public class PersistenceIdDTO {

	private final String id;
	
	public PersistenceIdDTO() {
		id = null;
	}
	
	public PersistenceIdDTO(final String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public boolean hasId() {
		return (null != id);
	}
	
	@Override
	public String toString() {
		return String.format("%s",id);
	}
	
}
