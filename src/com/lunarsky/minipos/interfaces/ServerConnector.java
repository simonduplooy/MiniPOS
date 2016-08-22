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

public interface ServerConnector {
	
	public PersistenceConfig getPersistenceConfig();
	public void setPersistenceConfig(PersistenceConfig config);
	public void createPersistenceConnection();
	public void testPersistenceConnection(PersistenceConfig config);
	
	/*****************************************************************************
	 * Users 
	 *****************************************************************************/
	public List<User> getUsers();
	public User getUser(final PersistenceId id);
	public User getUserWithPassword(final String password);
	public User saveUser(final User user);
	public void deleteUser(final PersistenceId id);
	
	/*****************************************************************************
	 * Roles
	 *****************************************************************************/
	public List<Role> getRoles();
	public Role saveRole(final Role role);
	public void deleteRole(final PersistenceId id);
	
	/*****************************************************************************
	 * Stock
	 *****************************************************************************/
	public List<StockItem> getStock();
	public StockItem saveStockItem(final StockItem stockItem);
	public void deleteStockItem(final PersistenceId id);
	
	/*****************************************************************************
	 * Accounts
	 *****************************************************************************/
	public List<Account> getAccounts();
	public List<Account> getAccounts(final PersistenceId userId);
	public Account createAccount(final PersistenceId userId,final Account account);
	public void updateAccount(final Account account);
	public void deleteAccount(final PersistenceId id);
	
	/*****************************************************************************
	 * Products
	 *****************************************************************************/
	public List<Product> getProducts();
	public Product saveProduct(final Product product);
	public void deleteProduct(final PersistenceId id);
	
	/*****************************************************************************
	 * Product Buttons
	 *****************************************************************************/
	public List<ProductButtonConfig> getProductButtons();
	public ProductButtonConfig saveProductButton(final ProductButtonConfig config);
	public void deleteProductButton(final PersistenceId id);
	
	public List<ProductButtonGroupConfig> getProductButtonGroups();
	public ProductButtonGroupConfig saveProductButtonGroup(final ProductButtonGroupConfig config);
	public void deleteProductButtonGroup(final PersistenceId id);
	
	public void close();
}
