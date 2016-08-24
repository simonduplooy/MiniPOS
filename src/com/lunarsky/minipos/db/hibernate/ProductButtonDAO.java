package com.lunarsky.minipos.db.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.ProductButtonConfig;
import com.lunarsky.minipos.model.dto.ProductDTO;

@Entity
@Table(	name="productButtons")
public class ProductButtonDAO extends HibernateDAO {

	@ManyToOne (optional = true)
	@JoinColumn(name="parentId", foreignKey = @ForeignKey(name = "FK_ProductButtons_ProductButtonGroups"))
	ProductButtonGroupDAO parentButtonGroupDAO;
	@ManyToOne (optional = false)
	@JoinColumn(name="productId", foreignKey = @ForeignKey(name = "FK_ProductButtons_Product"))
	ProductDAO productDAO;
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
		final ProductButtonConfig buttonConfig = new ProductButtonConfig(getId(),getParentId(),getProduct(),getColumnIndex(),getRowIndex());  
		return buttonConfig;
	}

	public void setConfig(final ProductButtonConfig buttonConfig) {
		assert(null != buttonConfig);
		
		setId(buttonConfig.getId());
		setParentId(buttonConfig.getParentId());
		setProduct(buttonConfig.getProduct());
		setColumnIndex(buttonConfig.getColumnIndex());
		setRowIndex(buttonConfig.getRowIndex());
	}

	private ProductButtonDAO(final EntityManager entityManager,final ProductButtonConfig buttonConfig) {
		super(entityManager);
		setConfig(buttonConfig);
	}

	private PersistenceId getParentId() {
		if(null == parentButtonGroupDAO) {
			return null;
		}
		final PersistenceId parentId = parentButtonGroupDAO.getId();
		return parentId;
	}
	
	private void setParentId(final PersistenceId parentId) {
		if(null != parentId) {
			this.parentButtonGroupDAO = ProductButtonGroupDAO.load(getEntityManager(),(HibernatePersistenceId)parentId);
		}
	}
	
	private ProductDTO getProduct() {
		assert(null != productDAO);
		final ProductDTO product = productDAO.getProduct();
		return product; 
		}
	
	private void setProduct(final ProductDTO product) {
		assert(null != product);
		this.productDAO = ProductDAO.load(getEntityManager(),(HibernatePersistenceId)product.getId());
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
