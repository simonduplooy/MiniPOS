package com.lunarsky.minipos.db.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Table;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.Role;

@Entity
@Table(name="roles")
public class RoleDAO extends HibernateDAO {

	@Column(unique = true)
	private String name;
	private Boolean canVoid;
	private Boolean canManageUsers;

	//used by hibernate
	public RoleDAO() {}
	
	public static RoleDAO load(final EntityManager entityManager, final HibernatePersistenceId id) {
		assert(null != entityManager);
		assert(null != id);
		
		final RoleDAO roleDAO = entityManager.find(RoleDAO.class,id.getId());
		if(null == roleDAO) { throw new EntityNotFoundException(String.format("Role %s not found",id)); }
		roleDAO.setEntityManager(entityManager);
		
		return roleDAO;
	}
	
	public static RoleDAO create(final EntityManager entityManager, final Role role) {
		assert(null != entityManager);
		assert(null != role);
		
		final RoleDAO roleDAO = new RoleDAO(entityManager,role);
		entityManager.persist(roleDAO);
		
		return roleDAO;
	}
	
	public Role getRole() {
		return new Role(getId(),getName(),getCanVoid(),getCanManageUsers());
	}

	public void setRole(final Role role) {
		assert(null != role);
		
		setId(role.getId());
		setName(role.getName());
		setCanVoid(role.getCanVoid());
		setCanManageUsers(role.getCanManageUsers());
	}
	
	private RoleDAO(final EntityManager entityManager, final Role role) {
		super(entityManager);
		setRole(role);
	}

	private String getName() {
		assert(null != name);
		return name; 
		}
	
	private void setName(final String name) {
		assert(null != name);
		this.name = name; 
		}
	
	private Boolean getCanVoid() {
		assert(null != canVoid);
		return canVoid; 
		}
	
	private void setCanVoid(final Boolean canVoid) { 
		assert(null != canVoid);
		this.canVoid = canVoid; 
		}
	
	private Boolean getCanManageUsers() { 
		assert(null != canManageUsers);
		return canManageUsers; 
		}
	
	private void setCanManageUsers(final Boolean canManageUsers) {
		assert(null != canManageUsers);
		this.canManageUsers = canManageUsers; 
		}
	
}
