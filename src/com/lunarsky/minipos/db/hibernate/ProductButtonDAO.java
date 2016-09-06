package com.lunarsky.minipos.db.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.dto.PersistenceIdDTO;
import com.lunarsky.minipos.model.dto.ProductButtonConfigDTO;
import com.lunarsky.minipos.model.dto.ProductDTO;
import com.lunarsky.minipos.model.ui.PersistenceId;

@Entity
@Table(	name="productButtons")
public class ProductButtonDAO extends HibernateDAO {

	@ManyToOne (optional = true)
	@JoinColumn(name="parentId", foreignKey = @ForeignKey(name = "FK_ProductButtons_ProductGroupButtons"))
	ProductGroupButtonDAO parentGroupButtonDAO;
	@ManyToOne (optional = false)
	@JoinColumn(name="productId", foreignKey = @ForeignKey(name = "FK_ProductButtons_Product"))
	ProductDAO productDAO;
	@Column(nullable = false )
	Integer columnIdx;
	@Column(nullable = false )
	Integer rowIdx;

	//used by hibernate
	public ProductButtonDAO() {}
	
	public static ProductButtonDAO load(final EntityManager entityManager, final PersistenceIdDTO id) {

		final ProductButtonDAO productButtonDAO = entityManager.find(ProductButtonDAO.class,id.getId());
		if(null == productButtonDAO) { 
			throw new EntityNotFoundException(String.format("ProductButton %s not found",id));
		}
		
		productButtonDAO.setEntityManager(entityManager);
		return productButtonDAO;
	}
	
	public static ProductButtonDAO create(final EntityManager entityManager, final ProductButtonConfigDTO buttonConfig) {

		final ProductButtonDAO productButtonDAO = new ProductButtonDAO(entityManager,buttonConfig);
		entityManager.persist(productButtonDAO);
		
		return productButtonDAO;
	}
	
	private ProductButtonDAO(final EntityManager entityManager,final ProductButtonConfigDTO buttonConfig) {
		super(entityManager);
		setDTO(buttonConfig);
	}

	public ProductButtonConfigDTO getDTO() {
		final ProductButtonConfigDTO buttonConfig = new ProductButtonConfigDTO(getId(),getParentId(),getProduct(),getColumnIndex(),getRowIndex());  
		return buttonConfig;
	}

	public void setDTO(final ProductButtonConfigDTO buttonConfig) {

		setId(buttonConfig.getId());
		setParentId(buttonConfig.getParentId());
		setProduct(buttonConfig.getProduct());
		setColumnIndex(buttonConfig.getColumnIndex());
		setRowIndex(buttonConfig.getRowIndex());
	}

	private PersistenceIdDTO getParentId() {
		
		final PersistenceIdDTO parentId;
		
		if(null != parentGroupButtonDAO) {
			parentId = parentGroupButtonDAO.getId();
		} else {
			parentId =  new PersistenceIdDTO(null);
		}
		
		return parentId;
	}
	
	private void setParentId(final PersistenceIdDTO parentId) {
		if(parentId.hasId()) {
			parentGroupButtonDAO = ProductGroupButtonDAO.load(getEntityManager(),parentId);
		}
	}
	
	private ProductDTO getProduct() {
		final ProductDTO product = productDAO.getDTO();
		return product; 
		}
	
	private void setProduct(final ProductDTO product) {
		productDAO = ProductDAO.load(getEntityManager(),product.getId());
		}
	
	private Integer getColumnIndex() {
		return columnIdx; 
		}
	
	private void setColumnIndex(final Integer columnIdx) {
		this.columnIdx = columnIdx;
	}

	private Integer getRowIndex() {
		return rowIdx; 
		}
	
	private void setRowIndex(final Integer rowIdx) {
		this.rowIdx = rowIdx;
	}

}
