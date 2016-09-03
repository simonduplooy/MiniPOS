package com.lunarsky.minipos.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.dto.PersistenceIdDTO;
import com.lunarsky.minipos.model.dto.ProductDTO;

public class ProductManager {
	private static final Logger log = LogManager.getLogger();
	
	public static List<ProductDTO> getProducts(final EntityManager entityManager) {
		log.debug("Getting Products");
		
		final Query query = entityManager.createQuery("from ProductDAO");
		final List<ProductDAO> resultList = query.getResultList();

		final List<ProductDTO> products = new ArrayList<ProductDTO>();
		for(ProductDAO result: resultList) {
			products.add(result.getDTO());
		}
		
		return products;
	}
	
	public static ProductDTO save(final EntityManager entityManager, final ProductDTO product) {
		log.debug("save() {}",product);
		
		final ProductDAO productDAO;
		if(product.hasId()) {
			productDAO = ProductDAO.load(entityManager,product.getId());
			productDAO.setDTO(product);
		} else {
			productDAO = ProductDAO.create(entityManager,product);
		}
		
		final ProductDTO updatedProduct = productDAO.getDTO();		
		return updatedProduct;
	}

	public static void delete(final EntityManager entityManager, final PersistenceIdDTO id) {
		log.debug("Deleting Product {}",id);
		
		final ProductDAO productDAO = entityManager.find(ProductDAO.class,id.getId());
		if(null == productDAO) { 
			throw new EntityNotFoundException(String.format("Product %s not found",id)); 
			}
		entityManager.remove(productDAO);
	}	
	
}
