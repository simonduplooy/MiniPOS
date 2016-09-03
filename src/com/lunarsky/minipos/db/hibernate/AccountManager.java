package com.lunarsky.minipos.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.common.exception.NameInUseException;
import com.lunarsky.minipos.model.dto.AccountDTO;
import com.lunarsky.minipos.model.dto.PersistenceIdDTO;

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
			accounts.add(result.getDTO());
		}
		
		return accounts;
	}
	
	public static List<AccountDTO> getAccounts(final EntityManager entityManager, PersistenceIdDTO userId) {
		log.debug("getAccounts() {}",userId);
		
		final UserDAO userDAO = UserDAO.load(entityManager,userId);
		final List<AccountDTO> accounts = userDAO.getAccounts();
		
		return accounts;
	}
	
	public static AccountDTO create(final EntityManager entityManager, final PersistenceIdDTO userId, final AccountDTO account) throws NameInUseException, EntityNotFoundException {

		log.debug("create() {}",account);
		final AccountDAO accountDAO = AccountDAO.create(entityManager, account);
		final UserDAO userDAO = UserDAO.load(entityManager,userId);
		accountDAO.addUser(userDAO);
		entityManager.persist(accountDAO);

		final AccountDTO updatedAccount = accountDAO.getDTO();
		return updatedAccount;
	}
	
	public static void update(final EntityManager entityManager, final AccountDTO account) throws NameInUseException, EntityNotFoundException {
		log.debug("update() {}",account);
		
		final AccountDAO accountDAO = AccountDAO.load(entityManager,account.getId());
		accountDAO.setDTO(account);		
	}
	
	public static void delete(final EntityManager entityManager, final PersistenceIdDTO id) throws EntityNotFoundException {	
		log.debug("delete() {}",id);
		
		final AccountDAO accountDAO = entityManager.find(AccountDAO.class,id.getId());
		if(null == accountDAO) {
			throw new EntityNotFoundException(String.format("Account %s not found",id));
		}
		entityManager.remove(accountDAO);
	}
}
