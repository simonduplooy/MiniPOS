package com.lunarsky.minipos.db.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.dto.PersistenceIdDTO;
import com.lunarsky.minipos.model.dto.ProductGroupButtonConfigDTO;

@Entity
@Table(	name="productgroupbuttons")
public class ProductGroupButtonDAO extends HibernateDAO {

	@ManyToOne(optional = true)
	@JoinColumn(name = "parentId", foreignKey = @ForeignKey(name = "FK_ProductButtons_ProductGroupButtons"))
	private ProductGroupButtonDAO parentGroupButtonDAO;
	@Column(nullable = false, length = Const.MAX_TEXTFIELD_LENGTH)
	private String name;
	@Column(nullable = false)
	private Integer columnIdx;
	@Column(nullable = false)
	private Integer rowIdx;
	
	//used by hibernate
	public ProductGroupButtonDAO() {}
	
	public static ProductGroupButtonDAO load(final EntityManager entityManager, final PersistenceIdDTO id) {

		final ProductGroupButtonDAO productButtonGroupDAO = entityManager.find(ProductGroupButtonDAO.class,id.getId());
		if(null == productButtonGroupDAO) { 
			throw new EntityNotFoundException(String.format("ProductButtonGroup %s not found",id));
		}
		
		productButtonGroupDAO.setEntityManager(entityManager);
		return productButtonGroupDAO;
	}
	
	public static ProductGroupButtonDAO create(final EntityManager entityManager, final ProductGroupButtonConfigDTO buttonConfig) {

		final ProductGroupButtonDAO productButtonGroupDAO = new ProductGroupButtonDAO(entityManager,buttonConfig);
		entityManager.persist(productButtonGroupDAO);
		
		return productButtonGroupDAO;
	}
	
	private ProductGroupButtonDAO(final EntityManager entityManager,final ProductGroupButtonConfigDTO buttonConfig) {
		super(entityManager);
		setDTO(buttonConfig);
	}
	
	public ProductGroupButtonConfigDTO getDTO() {
		final ProductGroupButtonConfigDTO buttonConfig = new ProductGroupButtonConfigDTO(getId(),getParentId(),getName(),getColumnIndex(),getRowIndex()); 
		return buttonConfig;
	}
	
	public void setDTO(final ProductGroupButtonConfigDTO buttonConfig) {
		assert(null != buttonConfig);
		
		setId(buttonConfig.getId());
		setParentId(buttonConfig.getParentId());
		setName(buttonConfig.getName());
		setColumnIndex(buttonConfig.getColumnIndex());
		setRowIndex(buttonConfig.getRowIndex());
	}

	private PersistenceIdDTO getParentId() {
		final PersistenceIdDTO parentId;
		if(null != parentGroupButtonDAO) {
			parentId = parentGroupButtonDAO.getId();
		} else {
			parentId = new PersistenceIdDTO();
		}
		return parentId;
	}
	
	private void setParentId(final PersistenceIdDTO parentId) {
		if(parentId.hasId()) {
			parentGroupButtonDAO = ProductGroupButtonDAO.load(getEntityManager(),parentId);
		}
	}
	
	private String getName() {
		return name;
	}
	
	private void setName(final String name) {
		this.name = name; 
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
