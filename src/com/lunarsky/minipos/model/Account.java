package com.lunarsky.minipos.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;

public class Account extends PersistenceObject {
	private static final Logger log = LogManager.getLogger();
	
	private String name;

	public Account() {
		this(null);
	}

	public Account(final PersistenceId id) {
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
