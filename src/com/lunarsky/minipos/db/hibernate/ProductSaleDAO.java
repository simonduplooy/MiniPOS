package com.lunarsky.minipos.db.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.dto.PersistenceIdDTO;
import com.lunarsky.minipos.model.dto.ProductDTO;
import com.lunarsky.minipos.model.dto.ProductSaleDTO;

@Entity
@Table(	name="productsales" )
public class ProductSaleDAO extends HibernateDAO {

	@OneToOne(optional = false)
	@JoinColumn(name = "productId", foreignKey = @ForeignKey(name = "FK_ProductSales_Products"))
	private ProductDAO product;
	
	@Column(nullable = false)
	private Integer count;
	
	@Column(nullable = false)
	private Double discount;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "orderId", foreignKey = @ForeignKey(name = "FK_ProductSales_Orders"))
	private SaleOrderDAO saleOrder;
	
	//used by hibernate
	public ProductSaleDAO() {}
	
	public static ProductSaleDAO load(final EntityManager entityManager, final PersistenceIdDTO id) {
		
		final ProductSaleDAO sale = entityManager.find(ProductSaleDAO.class,id.getId());
		if(null == sale) { 
			throw new EntityNotFoundException(String.format("ProductSale %s not found",id));
			}
		sale.setEntityManager(entityManager);
		
		return sale;
	}
	
	public static ProductSaleDAO create(final EntityManager entityManager, final ProductSaleDTO sale) {
		
		final ProductSaleDAO saleDAO = new ProductSaleDAO(entityManager,sale);
		entityManager.persist(saleDAO);
		
		return saleDAO;
	}
	
	
	private ProductSaleDAO(final EntityManager entityManager, final ProductSaleDTO sale) {
		super(entityManager);
		setDTO(sale);
	}
	
	public ProductSaleDTO getDTO() {
		final ProductSaleDTO sale = new ProductSaleDTO(getId(),getProduct(),getCount(),getDiscount());
		return sale;
	}
	
	public void setDTO(final ProductSaleDTO sale) {
		setId(sale.getId());
		setProduct(sale.getProduct());
		setCount(sale.getCount());
		setDiscount(sale.getDiscount());
	}
	
	private ProductDTO getProduct() {
		return product.getDTO();
	}
	
	private void setProduct(final ProductDTO product) {
		this.product = ProductDAO.load(getEntityManager(),product.getId());
	}
	
	private Integer getCount() {
		return count;
	}
	
	private void setCount(final Integer count) {
		this.count = count;
	}
	

	private Double getDiscount() {
		return discount;
	}
	
	private void setDiscount(final Double discount) {
		this.discount = discount;
	}
	
			
}
