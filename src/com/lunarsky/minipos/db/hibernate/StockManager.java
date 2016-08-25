package com.lunarsky.minipos.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.dto.StockItemDTO;

public class StockManager {
	private static final Logger log = LogManager.getLogger();
	
	public static List<StockItemDTO> getStock(final EntityManager entityManager) {

		log.debug("Getting StockItems");
		final Query query = entityManager.createQuery("from StockItemDAO");
		final List<StockItemDAO> resultList = query.getResultList();

		final List<StockItemDTO> stockItems = new ArrayList<StockItemDTO>();
		for(StockItemDAO result: resultList) {
			stockItems.add(result.getStockItem());
		}
		
		return stockItems;
	}
	
	public static StockItemDTO save(final EntityManager entityManager, final StockItemDTO stockItem) {
		log.debug("Save StockItem {}",stockItem.getName());
		
		final StockItemDAO stockItemDAO;
		if(stockItem.hasId()) {
			stockItemDAO = StockItemDAO.load(entityManager,(HibernatePersistenceId)stockItem.getId());
			stockItemDAO.setStockItem(stockItem);
		} else {
			stockItemDAO = StockItemDAO.create(entityManager,stockItem);
		}
		
		final StockItemDTO updatedStockItem = stockItemDAO.getStockItem();		
		return updatedStockItem;
	}

	public static void delete(final EntityManager entityManager, final HibernatePersistenceId id) {
		log.debug("Deleting StockItem {}",id);
		final StockItemDAO stockItemDAO = entityManager.find(StockItemDAO.class,id.getId());
		if(null == stockItemDAO) { throw new EntityNotFoundException(String.format("StockItem %s not found",id)); }
		entityManager.remove(stockItemDAO);
	}	
	
}
