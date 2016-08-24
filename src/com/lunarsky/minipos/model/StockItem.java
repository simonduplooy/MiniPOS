package com.lunarsky.minipos.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.dto.PersistenceObjectDTO;

public class StockItem extends PersistenceObjectDTO implements Comparable<StockItem> {
	private static final Logger log = LogManager.getLogger();
	
	final String name;
	final boolean trackStockLevel;
	final Double stockLevel;
	
	public StockItem(final PersistenceId id, final String name, final boolean trackStockLevel, final Double stockLevel) {
		super(id);
		
		assert(null != name);
		assert(null != stockLevel);
		
		this.name = name;
		this.trackStockLevel = trackStockLevel;
		this.stockLevel = stockLevel;
	}
	
	public StockItem duplicate() {
		final StockItem stockItem = new StockItem(null,getName(),getTrackStockLevel(),getStockLevel());
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
	
	public int compareTo(StockItem stockItem) {
		return getName().compareToIgnoreCase(stockItem.getName());
	}
}
