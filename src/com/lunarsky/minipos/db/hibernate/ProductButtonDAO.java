package com.lunarsky.minipos.db.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.Product;
import com.lunarsky.minipos.model.ProductButtonConfig;

@Entity
@Table(	name="productbuttons")
public class ProductButtonDAO extends HibernateDAO {

	@ManyToOne (optional = false)
	ProductDAO product;
	@Column(nullable = false )
	Integer columnIdx;
	@Column(nullable = false )
	Integer rowIdx;

	//used by hibernate
	public ProductButtonDAO() {}
	
	public static ProductButtonDAO load(final EntityManager entityManager, final HibernatePersistenceId id) {
		assert(null != entityManager);
		assert(null != id);
		
		final ProductButtonDAO productButtonDAO = entityManager.find(ProductButtonDAO.class,id.getId());
		if(null == productButtonDAO) { 
			throw new EntityNotFoundException(String.format("ProductButton %s not found",id));
		}
		
		productButtonDAO.setEntityManager(entityManager);
		return productButtonDAO;
	}
	
	public static ProductButtonDAO create(final EntityManager entityManager, final ProductButtonConfig buttonConfig) {
		assert(null != entityManager);
		assert(null != buttonConfig);
		
		final ProductButtonDAO productButtonDAO = new ProductButtonDAO(entityManager,buttonConfig);
		entityManager.persist(productButtonDAO);
		
		return productButtonDAO;
	}
	
	public ProductButtonConfig getConfig() {
		final ProductButtonConfig buttonConfig = new ProductButtonConfig(getId(),getProduct(),getColumnIndex(),getRowIndex()); 
		return buttonConfig;
	}

	public void setConfig(final ProductButtonConfig buttonConfig) {
		assert(null != buttonConfig);
		
		setId(buttonConfig.getId());
		setProduct(buttonConfig.getProduct());
		setColumnIndex(buttonConfig.getColumnIndex());
		setRowIndex(buttonConfig.getRowIndex());
	}

	private ProductButtonDAO(final EntityManager entityManager,final ProductButtonConfig buttonConfig) {
		super(entityManager);
		setConfig(buttonConfig);
	}
	
	private Product getProduct() {
		assert(null != product);
		final Product product = this.product.getProduct();
		return product; 
		}
	
	private void setProduct(final Product product) {
		assert(null != product);
		final ProductDAO productDAO = ProductDAO.load(getEntityManager(),(HibernatePersistenceId)product.getId());
		this.product = productDAO; 
		}
	
	private Integer getColumnIndex() {
		assert(null != columnIdx);
		return columnIdx; 
		}
	
	private void setColumnIndex(final Integer columnIdx) {
		assert(null != columnIdx);
		this.columnIdx = columnIdx;
	}

	private Integer getRowIndex() {
		assert(null != rowIdx);
		return rowIdx; 
		}
	
	private void setRowIndex(final Integer rowIdx) {
		assert(null != rowIdx);
		this.rowIdx = rowIdx;
	}

}
