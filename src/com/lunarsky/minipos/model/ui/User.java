package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.dto.UserDTO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User extends PersistenceObject implements Comparable<User> {
	private static final Logger log = LogManager.getLogger();
	
	private final StringProperty nameProperty;
	private final StringProperty passwordProperty;
	
	/**************************************************************************
	 * Constructors
	 **************************************************************************/
	private User(final PersistenceId id, final String name, final String password) {
		super(id);
		nameProperty = new SimpleStringProperty(name);
		passwordProperty = new SimpleStringProperty(password);		
	}

	public User(final String name, final String password) {
		this(null,name,password);		
	}
		
	public User(final User user) {
		this(user.getId(),user.getName(),user.getPassword());
	}
	
	public User(final UserDTO dto) {
		this(dto.getId(),dto.getName(),dto.getPassword());
	}
	
	/**************************************************************************
	 * Factories
	 **************************************************************************/
	
	public UserDTO createDTO() {
		final UserDTO dto = new UserDTO(getId(),getName(),getPassword());
		return dto;
	}
	
	public User duplicate() {
		//do not copy the id
		return new User(null,getName(),getPassword());
	}
	
	/**************************************************************************
	 * Getters & Setters
	 **************************************************************************/
	public void set(final User user) {
		setId(user.getId());
		setName(user.getName());
		setPassword(user.getPassword());
	}
	
	public void set(final UserDTO dto) {
		setId(dto.getId());
		setName(dto.getName());
		setPassword(dto.getPassword());
	}
		
	public StringProperty nameProperty() {
		assert(null != nameProperty);
		return nameProperty;
	}

	public String getName() { 
		return nameProperty().getValue(); 
		}
	
	public void setName(final String name) {
		assert(null != name);
		nameProperty().setValue(name);
	}
	
	public StringProperty passwordProperty() {
		assert(null != passwordProperty);
		return passwordProperty;
	}
	
	public String getPassword() {
		return passwordProperty().getValue(); 
		}
	
	public void setPassword(final String password) {
		assert(null != password);
		passwordProperty().setValue(password);
	}
	
	/**************************************************************************
	 * Utilities
	 **************************************************************************/
	//Implement Comparable
	public int compareTo(final User user) {
		return getName().compareToIgnoreCase(user.getName());
	}
	
	@Override
	public String toString() {
		return String.format("name:[%s] id:[%s] password[%s]",getName(),getId(),getPassword());
	}
}
