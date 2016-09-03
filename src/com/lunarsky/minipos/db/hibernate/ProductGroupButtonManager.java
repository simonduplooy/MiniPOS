package com.lunarsky.minipos.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.dto.PersistenceIdDTO;
import com.lunarsky.minipos.model.dto.ProductGroupButtonConfigDTO;

public class ProductGroupButtonManager {
	private static final Logger log = LogManager.getLogger();
	
	public static List<ProductGroupButtonConfigDTO> getConfigs(final EntityManager entityManager) {
		log.debug("getConfigs()");
		
		final Query query = entityManager.createQuery("from ProductGroupButtonDAO");
		final List<ProductGroupButtonDAO> resultList = query.getResultList();

		final List<ProductGroupButtonConfigDTO> buttonConfigs = new ArrayList<ProductGroupButtonConfigDTO>();
		for(ProductGroupButtonDAO result: resultList) {
			buttonConfigs.add(result.getDTO());
		}
		
		return buttonConfigs;
	}
	
	public static ProductGroupButtonConfigDTO save(final EntityManager entityManager, final ProductGroupButtonConfigDTO config) {
		log.debug("save() {}",config);
		
		final ProductGroupButtonDAO buttonDAO;
		if(config.hasId()) {
			buttonDAO = ProductGroupButtonDAO.load(entityManager,config.getId());
			buttonDAO.setDTO(config);
		} else {
			buttonDAO = ProductGroupButtonDAO.create(entityManager,config);
		}
		
		final ProductGroupButtonConfigDTO updatedConfig = buttonDAO.getDTO();		
		return updatedConfig;
	}

	public static void delete(final EntityManager entityManager, final PersistenceIdDTO id) {
		log.debug("delete() {}",id);
		
		final ProductGroupButtonDAO buttonDAO = entityManager.find(ProductGroupButtonDAO.class,id.getId());
		if(null == buttonDAO) { 
			throw new EntityNotFoundException(String.format("ProductGroupButton %s not found",id)); 
			}
		entityManager.remove(buttonDAO);
	}	
}
