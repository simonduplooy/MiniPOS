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
import com.lunarsky.minipos.model.dto.ProductDTO;

@Entity
@Table(	name="products", 
		uniqueConstraints = { @UniqueConstraint(columnNames = "name", name = "NAME_CONSTRAINT") }
		)
public class ProductDAO extends HibernateDAO {

	@ManyToOne(optional = true)
	@JoinColumn(name = "parentId", foreignKey = @ForeignKey(name = "FK_Products_ProductGroups"))
	private ProductGroupDAO parentProductGroupDAO;
	
	@Column(length = Const.MAX_TEXTFIELD_LENGTH)
	private String name;
	private Double price;

	//used by hibernate
	public ProductDAO() {}
	
	public static ProductDAO load(final EntityManager entityManager, final HibernatePersistenceId id) {
		assert(null != entityManager);
		assert(null != id);
		
		final ProductDAO productDAO = entityManager.find(ProductDAO.class,id.getId());
		if(null == productDAO) { throw new EntityNotFoundException(String.format("Product %s not found",id));}
		productDAO.setEntityManager(entityManager);
		return productDAO;
	}
	
	public static ProductDAO create(final EntityManager entityManager, final ProductDTO product) {
		assert(null != entityManager);
		assert(null != product);
		
		final ProductDAO productDAO = new ProductDAO(entityManager,product);
		entityManager.persist(productDAO);
		
		return productDAO;
	}
	
	public ProductDTO getProduct() {
		final ProductDTO product = new ProductDTO(getId(),getParentId(),getName(),getPrice()); 
		return product;
	}

	public void setProduct(final ProductDTO product) {
		assert(null != product);
		
		setId(product.getId());
		setParentId(product.getParentId());
		setName(product.getName());
		setPrice(product.getPrice());
	}

	private ProductDAO(final EntityManager entityManager,final ProductDTO product) {
		super(entityManager);
		setProduct(product);
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
			parentProductGroupDAO = ProductGroupDAO.load(getEntityManager(),(HibernatePersistenceId)parentId);
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
		
	private Double getPrice() {
		assert(null != price);
		return price; 
		}
	
	private void setPrice(final Double price) {
		assert(null != price);
		this.price = price;
	}
		
}
