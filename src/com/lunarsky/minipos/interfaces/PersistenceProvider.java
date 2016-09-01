package com.lunarsky.minipos.interfaces;

import java.util.List;

import com.lunarsky.minipos.model.dto.AccountDTO;
import com.lunarsky.minipos.model.dto.PersistenceConfigDTO;
import com.lunarsky.minipos.model.dto.ProductButtonConfigDTO;
import com.lunarsky.minipos.model.dto.ProductDTO;
import com.lunarsky.minipos.model.dto.ProductGroupButtonConfigDTO;
import com.lunarsky.minipos.model.dto.ProductGroupDTO;
import com.lunarsky.minipos.model.dto.RoleDTO;
import com.lunarsky.minipos.model.dto.StockItemDTO;
import com.lunarsky.minipos.model.dto.UserDTO;
import com.lunarsky.minipos.ui.XProductButtonGroup;

public interface PersistenceProvider {
	
	public PersistenceConfigDTO getConfig();
	public void setConfig(PersistenceConfigDTO config);
	public void createConnection();
	public void testConnection(PersistenceConfigDTO config);
	
	public Transaction startTransaction();
	
	/*****************************************************************************
	 * Users
	 *****************************************************************************/
	public List<UserDTO> getUsers(final Transaction transaction);
	public UserDTO getUser(final Transaction transaction, final PersistenceId id);
	public UserDTO getUserWithPassword(final Transaction transaction, final String password);	
	public UserDTO saveUser(final Transaction transaction, final UserDTO user);
	public void deleteUser(final Transaction transaction, final PersistenceId id);
	
	/*****************************************************************************
	 * Roles
	 *****************************************************************************/
	public List<RoleDTO> getRoles(final Transaction transaction);
	public RoleDTO saveRole(final Transaction transaction, final RoleDTO role);
	public void deleteRole(final Transaction transaction, final PersistenceId id);
	
	/*****************************************************************************
	 * Stock
	 *****************************************************************************/
	public List<StockItemDTO> getStock(final Transaction transaction);
	public StockItemDTO saveStockItem(final Transaction transaction, final StockItemDTO stockItem);
	public void deleteStockItem(final Transaction transaction, final PersistenceId id);
	
	/*****************************************************************************
	 * Accounts
	 *****************************************************************************/
	public List<AccountDTO> getAccounts(final Transaction transaction);
	public List<AccountDTO> getAccounts(final Transaction transaction, final PersistenceId userId);
	public AccountDTO createAccount(final Transaction transaction, final PersistenceId userId, final AccountDTO account);
	public void updateAccount(final Transaction transaction, final AccountDTO account);
	public void deleteAccount(final Transaction transaction, final PersistenceId id);
	
	/*****************************************************************************
	 * Products
	 *****************************************************************************/
	public List<ProductDTO> getProducts(final Transaction transaction);
	public ProductDTO saveProduct(final Transaction transaction, final ProductDTO product);
	public void deleteProduct(final Transaction transaction, final PersistenceId id);
	
	public List<ProductGroupDTO> getProductGroups(final Transaction transaction);
	public ProductGroupDTO saveProductGroup(final Transaction transaction, final ProductGroupDTO group);
	public void deleteProductGroup(final Transaction transaction, final PersistenceId id);
	
	/*****************************************************************************
	 * Product Buttons
	 *****************************************************************************/
	public List<ProductButtonConfigDTO> getProductButtons(final Transaction transaction);
	public ProductButtonConfigDTO saveProductButton(final Transaction transaction, final ProductButtonConfigDTO config);
	public void deleteProductButton(final Transaction transaction, final PersistenceId id);
	
	public List<ProductGroupButtonConfigDTO> getProductGroupButtons(final Transaction transaction);
	public ProductGroupButtonConfigDTO saveProductGroupButton(final Transaction transaction, final ProductGroupButtonConfigDTO config);
	public void deleteProductGroupButton(final Transaction transaction, final PersistenceId id);
	
	public void close();
	
}
