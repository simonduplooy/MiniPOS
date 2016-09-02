package com.lunarsky.minipos.model.ui;

import com.lunarsky.minipos.model.dto.PersistenceIdDTO;

public class PersistenceId {

	private String id;
	
	public PersistenceId(final String id) {
		this.id = id;
	}
	
	public PersistenceId(PersistenceIdDTO dto) {
		setId(dto.getId());
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(final String id) {
		this.id = id;
	}
	
	public boolean hasId() {
		return (null != id);
	}
	
	@Override
	public boolean equals(Object object) {
		assert(null != object);
		assert(object instanceof PersistenceId);
		
		final PersistenceId id = (PersistenceId)object;
		
		final boolean match = this.id.equals(id.getId());
		return match;
	}
	
	@Override
	public String toString() {
		return String.format("%s",id);
	}
	
}
