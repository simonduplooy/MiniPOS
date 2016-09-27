package com.lunarsky.minipos.db.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.dto.AccountDTO;
import com.lunarsky.minipos.model.dto.PersistenceIdDTO;
import com.lunarsky.minipos.model.dto.SaleOrderDTO;

@Entity
@Table(
	name="accounts", 
	uniqueConstraints = { 
		@UniqueConstraint(columnNames = "name", name = "NAME_CONSTRAINT") ,
		}
)
public class AccountDAO extends HibernateDAO {
	
	@Column(nullable = false, length = Const.MAX_TEXTFIELD_LENGTH)
	private String name;
	
	@ManyToMany  
	@JoinTable(joinColumns=@JoinColumn(name="account_id")
		, inverseForeignKey = @ForeignKey(name="FK_Users_Accounts")
		, inverseJoinColumns=@JoinColumn(name="user_id")
		, foreignKey = @ForeignKey(name = "FK_Accounts_Users"))  
	private Set<UserDAO> users;
	
	
	@OneToMany(mappedBy="account")
	private Set<SaleOrderDAO> orders;
	
	//used by Hibernate
	public AccountDAO() {}
	
	public static AccountDAO load(final EntityManager entityManager, final PersistenceIdDTO id) {

		final AccountDAO accountDAO = entityManager.find(AccountDAO.class,id.getId());
		if(null == accountDAO) { 
			throw new EntityNotFoundException(String.format("Account %s not found",id));
		}
		
		accountDAO.setEntityManager(entityManager);
		return accountDAO;
	}
	
	public static AccountDAO create(final EntityManager entityManager, final AccountDTO account) {
		
		final AccountDAO accountDAO = new AccountDAO(entityManager,account);
		entityManager.persist(accountDAO);
		
		return accountDAO;
	}
	
	private AccountDAO(final EntityManager entityManager, final AccountDTO account) {
		super(entityManager);
		setDTO(account);
	}
	
	public AccountDTO getDTO() {
		final AccountDTO account = new AccountDTO(getId(),getName());
		return account;
	}

	public void setDTO(final AccountDTO account) {
		setId(account.getId());
		setName(account.getName());
	}
	
	private String getName() {
		return name;
	}
	
	private void setName(final String name) {
		this.name = name;
	}
	
	public void addUser(final UserDAO userDAO) {
		if(null == users) {
			users = new HashSet<UserDAO>();
		}
		users.add(userDAO);
	}
	
	public List<SaleOrderDTO> getOrders() {
		
		final List<SaleOrderDTO> orderList = new ArrayList<SaleOrderDTO>();
		for(SaleOrderDAO order: orders) {
			final SaleOrderDTO orderDTO = order.getDTO();
			orderList.add(orderDTO);
		}
		return orderList;
	}
	
	public void addOrder(final PersistenceIdDTO accountId, final SaleOrderDTO order) {
		
		final SaleOrderDAO orderDAO = SaleOrderDAO.create(getEntityManager(),accountId,order);
		orders.add(orderDAO);
		
	}
}
