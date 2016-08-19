package com.lunarsky.minipos.db.hibernate;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.Account;

@Entity
@Table(
	name="accounts", 
	uniqueConstraints = { 
		@UniqueConstraint(columnNames = "name", name = "NAME_CONSTRAINT") ,
		}
)
public class AccountDAO extends HibernateDAO {
	@Column(length = Const.MAX_TEXTFIELD_LENGTH)
	private String name;
	
	@ManyToMany  
	@JoinTable(joinColumns=@JoinColumn(name="account_id"), inverseJoinColumns=@JoinColumn(name="user_id"))  
	private Set<UserDAO> users;
	
	//used by Hibernate
	public AccountDAO() {}
	
	public static AccountDAO load(final EntityManager entityManager, final HibernatePersistenceId id) {
		assert(null != entityManager);
		assert(null != id);
		
		final AccountDAO accountDAO = entityManager.find(AccountDAO.class,id.getId());
		if(null == accountDAO) { 
			throw new EntityNotFoundException(String.format("Account %s not found",id));
		}
		
		accountDAO.setEntityManager(entityManager);
		return accountDAO;
	}
	
	public static AccountDAO create(final EntityManager entityManager, final Account account) {
		assert(null != entityManager);
		assert(null != account);
		
		final AccountDAO accountDAO = new AccountDAO(entityManager,account);
		entityManager.persist(accountDAO);
		
		return accountDAO;
	}
	
	private AccountDAO(final EntityManager entityManager, final Account account) {
		super(entityManager);
		setAccount(account);
	}
	
	public Account getAccount() {
		final Account account = new Account(getId());
		account.setName(getName());
		return account;
	}

	public void setAccount(final Account account) {
		assert(null != account);
		
		setId(account.getId());
		setName(account.getName());
	}
	
	private String getName() {
		assert(null != name);
		return name;
	}
	
	private void setName(final String name) {
		assert(null != name);
		this.name = name;
	}
	
	public void addUser(final UserDAO userDAO) {
		assert(null != userDAO);
		if(null == users) {
			users = new HashSet<UserDAO>();
		}
		this.users.add(userDAO);
	}
}
