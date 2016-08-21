package com.lunarsky.minipos.interfaces;

import java.util.List;

import com.lunarsky.minipos.model.Account;
import com.lunarsky.minipos.model.PersistenceConfig;
import com.lunarsky.minipos.model.Product;
import com.lunarsky.minipos.model.ProductButtonConfig;
import com.lunarsky.minipos.model.ProductButtonGroupConfig;
import com.lunarsky.minipos.model.Role;
import com.lunarsky.minipos.model.StockItem;
import com.lunarsky.minipos.model.User;
import com.lunarsky.minipos.ui.ProductButtonGroup;

public interface PersistenceProvider {
	
	public PersistenceConfig getConfig();
	public void setConfig(PersistenceConfig config);
	public void createConnection();
	public void testConnection(PersistenceConfig config);
	
	public Transaction startTransaction();
	
	/*****************************************************************************
	 * Users
	 *****************************************************************************/
	public List<User> getUsers(final Transaction transaction);
	public User getUser(final Transaction transaction, final PersistenceId id);
	public User getUserWithPassword(final Transaction transaction, final String password);	
	public User saveUser(final Transaction transaction, final User user);
	public void deleteUser(final Transaction transaction, final PersistenceId id);
	
	/*****************************************************************************
	 * Roles
	 *****************************************************************************/
	public List<Role> getRoles(final Transaction transaction);
	public Role saveRole(final Transaction transaction, final Role role);
	public void deleteRole(final Transaction transaction, final PersistenceId id);
	
	/*****************************************************************************
	 * Stock
	 *****************************************************************************/
	public List<StockItem> getStock(final Transaction transaction);
	public StockItem saveStockItem(final Transaction transaction, final StockItem stockItem);
	public void deleteStockItem(final Transaction transaction, final PersistenceId id);
	
	/*****************************************************************************
	 * Accounts
	 *****************************************************************************/
	public List<Account> getAccounts(final Transaction transaction);
	public List<Account> getAccounts(final Transaction transaction, final PersistenceId userId);
	public Account createAccount(final Transaction transaction, final PersistenceId userId, final Account account);
	public void updateAccount(final Transaction transaction, final Account account);
	public void deleteAccount(final Transaction transaction, final PersistenceId id);
	
	/*****************************************************************************
	 * Products
	 *****************************************************************************/
	public List<Product> getProducts(final Transaction transaction);
	public Product saveProduct(final Transaction transaction, final Product product);
	public void deleteProduct(final Transaction transaction, final PersistenceId id);
	
	/*****************************************************************************
	 * Product Buttons
	 *****************************************************************************/
	public List<ProductButtonConfig> getProductButtons(final Transaction transaction);
	public ProductButtonConfig saveProductButton(final Transaction transaction, final ProductButtonConfig config);
	
	public List<ProductButtonGroup> getProductButtonGroups(final Transaction transaction);
	public ProductButtonGroupConfig saveProductButtonGroup(final Transaction transaction, final ProductButtonGroupConfig config);
	
	public void close();
	
}
