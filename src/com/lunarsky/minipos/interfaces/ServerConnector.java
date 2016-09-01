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

public interface ServerConnector {
	
	public PersistenceConfigDTO getPersistenceConfig();
	public void setPersistenceConfig(PersistenceConfigDTO config);
	public void createPersistenceConnection();
	public void testPersistenceConnection(PersistenceConfigDTO config);
	
	/*****************************************************************************
	 * Users 
	 *****************************************************************************/
	public List<UserDTO> getUsers();
	public UserDTO getUser(final PersistenceId id);
	public UserDTO getUserWithPassword(final String password);
	public UserDTO saveUser(final UserDTO user);
	public void deleteUser(final PersistenceId id);
	
	/*****************************************************************************
	 * Roles
	 *****************************************************************************/
	public List<RoleDTO> getRoles();
	public RoleDTO saveRole(final RoleDTO role);
	public void deleteRole(final PersistenceId id);
	
	/*****************************************************************************
	 * Stock
	 *****************************************************************************/
	public List<StockItemDTO> getStock();
	public StockItemDTO saveStockItem(final StockItemDTO stockItem);
	public void deleteStockItem(final PersistenceId id);
	
	/*****************************************************************************
	 * Accounts
	 *****************************************************************************/
	public List<AccountDTO> getAccounts();
	public List<AccountDTO> getAccounts(final PersistenceId userId);
	public AccountDTO createAccount(final PersistenceId userId,final AccountDTO account);
	public void updateAccount(final AccountDTO account);
	public void deleteAccount(final PersistenceId id);
	
	/*****************************************************************************
	 * Products
	 *****************************************************************************/
	public List<ProductDTO> getProducts();
	public ProductDTO saveProduct(final ProductDTO product);
	public void deleteProduct(final PersistenceId id);
	
	public List<ProductGroupDTO> getProductGroups();
	public ProductGroupDTO saveProductGroup(final ProductGroupDTO group);
	public void deleteProductGroup(final PersistenceId id);
	
	/*****************************************************************************
	 * Product Buttons
	 *****************************************************************************/
	public List<ProductButtonConfigDTO> getProductButtons();
	public ProductButtonConfigDTO saveProductButton(final ProductButtonConfigDTO config);
	public void deleteProductButton(final PersistenceId id);
	
	public List<ProductGroupButtonConfigDTO> getProductGroupButtons();
	public ProductGroupButtonConfigDTO saveProductGroupButton(final ProductGroupButtonConfigDTO config);
	public void deleteProductGroupButton(final PersistenceId id);
	
	public void close();
}
