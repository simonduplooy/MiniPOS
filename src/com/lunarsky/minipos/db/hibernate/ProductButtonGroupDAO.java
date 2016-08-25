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
import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.dto.ProductGroupButtonConfigDTO;

@Entity
@Table(	name="productbuttongroups")
public class ProductButtonGroupDAO extends HibernateDAO {

	@ManyToOne(optional = true)
	@JoinColumn(name = "parentId", foreignKey = @ForeignKey(name = "FK_ProductButtonGroups_ProductButtonGroups"))
	private ProductButtonGroupDAO parentButtonGroupDAO;
	@Column(nullable = false, length = Const.MAX_TEXTFIELD_LENGTH)
	private String name;
	@Column(nullable = false)
	private Integer columnIdx;
	@Column(nullable = false)
	private Integer rowIdx;
	
	//used by hibernate
	public ProductButtonGroupDAO() {}
	
	public static ProductButtonGroupDAO load(final EntityManager entityManager, final HibernatePersistenceId id) {
		assert(null != entityManager);
		assert(null != id);
		
		final ProductButtonGroupDAO productButtonGroupDAO = entityManager.find(ProductButtonGroupDAO.class,id.getId());
		if(null == productButtonGroupDAO) { 
			throw new EntityNotFoundException(String.format("ProductButtonGroup %s not found",id));
		}
		
		productButtonGroupDAO.setEntityManager(entityManager);
		return productButtonGroupDAO;
	}
	
	public static ProductButtonGroupDAO create(final EntityManager entityManager, final ProductGroupButtonConfigDTO buttonConfig) {
		assert(null != entityManager);
		assert(null != buttonConfig);
		
		final ProductButtonGroupDAO productButtonGroupDAO = new ProductButtonGroupDAO(entityManager,buttonConfig);
		entityManager.persist(productButtonGroupDAO);
		
		return productButtonGroupDAO;
	}
	
	private ProductButtonGroupDAO(final EntityManager entityManager,final ProductGroupButtonConfigDTO buttonConfig) {
		super(entityManager);
		setConfig(buttonConfig);
	}
	
	public ProductGroupButtonConfigDTO getConfig() {
		final ProductGroupButtonConfigDTO buttonConfig = new ProductGroupButtonConfigDTO(getId(),getParentId(),getName(),getColumnIndex(),getRowIndex()); 
		return buttonConfig;
	}
	
	public void setConfig(final ProductGroupButtonConfigDTO buttonConfig) {
		assert(null != buttonConfig);
		
		setId(buttonConfig.getId());
		setParentId(buttonConfig.getParentId());
		setName(buttonConfig.getName());
		setColumnIndex(buttonConfig.getColumnIndex());
		setRowIndex(buttonConfig.getRowIndex());
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
	
	private String getName() {
		assert(null != name);
		return name;
	}
	
	private void setName(final String name) {
		assert(null != name);
		this.name = name; 
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
