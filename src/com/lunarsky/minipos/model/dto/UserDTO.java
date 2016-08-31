package com.lunarsky.minipos.model.dto;

import com.lunarsky.minipos.interfaces.PersistenceId;

public class UserDTO extends PersistenceObjectDTO  {
	
	private final String name;
	private final String password;
	
	/**************************************************************************
	 * Constructor
	 **************************************************************************/
	public UserDTO(final PersistenceId id, final String name, final String password) {
		super(id);
		
		assert(null != name);
		assert(null != password);
		
		this.name= name;
		this.password = password;
	}
	
	/**************************************************************************
	 * Getters
	 **************************************************************************/
	public String getName() { 
		return name; 
	}
	
	public String getPassword() { 
		return password; 
	}
	
	/**************************************************************************
	 * Utilities
	 **************************************************************************/
	@Override
	public String toString() {
		return String.format("name:[%s] id:[%s] password[%s]",getName(),getId(),getPassword());
	}
}
