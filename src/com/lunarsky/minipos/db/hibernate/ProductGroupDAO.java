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
import com.lunarsky.minipos.model.dto.PersistenceIdDTO;
import com.lunarsky.minipos.model.dto.ProductGroupDTO;
import com.lunarsky.minipos.model.ui.PersistenceId;

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
	
	public static ProductGroupDAO load(final EntityManager entityManager, final PersistenceIdDTO id) {

		final ProductGroupDAO groupDAO = entityManager.find(ProductGroupDAO.class,id.getId());
		if(null == groupDAO) { 
			throw new EntityNotFoundException(String.format("ProductGroup %s not found",id));
		}
		
		groupDAO.setEntityManager(entityManager);
		return groupDAO;
	}
	
	public static ProductGroupDAO create(final EntityManager entityManager, final ProductGroupDTO group) {

		final ProductGroupDAO groupDAO = new ProductGroupDAO(entityManager,group);
		entityManager.persist(groupDAO);
		
		return groupDAO;
	}
	
	private ProductGroupDAO(final EntityManager entityManager,final ProductGroupDTO group) {
		super(entityManager);
		setDTO(group);
	}
	
	public ProductGroupDTO getDTO() {
		return new ProductGroupDTO(getId(),getParentId(),getName());
	}
	
	public void setDTO(final ProductGroupDTO group) {
		setId(group.getId());
		setParentId(group.getParentId());
		setName(group.getName());
	}

	private PersistenceIdDTO getParentId() {

		final PersistenceIdDTO parentId;

		if(null == parentProductGroupDAO) {
			parentId = new PersistenceIdDTO(null);
		} else {
			parentId = parentProductGroupDAO.getId();
		}
		
		return parentId;
	}
	
	private void setParentId(final PersistenceIdDTO parentId) {
		parentProductGroupDAO = ProductGroupDAO.load(getEntityManager(),parentId);
	}
	
	private String getName() {
		return name;
	}
	
	private void setName(final String name) {
		this.name = name; 
	}
}
