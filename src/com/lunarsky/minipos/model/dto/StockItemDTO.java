package com.lunarsky.minipos.model.dto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;

public class StockItemDTO extends PersistenceObjectDTO implements Comparable<StockItemDTO> {
	private static final Logger log = LogManager.getLogger();
	
	final String name;
	final boolean trackStockLevel;
	final Double stockLevel;
	
	public StockItemDTO(final PersistenceId id, final String name, final boolean trackStockLevel, final Double stockLevel) {
		super(id);
		
		assert(null != name);
		assert(null != stockLevel);
		
		this.name = name;
		this.trackStockLevel = trackStockLevel;
		this.stockLevel = stockLevel;
	}
	
	public StockItemDTO duplicate() {
		final StockItemDTO stockItem = new StockItemDTO(null,getName(),getTrackStockLevel(),getStockLevel());
		return stockItem;
	}
	
	public String getName() {
		assert(null != name);
		return name; 
		}
	
	public boolean getTrackStockLevel() {
		return trackStockLevel; 
		}
	
	public Double getStockLevel() {
		assert(null != stockLevel);
		return stockLevel; 
		}
	
	public int compareTo(StockItemDTO stockItem) {
		return getName().compareToIgnoreCase(stockItem.getName());
	}
}
