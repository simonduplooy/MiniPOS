package com.lunarsky.minipos.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaUpdate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.common.exception.NameInUseException;
import com.lunarsky.minipos.model.dto.AccountDTO;

public class AccountManager {
	private static final Logger log = LogManager.getLogger();
	
	//Prevent instantiation
	private AccountManager() {
	}

	public static List<AccountDTO> getAccounts(final EntityManager entityManager) {
		log.debug("getAccounts()");
		
		final Query query = entityManager.createQuery("from AccountDAO");
		final List<AccountDAO> resultList = query.getResultList();

		final List<AccountDTO> accounts = new ArrayList<AccountDTO>();
		for(AccountDAO result: resultList) {
			accounts.add(result.getAccount());
		}
		
		return accounts;
	}
	
	public static List<AccountDTO> getAccounts(final EntityManager entityManager, HibernatePersistenceId userId) {
		log.debug("getAccounts({})",userId);
		
		final UserDAO userDAO = UserDAO.load(entityManager,userId);
		final List<AccountDTO> accounts = userDAO.getAccounts();
		
		return accounts;
	}
	
	public static AccountDTO create(final EntityManager entityManager, final HibernatePersistenceId userId, final AccountDTO account) throws NameInUseException, EntityNotFoundException {

		log.debug("Creating new Account {}",account);
		final AccountDAO accountDAO = AccountDAO.create(entityManager, account);
		final UserDAO userDAO = UserDAO.load(entityManager,userId);
		accountDAO.addUser(userDAO);
		entityManager.persist(accountDAO);

		final AccountDTO updatedAccount = accountDAO.getAccount();
		return updatedAccount;
	}
	
	public static void update(final EntityManager entityManager, final AccountDTO account) throws NameInUseException, EntityNotFoundException {
		log.debug("Updating Account {}",account);
		
		final AccountDAO accountDAO = AccountDAO.load(entityManager,(HibernatePersistenceId)account.getId());
		accountDAO.setAccount(account);		
	}
	
	public static void delete(final EntityManager entityManager, final HibernatePersistenceId id) throws EntityNotFoundException {	
		log.debug("Deleting Account {}",id);
		
		final AccountDAO accountDAO = entityManager.find(AccountDAO.class,id.getId());
		if(null == accountDAO) {
			throw new EntityNotFoundException(String.format("Account %s not found",id));
		}
		entityManager.remove(accountDAO);
	}
}
