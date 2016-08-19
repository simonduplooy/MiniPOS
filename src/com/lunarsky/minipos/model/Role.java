package com.lunarsky.minipos.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;

public class Role extends PersistenceObject implements Comparable<Role> {
	private static final Logger log = LogManager.getLogger();

	private final String name;
	private final boolean canVoid;
	private final boolean canManageUsers;
	
	public Role(final PersistenceId id, final String name, final boolean canVoid, final boolean canManageUsers) {
		super(id);
		this.name = name;
		this.canVoid = canVoid;
		this.canManageUsers = canManageUsers;
	}
	
	public Role duplicate() {
		//do not copy the id
		return new Role(null,getName(),getCanVoid(),getCanManageUsers());
	}
	
	public String getName() { return name; }
	public boolean getCanVoid() { return canVoid; }
	public boolean getCanManageUsers() { return canManageUsers; }
	
	public int compareTo(Role role) {
		return getName().compareToIgnoreCase(role.getName());
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
