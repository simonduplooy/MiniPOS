package com.lunarsky.minipos.server;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.db.PersistenceProviderFactory;
import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.interfaces.PersistenceProvider;
import com.lunarsky.minipos.interfaces.ServerConnector;
import com.lunarsky.minipos.interfaces.Transaction;
import com.lunarsky.minipos.model.Account;
import com.lunarsky.minipos.model.PersistenceConfig;
import com.lunarsky.minipos.model.Product;
import com.lunarsky.minipos.model.ProductButtonConfig;
import com.lunarsky.minipos.model.ProductButtonGroupConfig;
import com.lunarsky.minipos.model.Role;
import com.lunarsky.minipos.model.StockItem;
import com.lunarsky.minipos.model.User;

public class LocalServerConnector implements ServerConnector {
	
	//Wrapper for Simple Database Calls
	private interface PersistenceCall<R> {
		public R call();
	}
	
	private static final Logger log = LogManager.getLogger();
	private final PersistenceProvider persistenceProvider;
	
	public LocalServerConnector() {
		persistenceProvider = PersistenceProviderFactory.getPersistenceProvider();
	}
	
	public PersistenceConfig getPersistenceConfig() {
		PersistenceConfig config = persistenceProvider.getConfig(); 
		return config;
	}
	
	public void setPersistenceConfig(PersistenceConfig config) {
		persistenceProvider.setConfig(config);
	}

	public void createPersistenceConnection() {
		persistenceProvider.createConnection();
	}
	
	public void testPersistenceConnection(PersistenceConfig config) {
		persistenceProvider.testConnection(config);
	}
	
	/*****************************************************************************
	 * Users
	 *****************************************************************************/	
	public List<User> getUsers() {
		final Transaction transaction = persistenceProvider.startTransaction();
		final List<User> users = (List<User>)persistenceCall(transaction,()->(persistenceProvider.getUsers(transaction)));
		return users;
	}
	
	public User getUser(final PersistenceId id){
		final Transaction transaction = persistenceProvider.startTransaction();
		final User user = (User)persistenceCall(transaction,()->(persistenceProvider.getUser(transaction, id)));
		return user;
	}
	
	public User getUserWithPassword(final String password) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final User user = (User)persistenceCall(transaction,()->(persistenceProvider.getUserWithPassword(transaction,password)));
		return user;
	}
	
	public User saveUser(final User user) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final User updatedUser = (User)persistenceCall(transaction,()->(persistenceProvider.saveUser(transaction, user)));
		return updatedUser;
	}

	public void deleteUser(final PersistenceId id) {
		final Transaction transaction = persistenceProvider.startTransaction();
		persistenceCall(transaction,()->{persistenceProvider.deleteUser(transaction, id);return null;});
	}
	
	/*****************************************************************************
	 * Roles
	 *****************************************************************************/
	public List<Role> getRoles() {
		final Transaction transaction = persistenceProvider.startTransaction();
		final List<Role> roles= (List<Role>)persistenceCall(transaction,()->(persistenceProvider.getRoles(transaction)));
		return roles;
	}
	
	/*****************************************************************************
	 * Roles
	 *****************************************************************************/
	public Role saveRole(final Role role) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final Role updatedRole= (Role)persistenceCall(transaction,()->(persistenceProvider.saveRole(transaction, role)));
		return updatedRole;
	}

	public void deleteRole(final PersistenceId id) {
		final Transaction transaction = persistenceProvider.startTransaction();
		persistenceCall(transaction,()->{persistenceProvider.deleteRole(transaction, id);return null;});
	}

	/*****************************************************************************
	 * Stock
	 *****************************************************************************/
	public List<StockItem> getStock() {
		final Transaction transaction = persistenceProvider.startTransaction();
		final List<StockItem> stockItems = (List<StockItem>)persistenceCall(transaction,()->(persistenceProvider.getStock(transaction)));
		return stockItems;
	}
	
	public StockItem saveStockItem(final StockItem stockItem) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final StockItem updatedStockItem = (StockItem)persistenceCall(transaction,()->(persistenceProvider.saveStockItem(transaction, stockItem)));
		return updatedStockItem;
	}
	
	public void deleteStockItem(final PersistenceId id) {
		final Transaction transaction = persistenceProvider.startTransaction();
		persistenceCall(transaction,()->{persistenceProvider.deleteStockItem(transaction, id);return null;});
	}

	/*****************************************************************************
	 * Accounts
	 *****************************************************************************/
	public List<Account> getAccounts() {
		final Transaction transaction = persistenceProvider.startTransaction();
		final List<Account> accounts = (List<Account>)persistenceCall(transaction,()->(persistenceProvider.getAccounts(transaction)));
		return accounts;		
	}

	public List<Account> getAccounts(final PersistenceId userId) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final List<Account> accounts = (List<Account>)persistenceCall(transaction,()->(persistenceProvider.getAccounts(transaction,userId)));
		return accounts;		
	}
	public Account createAccount(final PersistenceId userId, final Account account) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final Account updatedAccount = (Account)persistenceCall(transaction,()->(persistenceProvider.createAccount(transaction, userId, account)));
		return updatedAccount;
	}

	public void updateAccount(final Account account) {
		final Transaction transaction = persistenceProvider.startTransaction();
		persistenceCall(transaction,()->{persistenceProvider.updateAccount(transaction, account);return null;});
	}

	public void deleteAccount(final PersistenceId id) {
		final Transaction transaction = persistenceProvider.startTransaction();
		persistenceCall(transaction,()->{persistenceProvider.deleteAccount(transaction, id);return null;});		
	}
	
	/*****************************************************************************
	 * Products
	 *****************************************************************************/
	public List<Product> getProducts() {
		final Transaction transaction = persistenceProvider.startTransaction();
		final List<Product> products = (List<Product>)persistenceCall(transaction,()->(persistenceProvider.getProducts(transaction)));
		return products;
	}
	
	public Product saveProduct(final Product product) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final Product updatedProduct = (Product)persistenceCall(transaction,()->(persistenceProvider.saveProduct(transaction, product)));
		return updatedProduct;
	}


	public void deleteProduct(final PersistenceId id) {
		final Transaction transaction = persistenceProvider.startTransaction();
		persistenceCall(transaction,()->{persistenceProvider.deleteProduct(transaction, id);return null;});	
	}
	
	/*****************************************************************************
	 * Product Buttons
	 *****************************************************************************/
	public List<ProductButtonConfig> getProductButtons() {
		final Transaction transaction = persistenceProvider.startTransaction();
		final List<ProductButtonConfig> buttons = (List<ProductButtonConfig>)persistenceCall(transaction,()->(persistenceProvider.getProductButtons(transaction)));
		return buttons;
	}
	
	public ProductButtonConfig saveProductButton(final ProductButtonConfig config) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final ProductButtonConfig updatedConfig = (ProductButtonConfig)persistenceCall(transaction,()->(persistenceProvider.saveProductButton(transaction, config)));
		return updatedConfig;
	}
	
	public void deleteProductButton(final PersistenceId id) {
		final Transaction transaction = persistenceProvider.startTransaction();
		persistenceCall(transaction,()->{persistenceProvider.deleteProductButton(transaction,id);return null;});
	}
	
	public List<ProductButtonGroupConfig> getProductButtonGroups() {
		final Transaction transaction = persistenceProvider.startTransaction();
		final List<ProductButtonGroupConfig> buttons = (List<ProductButtonGroupConfig>)persistenceCall(transaction,()->(persistenceProvider.getProductButtonGroups(transaction)));
		return buttons;
	}
	
	public ProductButtonGroupConfig saveProductButtonGroup(final ProductButtonGroupConfig config) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final ProductButtonGroupConfig updatedConfig = (ProductButtonGroupConfig)persistenceCall(transaction,()->(persistenceProvider.saveProductButtonGroup(transaction, config)));
		return updatedConfig;
	}
	
	public void deleteProductButtonGroup(final PersistenceId id) {
		final Transaction transaction = persistenceProvider.startTransaction();
		persistenceCall(transaction,()->{persistenceProvider.deleteProductButtonGroup(transaction,id);return null;});
	}
	
	/*****************************************************************************
	 * Utils
	 *****************************************************************************/
	public void close() {
		persistenceProvider.close();
	}
	
	private static Object persistenceCall(final Transaction transaction, final PersistenceCall<Object> persistenceCall) {
		final Object result;
		try {
			result = persistenceCall.call();
		} catch (Exception e) {
			//log.catching(e);
			transaction.rollback();
			throw(e);
		}
		transaction.commit();
		
		return result;
	}
}
