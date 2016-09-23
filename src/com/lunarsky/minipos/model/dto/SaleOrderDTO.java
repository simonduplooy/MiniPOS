package com.lunarsky.minipos.model.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class SaleOrderDTO extends PersistenceObjectDTO {
	
	private final Date creationDate;
	private final List<SaleDTO> saleList;
	
	public SaleOrderDTO(final PersistenceIdDTO id, final Date creationDate, final List<SaleDTO> saleList) {
		super(id);

		this.creationDate = creationDate;
		this.saleList = saleList;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public List<SaleDTO> getSales() {
		return saleList;
	}
}
