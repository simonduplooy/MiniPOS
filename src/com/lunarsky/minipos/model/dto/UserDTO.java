package com.lunarsky.minipos.model.dto;

public class UserDTO extends PersistenceObjectDTO  {
	
	private final String name;
	private final String password;
	
	public UserDTO(final PersistenceIdDTO id, final String name, final String password) {
		super(id);

		this.name = name;
		this.password = password;
	}
	
	public String getName() { 
		return name; 
	}
	
	public String getPassword() { 
		return password; 
	}
	
	@Override
	public String toString() {
		return String.format("name:[%s] id:[%s] password[%s]",getName(),getId(),getPassword());
	}
}
