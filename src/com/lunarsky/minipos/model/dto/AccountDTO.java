package com.lunarsky.minipos.model.dto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;

public class AccountDTO extends PersistenceObjectDTO {
	private static final Logger log = LogManager.getLogger();
	
	private String name;

	public AccountDTO() {
		this(null);
	}

	public AccountDTO(final PersistenceId id) {
		super(id);
	}
	
	public String getName() {
		assert(null != name);
		return name;
	}

	public void setName(final String name) {
		assert(null != name);
		assert(null == this.name);
		this.name = name;
	}
			
	@Override
	public String toString() {
		return getName();
	}
	
}
