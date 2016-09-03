package com.lunarsky.minipos.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.dto.PersistenceIdDTO;
import com.lunarsky.minipos.model.dto.ProductButtonConfigDTO;

public class ProductButtonManager {
	private static final Logger log = LogManager.getLogger();
	
	public static List<ProductButtonConfigDTO> getConfigs(final EntityManager entityManager) {
		log.debug("getButtonConfigs()");
		
		final Query query = entityManager.createQuery("from ProductButtonDAO");
		final List<ProductButtonDAO> resultList = query.getResultList();

		final List<ProductButtonConfigDTO> buttonConfigs = new ArrayList<ProductButtonConfigDTO>();
		for(ProductButtonDAO result: resultList) {
			buttonConfigs.add(result.getDTO());
		}
		
		return buttonConfigs;
	}
	
	public static ProductButtonConfigDTO save(final EntityManager entityManager, final ProductButtonConfigDTO config) {
		log.debug("save() {}",config);
		
		final ProductButtonDAO buttonDAO;
		if(config.hasId()) {
			buttonDAO = ProductButtonDAO.load(entityManager,config.getId());
			buttonDAO.setDTO(config);
		} else {
			buttonDAO = ProductButtonDAO.create(entityManager,config);
		}
		
		final ProductButtonConfigDTO updatedConfig = buttonDAO.getDTO();		
		return updatedConfig;
	}

	public static void delete(final EntityManager entityManager, final PersistenceIdDTO id) {
		log.debug("delete() {}",id);
		
		final ProductButtonDAO buttonDAO = entityManager.find(ProductButtonDAO.class,id.getId());
		if(null == buttonDAO) { 
			throw new EntityNotFoundException(String.format("ProductButton %s not found",id)); 
			}
		entityManager.remove(buttonDAO);
	}	
	
}
