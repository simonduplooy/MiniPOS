package com.lunarsky.minipos.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.common.exception.NameInUseException;
import com.lunarsky.minipos.common.exception.PasswordInUseException;
import com.lunarsky.minipos.model.dto.PersistenceIdDTO;
import com.lunarsky.minipos.model.dto.UserDTO;

public class UserManager {
	private static final Logger log = LogManager.getLogger();
	
	//Prevent instantiation
	private UserManager() {
	}
	
	public static List<UserDTO> getUsers(final EntityManager entityManager) {
		log.debug("getUsers()");
		
		final Query query = entityManager.createQuery("from UserDAO");
		final List<UserDAO> resultList = query.getResultList();

		final List<UserDTO> users = new ArrayList<UserDTO>();
		for(UserDAO result: resultList) {
			users.add(result.getDTO());
		}
		
		return users;
	}
	
	public static UserDTO getUser(final EntityManager entityManager, final PersistenceIdDTO id) throws EntityNotFoundException {
		log.debug("getUser() {}",id);
		
		final UserDAO userDAO = UserDAO.load(entityManager,id);
		return userDAO.getDTO();
	}
	
	public static UserDTO getUserWithPassword(final EntityManager entityManager, final String password) throws EntityNotFoundException {
		log.debug("Getting User with Password {}",password);
		
		final Query query = entityManager.createQuery("from UserDAO where password = :password");
		query.setParameter("password",password);
		
		//There must be a unique constraint on the password column
		final UserDAO userDAO;
		try {
			userDAO = (UserDAO)query.getSingleResult();
		} catch (NoResultException noResultException) {
			throw new EntityNotFoundException(String.format("User with password %s not found",password),noResultException); 
		}
		
		return userDAO.getDTO();
	}
	
	public static UserDTO saveUser(final EntityManager entityManager, final UserDTO user) throws NameInUseException, PasswordInUseException, EntityNotFoundException {
		log.debug("saveUser() {}", user);
		
		UserDAO userDAO;
		if(user.hasId()) {
			log.debug("Updating User {}",user.getName());
			userDAO = UserDAO.load(entityManager,user.getId());
			userDAO.setDTO(user);
		} else {
			log.debug("Creating new User {}",user.getName());
			userDAO = UserDAO.create(entityManager, user);
		}
		
		final UserDTO updatedUser = userDAO.getDTO();
		return updatedUser;
	}

	public static void deleteUser(final EntityManager entityManager, final PersistenceIdDTO id) throws EntityNotFoundException {
		log.debug("deleteUser() {}",id);
		
		final UserDAO userDAO = entityManager.find(UserDAO.class,id.getId());
		if(null == userDAO) { 
			throw new EntityNotFoundException(String.format("User %s not found",id));
			}
		entityManager.remove(userDAO);
	}
	
}
