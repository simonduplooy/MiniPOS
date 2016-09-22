package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	public User() {
		nameProperty = new SimpleStringProperty();
		passwordProperty = new SimpleStringProperty();
	}
	
	public User(final String name, final String password) {
		this();
		setName(name);
		setPassword(password);
	}
	
	public User(final PersistenceId id, final String name, final String password) {
		this(name,password);
		setId(id);
	}
	
	public User(final User user) {
		this(user.getId(),user.getName(),user.getPassword());
	}
	
	public User(final UserDTO userDTO) {
		this();
		setDTO(userDTO);
	}
	
	public void setDTO(final UserDTO userDTO) {
		setId(new PersistenceId(userDTO.getId()));
		setName(userDTO.getName());
		setPassword(userDTO.getPassword());
	}
	
	public UserDTO getDTO() {
		final UserDTO userDTO = new UserDTO(getId().getDTO(),getName(),getPassword());
		return userDTO;
	}
	
	public User duplicate() {
		//do not copy the id
		return new User(getName(),getPassword());
	}
	
	/**************************************************************************
	 * Getters & Setters
	 **************************************************************************/
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
