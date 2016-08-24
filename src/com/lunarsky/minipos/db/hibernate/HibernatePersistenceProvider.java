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
import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.interfaces.PersistenceProvider;
import com.lunarsky.minipos.interfaces.Transaction;
import com.lunarsky.minipos.model.Account;
import com.lunarsky.minipos.model.PersistenceConfig;
import com.lunarsky.minipos.model.ProductButtonConfig;
import com.lunarsky.minipos.model.ProductButtonGroupConfig;
import com.lunarsky.minipos.model.Role;
import com.lunarsky.minipos.model.StockItem;
import com.lunarsky.minipos.model.User;
import com.lunarsky.minipos.model.dto.ProductDTO;
import com.lunarsky.minipos.model.dto.ProductGroupDTO;

public class HibernatePersistenceProvider implements PersistenceProvider {
	private static final Logger log = LogManager.getLogger();

	private static final String PERSISTANCE_UNIT_NAME = "MiniPOS";

	EntityManagerFactory entityManagerFactory;
		
	public HibernatePersistenceProvider() {
	}
	
	
	/******************************************
	 * Config
	 ******************************************/
	public synchronized PersistenceConfig getConfig() {
		return ConfigManager.getConfig();
	}
	
	public synchronized void setConfig(PersistenceConfig config) {
		ConfigManager.setConfig(config);
	}
	
	
	/******************************************
	 * Test Persistence Connection
	 ******************************************/
	public void createConnection() {
		assert(null == entityManagerFactory);
		
		//Create the EntityManagerFactory 
		PersistenceConfig config = ConfigManager.getConfig();
		Map<String,String> properties = getPersistenceProperties(config);
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTANCE_UNIT_NAME,properties);
	}
	
	public void testConnection(PersistenceConfig config) {

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
	public List<User> getUsers(final Transaction transaction) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final List<User> users = UserManager.getUsers(entityManager);
		return users;
	}

	public User getUser(final Transaction transaction, final PersistenceId id) throws EntityNotFoundException {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		User user = UserManager.getUser(entityManager,(HibernatePersistenceId)id);
		return user;
	}
	
	public User getUserWithPassword(final Transaction transaction, final String password)  throws EntityNotFoundException {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final User user = UserManager.getUserWithPassword(entityManager,password);
		return user;
	}
	
	public User saveUser(final Transaction transaction , final User user) throws NameInUseException, PasswordInUseException, EntityNotFoundException {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final User updatedUser = UserManager.saveUser(entityManager, user);
		return updatedUser;
	}
	
	public void deleteUser(final Transaction transaction, final PersistenceId id)  throws EntityNotFoundException {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		UserManager.deleteUser(entityManager,(HibernatePersistenceId)id);
	}

	/*****************************************************************************
	 * Roles
	 *****************************************************************************/
	public List<Role> getRoles(final Transaction transaction) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final List<Role>roles = RoleManager.getRoles(entityManager);
		return roles;
	}
	
	public Role saveRole(final Transaction transaction, final Role role)  throws EntityNotFoundException {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final Role updatedRole = RoleManager.saveRole(entityManager, role);
		return updatedRole;
	}
	
	public void deleteRole(final Transaction transaction, final PersistenceId id)  throws EntityNotFoundException {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		RoleManager.deleteRole(entityManager,(HibernatePersistenceId)id);
	}
	
	/*****************************************************************************
	 * Stock
	 *****************************************************************************/
	public List<StockItem> getStock(final Transaction transaction) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final List<StockItem> stockList = StockManager.getStock(entityManager);
		return stockList;
	}
	
	public StockItem saveStockItem(final Transaction transaction, final StockItem stockItem) throws EntityNotFoundException {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final StockItem updatedStockItem = StockManager.save(entityManager,stockItem);
		return updatedStockItem;
	}
	
	public void deleteStockItem(final Transaction transaction, final PersistenceId id) throws EntityNotFoundException {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		StockManager.delete(entityManager,(HibernatePersistenceId)id);
	}
	
	
	/*****************************************************************************
	 * Accounts
	 *****************************************************************************/
	public List<Account> getAccounts(final Transaction transaction) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final List<Account> accounts = AccountManager.getAccounts(entityManager);
		return accounts;		
	}
	
	public List<Account> getAccounts(final Transaction transaction, final PersistenceId userId) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final List<Account> accounts = AccountManager.getAccounts(entityManager,(HibernatePersistenceId)userId);
		return accounts;		
	}
	
	public Account createAccount(final Transaction transaction, final PersistenceId userId, final Account account) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final Account updatedAccount = AccountManager.create(entityManager,(HibernatePersistenceId)userId,account);
		return updatedAccount;
	}

	public void updateAccount(final Transaction transaction, final Account account) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		AccountManager.update(entityManager,account);
	}
	
	public void deleteAccount(final Transaction transaction, final PersistenceId id) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		AccountManager.delete(entityManager,(HibernatePersistenceId)id);		
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
	
	public void deleteProduct(final Transaction transaction, final PersistenceId id) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		ProductManager.delete(entityManager,(HibernatePersistenceId)id);
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
	public void deleteProductGroup(final Transaction transaction, final PersistenceId id) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		ProductGroupManager.delete(entityManager,(HibernatePersistenceId)id);
	}
	
	/*****************************************************************************
	 * Product Buttons
	 *****************************************************************************/
	public List<ProductButtonConfig> getProductButtons(final Transaction transaction) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final List<ProductButtonConfig> buttonConfigs = ProductButtonManager.getConfigs(entityManager);
		return buttonConfigs;	
	}
	
	public ProductButtonConfig saveProductButton(final Transaction transaction, final ProductButtonConfig config) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final ProductButtonConfig updatedConfig = ProductButtonManager.save(entityManager,config);
		return updatedConfig;
	}
	
	public void deleteProductButton(final Transaction transaction, final PersistenceId id) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		ProductButtonManager.delete(entityManager,(HibernatePersistenceId)id);
	}
	
	public List<ProductButtonGroupConfig> getProductButtonGroups(final Transaction transaction) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final List<ProductButtonGroupConfig> buttonConfigs = ProductButtonGroupManager.getConfigs(entityManager);
		return buttonConfigs;	
	}
	
	public ProductButtonGroupConfig saveProductButtonGroup(final Transaction transaction, final ProductButtonGroupConfig config) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		final ProductButtonGroupConfig updatedConfig = ProductButtonGroupManager.save(entityManager,config);
		return updatedConfig;
	}
	
	public void deleteProductButtonGroup(final Transaction transaction, final PersistenceId id) {
		final EntityManager entityManager = ((HibernateTransaction)transaction).getEntityManager();
		ProductButtonGroupManager.delete(entityManager,(HibernatePersistenceId)id);
	}
	
	/*****************************************************************************
	 * Utils
	 *****************************************************************************/
	public void close() {
		if(entityManagerFactory != null) {
			entityManagerFactory.close();
		}
	}
	
	private Map<String,String> getPersistenceProperties(PersistenceConfig config) {

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
