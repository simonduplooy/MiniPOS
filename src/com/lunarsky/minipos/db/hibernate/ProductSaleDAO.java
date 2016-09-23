package com.lunarsky.minipos.db.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
			
}
