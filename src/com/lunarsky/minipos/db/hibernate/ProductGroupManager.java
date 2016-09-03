package com.lunarsky.minipos.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.dto.PersistenceIdDTO;
import com.lunarsky.minipos.model.dto.ProductGroupDTO;

public class ProductGroupManager {
	private static final Logger log = LogManager.getLogger();
	
	public static List<ProductGroupDTO> getGroups(final EntityManager entityManager) {
		log.debug("getGroups()");
		
		final Query query = entityManager.createQuery("from ProductGroupDAO");
		final List<ProductGroupDAO> resultList = query.getResultList();

		final List<ProductGroupDTO> groups = new ArrayList<ProductGroupDTO>();
		for(ProductGroupDAO result: resultList) {
			groups.add(result.getDTO());
		}
		
		return groups;
	}
	
	public static ProductGroupDTO save(final EntityManager entityManager, final ProductGroupDTO group) {
		log.debug("save() {}",group.getName());
		
		final ProductGroupDAO groupDAO;
		if(group.hasId()) {
			groupDAO = ProductGroupDAO.load(entityManager,group.getId());
			groupDAO.setDTO(group);
		} else {
			groupDAO = ProductGroupDAO.create(entityManager,group);
		}
		
		final ProductGroupDTO updatedGroup= groupDAO.getDTO();		
		return updatedGroup;
	}

	public static void delete(final EntityManager entityManager, final PersistenceIdDTO id) {
		log.debug("delete() {}",id);
		
		final ProductGroupDAO groupDAO = entityManager.find(ProductGroupDAO.class,id.getId());
		if(null == groupDAO) { 
			throw new EntityNotFoundException(String.format("ProductGroup %s not found",id)); 
			}
		entityManager.remove(groupDAO);
	}	
	
}
