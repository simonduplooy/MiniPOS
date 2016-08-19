package com.lunarsky.minipos.interfaces;

import java.util.List;

import com.lunarsky.minipos.model.Account;
import com.lunarsky.minipos.model.PersistenceConfig;
import com.lunarsky.minipos.model.Product;
import com.lunarsky.minipos.model.Role;
import com.lunarsky.minipos.model.StockItem;
import com.lunarsky.minipos.model.User;

public interface ServerConnector {
	
	public PersistenceConfig getPersistenceConfig();
	public void setPersistenceConfig(PersistenceConfig config);
	public void createPersistenceConnection();
	public void testPersistenceConnection(PersistenceConfig config);
	
	public List<User> getUsers();
	public User getUser(final PersistenceId id);
	public User getUserWithPassword(final String password);
	public User saveUser(final User user);
	public void deleteUser(final PersistenceId id);
	
	public List<Role> getRoles();
	public Role saveRole(final Role role);
	public void deleteRole(final PersistenceId id);
	
	public List<StockItem> getStock();
	public StockItem saveStockItem(final StockItem stockItem);
	public void deleteStockItem(final PersistenceId id);
		
	public List<Account> getAccounts();
	public Account createAccount(final PersistenceId userId,final Account account);
	public void updateAccount(final Account account);
	public void deleteAccount(final PersistenceId id);
	
	public List<Product> getProducts();
	public Product saveProduct(final Product product);
	public void deleteProduct(final PersistenceId id);
	
	public void close();
}
