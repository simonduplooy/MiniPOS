package com.lunarsky.minipos.model.dto;

public class ProductButtonConfigDTO extends PersistenceObjectDTO {
	
	private final PersistenceIdDTO parentId; 
	private final ProductDTO product;
	private final Integer columnIdx;
	private final Integer rowIdx;
	
	public ProductButtonConfigDTO(final PersistenceIdDTO id, final PersistenceIdDTO parentId, final ProductDTO product, final Integer columnIdx, final Integer rowIdx) {
		super(id);

		this.parentId = parentId;
		this.product = product;
		this.columnIdx = columnIdx;
		this.rowIdx = rowIdx;
	}
	
	public PersistenceIdDTO getParentId() {
		return parentId;
	}
	
	public ProductDTO getProduct() {
		return product;
	}
	
	public Integer getColumnIndex() {
		return columnIdx;
	}
	
	public Integer getRowIndex() {
		return rowIdx;
	}
	
	public String toString() {
		return String.format("id:[%s] parentId:[%s] product:[%s] columnIdx:[%d] rowIdx:[%d]",getId(),getParentId(),getProduct(),getColumnIndex(),getRowIndex());
	}
}
