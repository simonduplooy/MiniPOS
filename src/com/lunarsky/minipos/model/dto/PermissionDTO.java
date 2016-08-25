package com.lunarsky.minipos.model.dto;

public enum PermissionDTO {
	
	CAN_VOID("CAN_VOID"),
	CAN_MANAGE_USERS("CAN_MANAGE_USERS"),
	CAN_CONFIGURE_UI("CAN_CONFIGURE_UI");
	
	private final String name;
	
	private PermissionDTO(final String name) {
		assert(null != name);
		this.name = name;
	}
	
	public String getName() {
		assert(null != name);
		return name;
	}

}
