package com.lunarsky.minipos.model.dto;

public class PersistenceIdDTO {

	private String id;
	
	public PersistenceIdDTO(final String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(final String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return String.format("%s",id);
	}
	
}
