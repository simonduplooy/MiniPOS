package com.lunarsky.minipos.db.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.common.exception.NameInUseException;
import com.lunarsky.minipos.common.exception.PasswordInUseException;
import com.lunarsky.minipos.interfaces.PersistenceProvider;
import com.lunarsky.minipos.interfaces.Transaction;
import com.lunarsky.minipos.model.dto.AccountDTO;
import com.lunarsky.minipos.model.dto.PersistenceConfigDTO;
import com.lunarsky.minipos.model.dto.PersistenceIdDTO;
import com.lunarsky.minipos.model.dto.ProductButtonConfigDTO;
import com.lunarsky.minipos.model.dto.ProductDTO;
import com.lunarsky.minipos.model.dto.ProductGroupButtonConfigDTO;
import com.lunarsky.minipos.model.dto.ProductGroupDTO;
import com.lunarsky.minipos.model.dto.UserDTO;

public class HibernatePersistenceProvider implements PersistenceProvider {
	private static final Logger log = LogManager.getLogger();

	private static final String PERSISTANCE_UNIT_NAME = "MiniPOS";

	EntityManagerFactory entityManagerFactory;
		
	public HibernatePersistenceProvider() {
	}
	
	
	/******************************************
	 * Config
	 ******************************************/
	public synchronized PersistenceConfigDTO getConfig() {
		return ConfigManager.getConfig();
	}
	
	public synchronized void setConfig(PersistenceConfigDTO config) {
		ConfigManager.setConfig(config);
	}
	
	
	/******************************************
	 * Test Persistence Connection
	 ******************************************/
	public void createConnection() {
		assert(null == entityManagerFactory);
		
		//Create the EntityManagerFactory 
		PersistenceConfigDTO config = ConfigManager.getConfig();
		Map<String,String> properties = getPersistenceProperties(config);
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTANCE_UNIT_NAME,properties);
	}
	
	public void testConnection(PersistenceConfigDTO config) {

		Map<String,String> properties = getPersistenceProperties(config);
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTANCE_UNIT_NAME,properties);
		entityManagerFactory.close();
		
	}

	public Transaction startTransaction() {
		return new HibernateTransaction(getEntityManagerFactory());
	}
	
	/*****************************************************************************
	 * Users
	 *****************************************************************************/
	public List<UserDTO> getUsers(final Transaction transaction) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final List<UserDTO> users = UserManager.getUsers(entityManager);
		return users;
	}

	public UserDTO getUser(final Transaction transaction, final PersistenceIdDTO id) throws EntityNotFoundException {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		UserDTO user = UserManager.getUser(entityManager,id);
		return user;
	}
	
	public UserDTO getUserWithPassword(final Transaction transaction, final String password)  throws EntityNotFoundException {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final UserDTO user = UserManager.getUserWithPassword(entityManager,password);
		return user;
	}
	
	public UserDTO saveUser(final Transaction transaction , final UserDTO user) throws NameInUseException, PasswordInUseException, EntityNotFoundException {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final UserDTO updatedUser = UserManager.saveUser(entityManager, user);
		return updatedUser;
	}
	
	public void deleteUser(final Transaction transaction, final PersistenceIdDTO id)  throws EntityNotFoundException {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		UserManager.deleteUser(entityManager,id);
	}

	/*****************************************************************************
	 * Accounts
	 *****************************************************************************/
	public List<AccountDTO> getAccounts(final Transaction transaction) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final List<AccountDTO> accounts = AccountManager.getAccounts(entityManager);
		return accounts;		
	}
	
	public List<AccountDTO> getAccounts(final Transaction transaction, final PersistenceIdDTO userId) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final List<AccountDTO> accounts = AccountManager.getAccounts(entityManager,userId);
		return accounts;		
	}

	public AccountDTO createAccount(final Transaction transaction, final PersistenceIdDTO userId, final AccountDTO account) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final AccountDTO updatedAccount = AccountManager.create(entityManager,userId,account);
		return updatedAccount;
	}

	public void updateAccount(final Transaction transaction, final AccountDTO account) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		AccountManager.update(entityManager,account);
	}
	
	public void deleteAccount(final Transaction transaction, final PersistenceIdDTO id) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		AccountManager.delete(entityManager,id);		
	}

	/*****************************************************************************
	 * Products
	 *****************************************************************************/
	public List<ProductDTO> getProducts(final Transaction transaction) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final List<ProductDTO> products = ProductManager.getProducts(entityManager);
		return products;	
	}
	
	public ProductDTO saveProduct(final Transaction transaction, final ProductDTO product) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final ProductDTO updatedProduct = ProductManager.save(entityManager,product);
		return updatedProduct;
	}
	
	public void deleteProduct(final Transaction transaction, final PersistenceIdDTO id) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		ProductManager.delete(entityManager,id);
	}
	
	public List<ProductGroupDTO> getProductGroups(final Transaction transaction) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final List<ProductGroupDTO> groups = ProductGroupManager.getGroups(entityManager);
		return groups;	
	}
	public ProductGroupDTO saveProductGroup(final Transaction transaction, final ProductGroupDTO group) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final ProductGroupDTO updatedGroup = ProductGroupManager.save(entityManager,group);
		return updatedGroup;
	}
	public void deleteProductGroup(final Transaction transaction, final PersistenceIdDTO id) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		ProductGroupManager.delete(entityManager,id);
	}
	
	/*****************************************************************************
	 * Product Buttons
	 *****************************************************************************/
	public List<ProductButtonConfigDTO> getProductButtons(final Transaction transaction) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final List<ProductButtonConfigDTO> buttonConfigs = ProductButtonManager.getConfigs(entityManager);
		return buttonConfigs;	
	}
	
	public ProductButtonConfigDTO saveProductButton(final Transaction transaction, final ProductButtonConfigDTO config) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final ProductButtonConfigDTO updatedConfig = ProductButtonManager.save(entityManager,config);
		return updatedConfig;
	}
	
	public void deleteProductButton(final Transaction transaction, final PersistenceIdDTO id) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		ProductButtonManager.delete(entityManager,id);
	}
	
	public List<ProductGroupButtonConfigDTO> getProductGroupButtons(final Transaction transaction) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final List<ProductGroupButtonConfigDTO> buttonConfigs = ProductGroupButtonManager.getConfigs(entityManager);
		return buttonConfigs;	
	}
	
	public ProductGroupButtonConfigDTO saveProductGroupButton(final Transaction transaction, final ProductGroupButtonConfigDTO config) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final ProductGroupButtonConfigDTO updatedConfig = ProductGroupButtonManager.save(entityManager,config);
		return updatedConfig;
	}
	
	public void deleteProductGroupButton(final Transaction transaction, final PersistenceIdDTO id) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		ProductGroupButtonManager.delete(entityManager,id);
	}
	
	/*****************************************************************************
	 * Utils
	 *****************************************************************************/
	public void close() {
		if(entityManagerFactory != null) {
			entityManagerFactory.close();
		}
	}
	
	private Map<String,String> getPersistenceProperties(PersistenceConfigDTO config) {

		//TODO MAGIC STRINGS
		Map<String,String> properties = new HashMap<String,String>();
		properties.put("javax.persistence.jdbc.url","jdbc:mysql://"+config.getServer()+":"+config.getPort()+"/"+config.getDatabase());
		properties.put("javax.persistence.jdbc.user",config.getUsername());
		properties.put("javax.persistence.jdbc.password",config.getPassword());
		
		return properties;
	}
	
	
	private EntityManagerFactory getEntityManagerFactory() {
		assert(entityManagerFactory != null);
		return entityManagerFactory;
	}
	
}
