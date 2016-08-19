package com.lunarsky.minipos.db.hibernate;

import com.lunarsky.minipos.interfaces.PersistenceId;

public class HibernatePersistenceId implements PersistenceId {

	private String id;
	
	public HibernatePersistenceId(final String id) {
		this.id = id;
	}
	
	public boolean hasId() {
		return (null != id);
	}
	
	public boolean equals(PersistenceId persistenceId) {
		final HibernatePersistenceId compareId = (HibernatePersistenceId)persistenceId;
		final boolean match = id.equals(compareId.getId());
		return match;
	}
	
	public String getId() {
		return id;
	}
	
}
