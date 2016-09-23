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
import com.lunarsky.minipos.model.dto.ProductDTO;
import com.lunarsky.minipos.model.ui.PersistenceId;

@Entity
@Table(	name="products", 
		uniqueConstraints = { @UniqueConstraint(columnNames = "name", name = "NAME_CONSTRAINT") }
		)
public class ProductDAO extends HibernateDAO {

	@ManyToOne(optional = false)
	@JoinColumn(name = "parentId", foreignKey = @ForeignKey(name = "FK_Products_ProductGroups"))
	private ProductGroupDAO parentProductGroupDAO;
	
	@Column(length = Const.MAX_TEXTFIELD_LENGTH, nullable = false)
	private String name;
	@Column(nullable = false)
	private Double price;

	//used by hibernate
	public ProductDAO() {}
	
	public static ProductDAO load(final EntityManager entityManager, final PersistenceIdDTO id) {

		final ProductDAO productDAO = entityManager.find(ProductDAO.class,id.getId());
		if(null == productDAO) { throw new EntityNotFoundException(String.format("Product %s not found",id));}
		productDAO.setEntityManager(entityManager);
		
		return productDAO;
	}
	
	public static ProductDAO create(final EntityManager entityManager, final ProductDTO product) {

		final ProductDAO productDAO = new ProductDAO(entityManager,product);
		entityManager.persist(productDAO);
		
		return productDAO;
	}
	
	private ProductDAO(final EntityManager entityManager,final ProductDTO product) {
		super(entityManager);
		setDTO(product);
	}
	
	
	public ProductDTO getDTO() {
		final ProductDTO product = new ProductDTO(getId(),getParentId(),getName(),getPrice()); 
		return product;
	}

	public void setDTO(final ProductDTO product) {
		assert(null != product);
		
		setId(product.getId());
		setParentId(product.getParentId());
		setName(product.getName());
		setPrice(product.getPrice());
	}

	private PersistenceIdDTO getParentId() {
		final PersistenceIdDTO parentId = parentProductGroupDAO.getId();
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
		
	private Double getPrice() {
		return price; 
		}
	
	private void setPrice(final Double price) {
		this.price = price;
	}
		
}
