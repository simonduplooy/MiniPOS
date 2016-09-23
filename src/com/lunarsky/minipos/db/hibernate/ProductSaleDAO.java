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
@Table(	name="productsales" )
public class ProductSaleDAO extends HibernateDAO {

	@ManyToOne(optional = true)
	@JoinColumn(name = "orderId", foreignKey = @ForeignKey(name = "FK_ProductSales_Orders"))
	private SaleOrderDAO saleOrder;
	
	//used by hibernate
	public ProductSaleDAO() {}
			
}
