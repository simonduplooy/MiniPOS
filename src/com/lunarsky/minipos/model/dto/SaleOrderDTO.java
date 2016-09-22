package com.lunarsky.minipos.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SaleOrderDTO extends PersistenceObjectDTO {
	
	private final LocalDateTime creationTime;
	private final List<SaleDTO> saleList;
	
	public SaleOrderDTO(final PersistenceIdDTO id, final LocalDateTime creationTime, final List<SaleDTO> saleList) {
		super(id);

		this.creationTime = creationTime;
		this.saleList = saleList;
	}
	
	public LocalDateTime getCreationTime() {
		return creationTime;
	}
	
	public List<SaleDTO> getSales() {
		return saleList;
	}
}
