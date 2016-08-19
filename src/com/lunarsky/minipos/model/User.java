package com.lunarsky.minipos.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;

//Almost Immutable
public class User extends PersistenceObject implements Comparable<User> {
	private static final Logger log = LogManager.getLogger();
	
	private final String name;
	private final String password;
	private final Role role;
	
	public User(final PersistenceId id, final String name, final String password, final Role role) {
		super(id);
		
		assert(null != name);
		assert(null != password);
		assert(null != role);
		
		this.name= name;
		this.password = password;
		this.role = role;
	}
	
	public User duplicate() {
		//do not copy the id
		return new User(null,getName(),getPassword(),getRole());
	}
	
	public String getName() { return name; }	
	public String getPassword() { return password; }
	public Role getRole() { return role; }
	
	public int compareTo(User user) {
		return getName().compareToIgnoreCase(user.getName());
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
