package com.lunarsky.minipos.model.dto;

public class AccountDTO extends PersistenceObjectDTO {
	
	private final String name;

	public AccountDTO(final PersistenceIdDTO id, final String name) {
		super(id);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return String.format("name:[%s] id:[%s]",getName(),getId());
	}
	
}
