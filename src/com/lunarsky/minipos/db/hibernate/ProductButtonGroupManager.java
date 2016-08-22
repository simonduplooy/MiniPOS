package com.lunarsky.minipos.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.ProductButtonGroupConfig;

public class ProductButtonGroupManager {
	private static final Logger log = LogManager.getLogger();
	
	public static List<ProductButtonGroupConfig> getConfigs(final EntityManager entityManager) {
		log.debug("getConfigs()");
		
		final Query query = entityManager.createQuery("from ProductButtonGroupDAO");
		final List<ProductButtonGroupDAO> resultList = query.getResultList();

		final List<ProductButtonGroupConfig> buttonConfigs = new ArrayList<ProductButtonGroupConfig>();
		for(ProductButtonGroupDAO result: resultList) {
			buttonConfigs.add(result.getConfig());
		}
		
		return buttonConfigs;
	}
	
	public static ProductButtonGroupConfig save(final EntityManager entityManager, final ProductButtonGroupConfig config) {
		log.debug("save() {}",config);
		
		final ProductButtonGroupDAO buttonDAO;
		if(config.hasId()) {
			buttonDAO = ProductButtonGroupDAO.load(entityManager,(HibernatePersistenceId)config.getId());
			buttonDAO.setConfig(config);
		} else {
			buttonDAO = ProductButtonGroupDAO.create(entityManager,config);
		}
		
		final ProductButtonGroupConfig updatedConfig = buttonDAO.getConfig();		
		return updatedConfig;
	}

	public static void delete(final EntityManager entityManager, final HibernatePersistenceId id) {
		log.debug("delete() {}",id);
		
		final ProductButtonGroupDAO buttonDAO = entityManager.find(ProductButtonGroupDAO.class,id.getId());
		if(null == buttonDAO) { 
			throw new EntityNotFoundException(String.format("ProductButtonGroup %s not found",id)); 
			}
		entityManager.remove(buttonDAO);
	}	
}
