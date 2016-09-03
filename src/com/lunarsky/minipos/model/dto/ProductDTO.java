package com.lunarsky.minipos.model.dto;

import com.lunarsky.minipos.model.ui.PersistenceId;

public class ProductDTO extends PersistenceObjectDTO {
	
	private final PersistenceIdDTO parentId;
	private final String name;
	private final Double price;
	
	public ProductDTO(final PersistenceIdDTO id, final PersistenceIdDTO parentId, final String name, final Double price) {
		super(id);

		this.parentId = parentId;
		this.name = name;
		this.price = price;
	}
	
	public PersistenceIdDTO getParentId() {
		return parentId;
	}
	
	public String getName() {
		return name;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public String toString() {
		return String.format("name:[%s] id:[%s] parentid:[%s] price:[%.2f]",getName(),getId(),getParentId(),getPrice());
	}
}
