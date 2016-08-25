package com.lunarsky.minipos.model.dto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;

public class UserDTO extends PersistenceObjectDTO implements Comparable<UserDTO> {
	private static final Logger log = LogManager.getLogger();
	
	private final String name;
	private final String password;
	private final RoleDTO role;
	
	public UserDTO(final PersistenceId id, final String name, final String password, final RoleDTO role) {
		super(id);
		
		assert(null != name);
		assert(null != password);
		assert(null != role);
		
		this.name= name;
		this.password = password;
		this.role = role;
	}
	
	public UserDTO duplicate() {
		//do not copy the id
		return new UserDTO(null,getName(),getPassword(),getRole());
	}
	
	public String getName() { return name; }	
	public String getPassword() { return password; }
	public RoleDTO getRole() { return role; }
	
	public int compareTo(UserDTO user) {
		return getName().compareToIgnoreCase(user.getName());
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
