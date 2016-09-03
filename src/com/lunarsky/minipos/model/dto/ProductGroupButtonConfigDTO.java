package com.lunarsky.minipos.model.dto;

public class ProductGroupButtonConfigDTO extends PersistenceObjectDTO {
	
	private final PersistenceIdDTO parentId; 
	private final String name;
	private final Integer columnIdx;
	private final Integer rowIdx;
	
	public ProductGroupButtonConfigDTO(final PersistenceIdDTO id, final PersistenceIdDTO parentId, final String name, final Integer columnIdx, final Integer rowIdx) {
		super(id);

		this.parentId = parentId;
		this.name = name;
		this.columnIdx = columnIdx;
		this.rowIdx = rowIdx;
	}
	
	public PersistenceIdDTO getParentId() {
		return parentId;
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getColumnIndex() {
		return columnIdx;
	}
	
	public Integer getRowIndex() {
		return rowIdx;
	}
	
	public String toString() {
		return String.format("name:[%s] id:[%s] parentId:[%s] columnIdx:[%d] rowIdx:[%d]",getName(),getId(),getParentId(),getColumnIndex(),getRowIndex());
	}
}
