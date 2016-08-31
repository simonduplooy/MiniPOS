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
import com.lunarsky.minipos.model.dto.AccountDTO;
import com.lunarsky.minipos.model.dto.RoleDTO;
import com.lunarsky.minipos.model.dto.UserDTO;

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
	
	public static UserDAO create(final EntityManager entityManager, final UserDTO user) {
		assert(null != entityManager);
		assert(null != user);
		
		final UserDAO userDAO = new UserDAO(entityManager,user);
		entityManager.persist(userDAO);
		
		return userDAO;
	}

	public UserDTO getUser() {
		return new UserDTO(getId(),getName(),getPassword());
	}

	public void setUser(final UserDTO user) {
		assert(null != user);
		
		setId(user.getId());
		setName(user.getName());
		setPassword(user.getPassword());
	}
		
	private UserDAO(final EntityManager entityManager, final UserDTO user) {
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
	
	public List<AccountDTO> getAccounts() {
		final List<AccountDTO> accountList = new ArrayList<AccountDTO>();
		
		for(AccountDAO account: this.accounts) {
			accountList.add(account.getAccount());
		}
		
		return accountList;
	}
	
}
