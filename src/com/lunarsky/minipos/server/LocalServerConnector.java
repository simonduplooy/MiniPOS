package com.lunarsky.minipos.server;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.db.PersistenceProviderFactory;
import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.interfaces.PersistenceProvider;
import com.lunarsky.minipos.interfaces.ServerConnector;
import com.lunarsky.minipos.interfaces.Transaction;
import com.lunarsky.minipos.model.dto.AccountDTO;
import com.lunarsky.minipos.model.dto.PersistenceConfigDTO;
import com.lunarsky.minipos.model.dto.ProductButtonConfigDTO;
import com.lunarsky.minipos.model.dto.ProductDTO;
import com.lunarsky.minipos.model.dto.ProductGroupButtonConfigDTO;
import com.lunarsky.minipos.model.dto.ProductGroupDTO;
import com.lunarsky.minipos.model.dto.RoleDTO;
import com.lunarsky.minipos.model.dto.StockItemDTO;
import com.lunarsky.minipos.model.dto.UserDTO;

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
	
	public PersistenceConfigDTO getPersistenceConfig() {
		PersistenceConfigDTO config = persistenceProvider.getConfig(); 
		return config;
	}
	
	public void setPersistenceConfig(PersistenceConfigDTO config) {
		persistenceProvider.setConfig(config);
	}

	public void createPersistenceConnection() {
		persistenceProvider.createConnection();
	}
	
	public void testPersistenceConnection(PersistenceConfigDTO config) {
		persistenceProvider.testConnection(config);
	}
	
	/*****************************************************************************
	 * Users
	 *****************************************************************************/	
	public List<UserDTO> getUsers() {
		final Transaction transaction = persistenceProvider.startTransaction();
		final List<UserDTO> users = (List<UserDTO>)persistenceCall(transaction,()->(persistenceProvider.getUsers(transaction)));
		return users;
	}
	
	public UserDTO getUser(final PersistenceId id){
		final Transaction transaction = persistenceProvider.startTransaction();
		final UserDTO user = (UserDTO)persistenceCall(transaction,()->(persistenceProvider.getUser(transaction, id)));
		return user;
	}
	
	public UserDTO getUserWithPassword(final String password) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final UserDTO user = (UserDTO)persistenceCall(transaction,()->(persistenceProvider.getUserWithPassword(transaction,password)));
		return user;
	}
	
	public UserDTO saveUser(final UserDTO user) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final UserDTO updatedUser = (UserDTO)persistenceCall(transaction,()->(persistenceProvider.saveUser(transaction, user)));
		return updatedUser;
	}

	public void deleteUser(final PersistenceId id) {
		final Transaction transaction = persistenceProvider.startTransaction();
		persistenceCall(transaction,()->{persistenceProvider.deleteUser(transaction, id);return null;});
	}
	
	/*****************************************************************************
	 * Roles
	 *****************************************************************************/
	public List<RoleDTO> getRoles() {
		final Transaction transaction = persistenceProvider.startTransaction();
		final List<RoleDTO> roles= (List<RoleDTO>)persistenceCall(transaction,()->(persistenceProvider.getRoles(transaction)));
		return roles;
	}
	
	/*****************************************************************************
	 * Roles
	 *****************************************************************************/
	public RoleDTO saveRole(final RoleDTO role) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final RoleDTO updatedRole= (RoleDTO)persistenceCall(transaction,()->(persistenceProvider.saveRole(transaction, role)));
		return updatedRole;
	}

	public void deleteRole(final PersistenceId id) {
		final Transaction transaction = persistenceProvider.startTransaction();
		persistenceCall(transaction,()->{persistenceProvider.deleteRole(transaction, id);return null;});
	}

	/*****************************************************************************
	 * Stock
	 *****************************************************************************/
	public List<StockItemDTO> getStock() {
		final Transaction transaction = persistenceProvider.startTransaction();
		final List<StockItemDTO> stockItems = (List<StockItemDTO>)persistenceCall(transaction,()->(persistenceProvider.getStock(transaction)));
		return stockItems;
	}
	
	public StockItemDTO saveStockItem(final StockItemDTO stockItem) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final StockItemDTO updatedStockItem = (StockItemDTO)persistenceCall(transaction,()->(persistenceProvider.saveStockItem(transaction, stockItem)));
		return updatedStockItem;
	}
	
	public void deleteStockItem(final PersistenceId id) {
		final Transaction transaction = persistenceProvider.startTransaction();
		persistenceCall(transaction,()->{persistenceProvider.deleteStockItem(transaction, id);return null;});
	}

	/*****************************************************************************
	 * Accounts
	 *****************************************************************************/
	public List<AccountDTO> getAccounts() {
		final Transaction transaction = persistenceProvider.startTransaction();
		final List<AccountDTO> accounts = (List<AccountDTO>)persistenceCall(transaction,()->(persistenceProvider.getAccounts(transaction)));
		return accounts;		
	}

	public List<AccountDTO> getAccounts(final PersistenceId userId) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final List<AccountDTO> accounts = (List<AccountDTO>)persistenceCall(transaction,()->(persistenceProvider.getAccounts(transaction,userId)));
		return accounts;		
	}
	public AccountDTO createAccount(final PersistenceId userId, final AccountDTO account) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final AccountDTO updatedAccount = (AccountDTO)persistenceCall(transaction,()->(persistenceProvider.createAccount(transaction, userId, account)));
		return updatedAccount;
	}

	public void updateAccount(final AccountDTO account) {
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
	public List<ProductDTO> getProducts() {
		final Transaction transaction = persistenceProvider.startTransaction();
		final List<ProductDTO> products = (List<ProductDTO>)persistenceCall(transaction,()->(persistenceProvider.getProducts(transaction)));
		return products;
	}
	
	public ProductDTO saveProduct(final ProductDTO product) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final ProductDTO updatedProduct = (ProductDTO)persistenceCall(transaction,()->(persistenceProvider.saveProduct(transaction, product)));
		return updatedProduct;
	}

	public void deleteProduct(final PersistenceId id) {
		final Transaction transaction = persistenceProvider.startTransaction();
		persistenceCall(transaction,()->{persistenceProvider.deleteProduct(transaction, id);return null;});	
	}
	
	public List<ProductGroupDTO> getProductGroups() {
		final Transaction transaction = persistenceProvider.startTransaction();
		final List<ProductGroupDTO> groups = (List<ProductGroupDTO>)persistenceCall(transaction,()->(persistenceProvider.getProductGroups(transaction)));
		return groups;
	}
	
	public ProductGroupDTO saveProductGroup(final ProductGroupDTO group) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final ProductGroupDTO updatedGroup = (ProductGroupDTO)persistenceCall(transaction,()->(persistenceProvider.saveProductGroup(transaction, group)));
		return updatedGroup;
	}
	
	public void deleteProductGroup(final PersistenceId id) {
		final Transaction transaction = persistenceProvider.startTransaction();
		persistenceCall(transaction,()->{persistenceProvider.deleteProductGroup(transaction, id);return null;});	
	}
	
	/*****************************************************************************
	 * Product Buttons
	 *****************************************************************************/
	public List<ProductButtonConfigDTO> getProductButtons() {
		final Transaction transaction = persistenceProvider.startTransaction();
		final List<ProductButtonConfigDTO> buttons = (List<ProductButtonConfigDTO>)persistenceCall(transaction,()->(persistenceProvider.getProductButtons(transaction)));
		return buttons;
	}
	
	public ProductButtonConfigDTO saveProductButton(final ProductButtonConfigDTO config) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final ProductButtonConfigDTO updatedConfig = (ProductButtonConfigDTO)persistenceCall(transaction,()->(persistenceProvider.saveProductButton(transaction, config)));
		return updatedConfig;
	}
	
	public void deleteProductButton(final PersistenceId id) {
		final Transaction transaction = persistenceProvider.startTransaction();
		persistenceCall(transaction,()->{persistenceProvider.deleteProductButton(transaction,id);return null;});
	}
	
	public List<ProductGroupButtonConfigDTO> getProductGroupButtons() {
		final Transaction transaction = persistenceProvider.startTransaction();
		final List<ProductGroupButtonConfigDTO> buttons = (List<ProductGroupButtonConfigDTO>)persistenceCall(transaction,()->(persistenceProvider.getProductGroupButtons(transaction)));
		return buttons;
	}
	
	public ProductGroupButtonConfigDTO saveProductGroupButton(final ProductGroupButtonConfigDTO config) {
		final Transaction transaction = persistenceProvider.startTransaction();
		final ProductGroupButtonConfigDTO updatedConfig = (ProductGroupButtonConfigDTO)persistenceCall(transaction,()->(persistenceProvider.saveProductGroupButton(transaction, config)));
		return updatedConfig;
	}
	
	public void deleteProductGroupButton(final PersistenceId id) {
		final Transaction transaction = persistenceProvider.startTransaction();
		persistenceCall(transaction,()->{persistenceProvider.deleteProductGroupButton(transaction,id);return null;});
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
