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
import com.lunarsky.minipos.model.dto.ProductSaleDTO;

@Entity
@Table(	name="productsales" )
public class ProductSaleDAO extends HibernateDAO {

	@ManyToOne(optional = false)
	@JoinColumn(name = "productId", foreignKey = @ForeignKey(name = "FK_ProductSales_Products"))
	private ProductDAO product;
	
	@Column(nullable = false)
	private Integer count;
	
	@Column(nullable = false)
	private Double discount;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "orderId", foreignKey = @ForeignKey(name = "FK_ProductSales_Orders"))
	private SaleOrderDAO saleOrder;
	
	/*********************************************************************
	 * Public
	 ********************************************************************/
	//used by Hibernate
	public ProductSaleDAO() {}
	
	public static ProductSaleDAO create(final EntityManager entityManager, final SaleOrderDAO order, final ProductSaleDTO sale) {
		
		final ProductSaleDAO saleDAO = new ProductSaleDAO(entityManager,sale);
		saleDAO.saleOrder = order;
		entityManager.persist(saleDAO);
		
		return saleDAO;
	}
	
	public static ProductSaleDAO update(final EntityManager entityManager, final ProductSaleDTO sale) {
		
		final ProductSaleDAO saleDAO = load(entityManager,sale.getId());
		saleDAO.setDTO(sale);
		entityManager.persist(saleDAO);
		
		return saleDAO;
	}
	
	/*********************************************************************
	 * Private
	 ********************************************************************/
	private static ProductSaleDAO load(final EntityManager entityManager, final PersistenceIdDTO id) {
		
		final ProductSaleDAO sale = entityManager.find(ProductSaleDAO.class,id.getId());
		if(null == sale) { 
			throw new EntityNotFoundException(String.format("ProductSale %s not found",id));
			}
		sale.setEntityManager(entityManager);
		
		return sale;
	}
	
	private ProductSaleDAO(final EntityManager entityManager, final ProductSaleDTO sale) {
		super(entityManager);
		setDTO(sale);
	}
	
	public ProductSaleDTO getDTO() {
		final ProductSaleDTO sale = new ProductSaleDTO(getId(),product.getDTO(),count,discount);
		return sale;
	}
	
	private void setDTO(final ProductSaleDTO sale) {
		setId(sale.getId());
		this.product = ProductDAO.load(getEntityManager(),sale.getProduct().getId());
		this.count = sale.getCount();
		this.discount = sale.getDiscount();
	}	
			
}
