package com.lunarsky.minipos.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.Role;

public class RoleManager {
	private static final Logger log = LogManager.getLogger();
	
	public static List<Role> getRoles(final EntityManager entityManager) {

		log.debug("Getting Roles");
		final Query query = entityManager.createQuery("from RoleDAO");
		final List<RoleDAO> resultList = query.getResultList();

		final List<Role> roles = new ArrayList<Role>();
		for(RoleDAO result: resultList) {
			roles.add(result.getRole());
		}
		
		return roles;
	}
	
	public static Role saveRole(final EntityManager entityManager, final Role role) {

		RoleDAO roleDAO;
		if(role.hasId()) {
			log.debug("Updating Role {}",role.getName());
			roleDAO = RoleDAO.load(entityManager,(HibernatePersistenceId)role.getId());
			if(null == roleDAO) {throw new EntityNotFoundException(String.format("Role %s not found",role.getName()));} 
			roleDAO.setRole(role);
		} else {
			log.debug("Creating new Role {}",role.getName());
			roleDAO = RoleDAO.create(entityManager,role);
		}
		
		final Role updatedRole = roleDAO.getRole();
		return updatedRole;
	}

	public static void deleteRole(final EntityManager entityManager, final HibernatePersistenceId id) {
		log.debug("Deleting Role {}",id);
		final RoleDAO roleDAO = entityManager.find(RoleDAO.class,id.getId());
		if(null == roleDAO) {
			throw new EntityNotFoundException(String.format("Role %s not found",id));
		}
		entityManager.remove(roleDAO);
	}	
	
}
