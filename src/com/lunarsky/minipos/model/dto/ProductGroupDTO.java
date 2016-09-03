package com.lunarsky.minipos.model.dto;

public class ProductGroupDTO extends PersistenceObjectDTO {
	
	private final PersistenceIdDTO parentId;
	private final String name;
	
	public ProductGroupDTO(final PersistenceIdDTO id, final PersistenceIdDTO parentId, final String name) {
		super(id);

		this.parentId = parentId;
		this.name = name;
	}
	
	public PersistenceIdDTO getParentId() {
		return parentId;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return String.format("name:[%s] id:[%s] parentid:[%s]",getName(),getId(),getParentId());
	}
}
