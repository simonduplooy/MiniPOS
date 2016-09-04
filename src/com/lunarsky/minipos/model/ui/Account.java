package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.dto.AccountDTO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Account extends PersistenceObject {
	private static final Logger log = LogManager.getLogger();
	
	private final StringProperty nameProperty;
	
	public Account() {
		nameProperty = new SimpleStringProperty();
	}
	
	public Account(final String name) {
		this();
		setName(name);
	}
	
	public Account(final PersistenceId id, final String name) {
		this(name);
		setId(id);
	}
	
	public Account(final Account account) {
		this(account.getId(),account.getName());
	}
	
	public Account(final AccountDTO accountDTO) {
		this();
		setDTO(accountDTO);
	}
	
	public AccountDTO getDTO() {
		final AccountDTO accountDTO = new AccountDTO(getId().getDTO(),getName());
		return accountDTO;
	}
	
	public void setDTO(final AccountDTO accountDTO) {
		setId(new PersistenceId(accountDTO.getId()));
		setName(accountDTO.getName());
	}
	
	public StringProperty nameProperty() {
		return nameProperty;
	}
	
	public String getName() {
		return nameProperty.getValue();
	}
	
	public void setName(final String name) {
		nameProperty.setValue(name);
	}
	
	@Override
	public String toString() {
		return String.format("name:[%s] id:[%s]",getName(),getId());
	}

}
