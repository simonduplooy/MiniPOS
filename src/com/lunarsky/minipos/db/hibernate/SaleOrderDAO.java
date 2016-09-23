package com.lunarsky.minipos.db.hibernate;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.lunarsky.minipos.model.dto.SaleOrderDTO;

@Entity
@Table(	name="saleorders" )
public class SaleOrderDAO extends HibernateDAO {

	@ManyToOne(optional = true)
	@JoinColumn(name = "accountId", foreignKey = @ForeignKey(name = "FK_SaleOrders_Accounts"))
	private AccountDAO account;
	
	@OneToMany(mappedBy="saleOrder")
	private Set<ProductSaleDAO> productSales;
	
	//used by hibernate
	public SaleOrderDAO() {}
	
}
