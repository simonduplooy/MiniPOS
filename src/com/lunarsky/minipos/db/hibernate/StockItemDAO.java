package com.lunarsky.minipos.db.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Table;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.StockItem;

@Entity
@Table(name="stockitems")
public class StockItemDAO extends HibernateDAO {

	@Column(unique = true)
	private String name;
	private boolean trackStockLevel;
	private Double stockLevel;

	//used by hibernate
	public StockItemDAO() {}
	
	public static StockItemDAO load(final EntityManager entityManager, final HibernatePersistenceId id) {
		assert(null != entityManager);
		assert(null != id);
		
		final StockItemDAO stockItemDAO = entityManager.find(StockItemDAO.class,id.getId());
		if(null == stockItemDAO) { throw new EntityNotFoundException(String.format("StockItem %s not found",id));}
		stockItemDAO.setEntityManager(entityManager);
		return stockItemDAO;
	}
	
	public static StockItemDAO create(final EntityManager entityManager, final StockItem stockItem) {
		assert(null != entityManager);
		assert(null != stockItem);
		
		final StockItemDAO stockItemDAO = new StockItemDAO(entityManager,stockItem);
		entityManager.persist(stockItemDAO);
		
		return stockItemDAO;
	}
	
	public StockItem getStockItem() {
		final StockItem stockItem = new StockItem(getId(),getName(),getTrackStockLevel(),getStockLevel()); 
		return stockItem;
	}

	public void setStockItem(final StockItem stockItem) {
		assert(null != stockItem);
		
		setId(stockItem.getId());
		setName(stockItem.getName());
		setTrackStockLevel(stockItem.getTrackStockLevel());
		setStockLevel(stockItem.getStockLevel());
	}

	private StockItemDAO(final EntityManager entityManager,final StockItem stockItem) {
		super(entityManager);
		setStockItem(stockItem);
	}
	
	private String getName() {
		assert(null != name);
		return name; 
		}
	
	private void setName(final String name) {
		assert(null != name);
		this.name = name; 
		}
	
	private boolean getTrackStockLevel() { 
		return trackStockLevel; 
		}
	
	private void setTrackStockLevel(final boolean trackStockLevel) {
		this.trackStockLevel = trackStockLevel; 
		}
	
	private Double getStockLevel() {
		assert(null != stockLevel);
		return stockLevel; 
		}
	
	private void setStockLevel(final Double stockLevel) {
		assert(null != stockLevel);
		this.stockLevel = stockLevel;
	}
		
}
