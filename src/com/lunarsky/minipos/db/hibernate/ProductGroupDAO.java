package com.lunarsky.minipos.db.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.ProductButtonGroupConfig;
import com.lunarsky.minipos.model.ProductGroup;

@Entity
@Table(	name="productgroups",
		uniqueConstraints = { @UniqueConstraint(columnNames = "name", name = "NAME_CONSTRAINT") }
		)
public class ProductGroupDAO extends HibernateDAO {

	@ManyToOne(optional = true)
	@JoinColumn(name = "parentId", foreignKey = @ForeignKey(name = "FK_ProductGroups_ProductGroups"))
	private ProductGroupDAO parentProductGroupDAO;
	@Column(nullable = false, length = Const.MAX_TEXTFIELD_LENGTH)
	private String name;
	
	//used by hibernate
	public ProductGroupDAO() {}
	
	public static ProductGroupDAO load(final EntityManager entityManager, final HibernatePersistenceId id) {
		assert(null != entityManager);
		assert(null != id);
		
		final ProductGroupDAO productGroupDAO = entityManager.find(ProductGroupDAO.class,id.getId());
		if(null == productGroupDAO) { 
			throw new EntityNotFoundException(String.format("ProductGroup %s not found",id));
		}
		
		productGroupDAO.setEntityManager(entityManager);
		return productGroupDAO;
	}
	
	public static ProductGroupDAO create(final EntityManager entityManager, final ProductGroup group) {
		assert(null != entityManager);
		assert(null != group);
		
		final ProductGroupDAO productGroupDAO = new ProductGroupDAO(entityManager,group);
		entityManager.persist(productGroupDAO);
		
		return productGroupDAO;
	}
	
	private ProductGroupDAO(final EntityManager entityManager,final ProductGroup group) {
		super(entityManager);
		setProductGroup(group);
	}
	
	public ProductGroup getProductGroup() {
		final ProductGroup group = new ProductGroup(); 
		group.setId(getId());
		group.setParentId(getParentId());
		group.setName(getName());
		return group;
	}
	
	public void setProductGroup(final ProductGroup group) {
		assert(null != group);
		
		setId(group.getId());
		setParentId(group.getParentId());
		setName(group.getName());
	}

	private PersistenceId getParentId() {
		if(null == parentProductGroupDAO) {
			return null;
		}
		final PersistenceId parentId = parentProductGroupDAO.getId();
		return parentId;
	}
	
	private void setParentId(final PersistenceId parentId) {
		if(null != parentId) {
			this.parentProductGroupDAO = ProductGroupDAO.load(getEntityManager(),(HibernatePersistenceId)parentId);
		}
	}
	
	private String getName() {
		assert(null != name);
		return name;
	}
	
	private void setName(final String name) {
		if(null==name) {throw new IllegalArgumentException();}
		if(null!=this.name) {throw new IllegalArgumentException();}
		this.name = name; 
	}
}
