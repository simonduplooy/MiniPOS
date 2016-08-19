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
import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.User;

public class UserManager {
	private static final Logger log = LogManager.getLogger();
	
	//Prevent instantiation
	private UserManager() {
	}
	
	public static List<User> getUsers(final EntityManager entityManager) {

		log.debug("Getting Users");
		final Query query = entityManager.createQuery("from UserDAO");
		final List<UserDAO> resultList = query.getResultList();

		final List<User> users = new ArrayList<User>();
		for(UserDAO result: resultList) {
			users.add(result.getUser());
		}
		
		return users;
	}
	
	public static User getUser(final EntityManager entityManager, final HibernatePersistenceId id) throws EntityNotFoundException {
		log.debug("Getting User {}",id);
		final UserDAO userDAO = UserDAO.load(entityManager,id);
		return userDAO.getUser();
	}
	
	public static User getUserWithPassword(final EntityManager entityManager, final String password) throws EntityNotFoundException {
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
		
		return userDAO.getUser();
	}
	
	public static User saveUser(final EntityManager entityManager, final User user) throws NameInUseException, PasswordInUseException, EntityNotFoundException {

		//checkConstraints(entityManager,user);
		
		UserDAO userDAO;
		if(user.hasId()) {
			log.debug("Updating User {}",user.getName());
			userDAO = UserDAO.load(entityManager,(HibernatePersistenceId)user.getId());
			userDAO.setUser(user);
		} else {
			log.debug("Creating new User {}",user.getName());
			userDAO = UserDAO.create(entityManager, user);
		}
		
		final User updatedUser = userDAO.getUser();
		return updatedUser;
	}

	public static void deleteUser(final EntityManager entityManager, final HibernatePersistenceId id) throws EntityNotFoundException {
		log.debug("Deleting User {}",id);
		final UserDAO userDAO = entityManager.find(UserDAO.class,id.getId());
		if(null == userDAO) { throw new EntityNotFoundException(String.format("User %s not found",id));}
		entityManager.remove(userDAO);
	}
	
}
