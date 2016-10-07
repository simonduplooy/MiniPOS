package com.lunarsky.minipos.model.dto;

import java.util.Date;

public class AccountDTO extends PersistenceObjectDTO {
	
	private final Date created;
	private final String name;

	public AccountDTO(final PersistenceIdDTO id,final Date created,final String name) {
		super(id);
		this.created = created;
		this.name = name;
	}
	
	public Date getCreated() {
		return created;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return String.format("name:[%s] id:[%s] created:[%s]",getName(),getId(),getCreated());
	}
	
}
