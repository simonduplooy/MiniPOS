package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.dto.SaleDTO;

public class OpenItemSale extends Sale {
	private static final Logger log = LogManager.getLogger();
	
	private static final String OPEN_ITEM_DESCRIPTION = "Open Item";
	
	public OpenItemSale() {
		setDescription(OPEN_ITEM_DESCRIPTION);
	}
	
	@Override
	public SaleDTO getDTO() {
		//TODO
		return null;
	}
	
	@Override
	public String toString() {
		return String.format("description:[%s] total:[%s]",getDescription(),getTotal());
	}
}
