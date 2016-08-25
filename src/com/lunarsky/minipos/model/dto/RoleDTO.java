package com.lunarsky.minipos.model.dto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;

public class RoleDTO extends PersistenceObjectDTO implements Comparable<RoleDTO> {
	private static final Logger log = LogManager.getLogger();

	private final String name;
	private final boolean canVoid;
	private final boolean canManageUsers;
	
	public RoleDTO(final PersistenceId id, final String name, final boolean canVoid, final boolean canManageUsers) {
		super(id);
		this.name = name;
		this.canVoid = canVoid;
		this.canManageUsers = canManageUsers;
	}
	
	public RoleDTO duplicate() {
		//do not copy the id
		return new RoleDTO(null,getName(),getCanVoid(),getCanManageUsers());
	}
	
	public String getName() { return name; }
	public boolean getCanVoid() { return canVoid; }
	public boolean getCanManageUsers() { return canManageUsers; }
	
	public int compareTo(RoleDTO role) {
		return getName().compareToIgnoreCase(role.getName());
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
