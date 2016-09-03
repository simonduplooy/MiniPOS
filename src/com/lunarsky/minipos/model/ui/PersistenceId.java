package com.lunarsky.minipos.model.ui;

import com.lunarsky.minipos.model.dto.PersistenceIdDTO;

public class PersistenceId {

	private String id;
	
	public PersistenceId(final String id) {
		this.id = id;
	}
	
	public PersistenceId(PersistenceIdDTO dto) {
		this(dto.getId());
	}
	
	public PersistenceIdDTO getDTO() {
		return new PersistenceIdDTO(getId());
	}
	
	public void setDTO(final PersistenceIdDTO idDTO) {
		setId(idDTO.getId());
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
		assert(object instanceof PersistenceId);
		
		final PersistenceId persistenceId = (PersistenceId)object;
		
		final boolean match = id.equals(persistenceId.getId());
		return match;
	}
	
	@Override
	public String toString() {
		return String.format("%s",id);
	}
	
}
