package com.lunarsky.minipos.db.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.lunarsky.minipos.common.Const;
import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.Account;
import com.lunarsky.minipos.model.Role;
import com.lunarsky.minipos.model.User;

@Entity
@Table(
	name="users", 
	uniqueConstraints = { 
		@UniqueConstraint(columnNames = "name", name = "NAME_CONSTRAINT") ,
		@UniqueConstraint(columnNames = "password", name = "PASSWORD_CONSTRAINT") 
		}
)
public class UserDAO extends HibernateDAO {

	@Column(nullable = false, length = Const.MAX_TEXTFIELD_LENGTH)
	private String name;

	@Column(nullable = false, length = Const.PASSWORD_LENGTH)
	private String password;

	@OneToOne
	private RoleDAO role;
	
	@ManyToMany(mappedBy="users")
	private Set<AccountDAO> accounts; 

	//used by hibernate
	public UserDAO() {}

	public static UserDAO load(final EntityManager entityManager, final HibernatePersistenceId id) {
		assert(null != entityManager);
		assert(null != id);
		
		final UserDAO userDAO = entityManager.find(UserDAO.class,id.getId());
		if(null == userDAO) { 
			throw new EntityNotFoundException(String.format("User %s not found",id));
		}
		
		userDAO.setEntityManager(entityManager);
		return userDAO;
	}
	
	public static UserDAO create(final EntityManager entityManager, final User user) {
		assert(null != entityManager);
		assert(null != user);
		
		final UserDAO userDAO = new UserDAO(entityManager,user);
		entityManager.persist(userDAO);
		
		return userDAO;
	}

	public User getUser() {
		return new User(getId(),getName(),getPassword(),getRole());
	}

	public void setUser(final User user) {
		assert(null != user);
		
		setId(user.getId());
		setName(user.getName());
		setPassword(user.getPassword());
		setRole(user.getRole());
	}
		
	private UserDAO(final EntityManager entityManager, final User user) {
		super(entityManager);
		setUser(user);
	}

	private String getName() {
		assert(null != name);
		return name; 
		}
	
	private void setName(String name) { 
		assert(null != name);
		this.name = name; 
		}

	private String getPassword() {
		assert(null != password);
		return password; 
		}
	
	private void setPassword(String password) { 
		assert(null != password);
		this.password = password; 
		}
	
	private Role getRole() {
		assert(null != role);
		return role.getRole(); 
		}
	
	public void setRole(Role role) { 
		assert(null != role);
		this.role = RoleDAO.load(getEntityManager(),(HibernatePersistenceId)role.getId()); 
	}

	public List<Account> getAccounts() {
		final List<Account> accounts = new ArrayList<Account>();
		
		if(null != accounts) {
			for(AccountDAO account: this.accounts){
				accounts.add(account.getAccount());
			}
		}
		
		return accounts;
	}
	
}
