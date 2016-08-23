package com.lunarsky.minipos.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.Product;

public class ProductGroupManager {
	private static final Logger log = LogManager.getLogger();
	
	public static List<Product> getProducts(final EntityManager entityManager) {
		log.debug("Getting Products");
		
		final Query query = entityManager.createQuery("from ProductDAO");
		final List<ProductDAO> resultList = query.getResultList();

		final List<Product> products = new ArrayList<Product>();
		for(ProductDAO result: resultList) {
			products.add(result.getProduct());
		}
		
		return products;
	}
	
	public static Product save(final EntityManager entityManager, final Product product) {
		log.debug("Save Product {}",product.getName());
		
		final ProductDAO productDAO;
		if(product.hasId()) {
			productDAO = ProductDAO.load(entityManager,(HibernatePersistenceId)product.getId());
			productDAO.setProduct(product);
		} else {
			productDAO = ProductDAO.create(entityManager,product);
		}
		
		final Product updatedProduct = productDAO.getProduct();		
		return updatedProduct;
	}

	public static void delete(final EntityManager entityManager, final HibernatePersistenceId id) {
		log.debug("Deleting Product {}",id);
		
		final ProductDAO productDAO = entityManager.find(ProductDAO.class,id.getId());
		if(null == productDAO) { throw new EntityNotFoundException(String.format("Product %s not found",id)); }
		entityManager.remove(productDAO);
	}	
	
}
