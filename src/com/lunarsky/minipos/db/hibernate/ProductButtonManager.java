package com.lunarsky.minipos.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.Product;
import com.lunarsky.minipos.model.ProductButtonConfig;

public class ProductButtonManager {
	private static final Logger log = LogManager.getLogger();
	
	public static List<ProductButtonConfig> getConfigs(final EntityManager entityManager) {
		log.debug("getButtonConfigs()");
		
		final Query query = entityManager.createQuery("from ProductButtonDAO");
		final List<ProductButtonDAO> resultList = query.getResultList();

		final List<ProductButtonConfig> buttonConfigs = new ArrayList<ProductButtonConfig>();
		for(ProductButtonDAO result: resultList) {
			buttonConfigs.add(result.getConfig());
		}
		
		return buttonConfigs;
	}
	
	public static ProductButtonConfig save(final EntityManager entityManager, final ProductButtonConfig config) {
		log.debug("save() {}",config);
		
		final ProductButtonDAO buttonDAO;
		if(config.hasId()) {
			buttonDAO = ProductButtonDAO.load(entityManager,(HibernatePersistenceId)config.getId());
			buttonDAO.setConfig(config);
		} else {
			buttonDAO = ProductButtonDAO.create(entityManager,config);
		}
		
		final ProductButtonConfig updatedConfig = buttonDAO.getConfig();		
		return updatedConfig;
	}

	public static void delete(final EntityManager entityManager, final HibernatePersistenceId id) {
		log.debug("delete({})",id);
		
		final ProductButtonDAO buttonDAO = entityManager.find(ProductButtonDAO.class,id.getId());
		if(null == buttonDAO) { 
			throw new EntityNotFoundException(String.format("ProductButton %s not found",id)); 
			}
		entityManager.remove(buttonDAO);
	}	
	
}
