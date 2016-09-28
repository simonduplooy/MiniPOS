package com.lunarsky.minipos.db.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.dto.AccountDTO;
import com.lunarsky.minipos.model.dto.PersistenceIdDTO;
import com.lunarsky.minipos.model.dto.ProductSaleDTO;
import com.lunarsky.minipos.model.dto.SaleDTO;
import com.lunarsky.minipos.model.dto.SaleOrderDTO;

@Entity
@Table(	name="saleorders" )
public class SaleOrderDAO extends HibernateDAO {

	@ManyToOne(optional = false)
	@JoinColumn(name = "accountId", foreignKey = @ForeignKey(name = "FK_SaleOrders_Accounts"))
	private AccountDAO account;
	
	@OneToMany(mappedBy="saleOrder")
	private Set<ProductSaleDAO> productSales;
	
	//used by hibernate
	public SaleOrderDAO() {}
	
	public static SaleOrderDAO load(final EntityManager entityManager, final PersistenceIdDTO id) {

		final SaleOrderDAO orderDAO = entityManager.find(SaleOrderDAO.class,id.getId());
		if(null == orderDAO) { 
			throw new EntityNotFoundException(String.format("SaleOrder %s not found",id));
		}
		
		orderDAO.setEntityManager(entityManager);
		return orderDAO;
	}
	
	public static SaleOrderDAO create(final EntityManager entityManager, final PersistenceIdDTO accountId, final SaleOrderDTO order) {
		
		final SaleOrderDAO orderDAO = new SaleOrderDAO(entityManager,order);
		orderDAO.setAccount(accountId);
		entityManager.persist(orderDAO);
		
		return orderDAO;
	}
	
	private SaleOrderDAO(final EntityManager entityManager, final SaleOrderDTO order) {
		super(entityManager);
		setDTO(order);
	}
	
	public SaleOrderDTO getDTO() {
		final SaleOrderDTO order = new SaleOrderDTO(getId(),getCreated(),getSales());
		return order;
	}

	public void setDTO(final SaleOrderDTO order) {
	}
	
	public List<SaleDTO> getSales() {
		final List<SaleDTO> saleList = new ArrayList<SaleDTO>();
		for(ProductSaleDAO saleDAO: productSales) {
			final ProductSaleDTO saleDTO = saleDAO.getDTO();
			saleList.add(saleDTO);
		}
		return saleList;
	}
	
	private void setAccount(final PersistenceIdDTO accountId) {
		this.account = AccountDAO.load(getEntityManager(),accountId);
	}
	
}