package com.lunarsky.minipos.model.ui;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.dto.AccountDTO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Account extends PersistenceObject {
	private static final Logger log = LogManager.getLogger();
	
	private Date created;
	private final StringProperty nameProperty;
	
	public Account() {
		nameProperty = new SimpleStringProperty();
	}
	
	public Account(final PersistenceId id, final String name) {
		this();
		setId(id);
		setName(name);
	}
	
	public Account(final Account account) {
		this(account.getId(),account.getName());
	}
	
	public Account(final AccountDTO accountDTO) {
		this();
		setDTO(accountDTO);
	}
	
	public AccountDTO getDTO() {
		final AccountDTO accountDTO = new AccountDTO(getId().getDTO(),getCreated(),getName());
		return accountDTO;
	}
	
	public void setDTO(final AccountDTO accountDTO) {
		setId(new PersistenceId(accountDTO.getId()));
		setCreated(accountDTO.getCreated());
		setName(accountDTO.getName());
	}
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(final Date created) {
		this.created = created;
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
		return String.format("name:[%s] id:[%s] created:[%s]",getName(),getId(),getCreated());
	}

}
