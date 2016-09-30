package com.lunarsky.minipos.interfaces;

import java.util.List;

import com.lunarsky.minipos.model.dto.AccountDTO;
import com.lunarsky.minipos.model.dto.PersistenceConfigDTO;
import com.lunarsky.minipos.model.dto.PersistenceIdDTO;
import com.lunarsky.minipos.model.dto.ProductButtonConfigDTO;
import com.lunarsky.minipos.model.dto.ProductDTO;
import com.lunarsky.minipos.model.dto.ProductGroupButtonConfigDTO;
import com.lunarsky.minipos.model.dto.ProductGroupDTO;
import com.lunarsky.minipos.model.dto.SaleOrderDTO;
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
	public UserDTO getUser(final PersistenceIdDTO id);
	public UserDTO getUserWithPassword(final String password);
	public UserDTO saveUser(final UserDTO user);
	public void deleteUser(final PersistenceIdDTO id);
	
	/*****************************************************************************
	 * Accounts
	 *****************************************************************************/
	public List<AccountDTO> getAccounts();
	public List<AccountDTO> getAccounts(final PersistenceIdDTO userId);
	public AccountDTO createAccount(final PersistenceIdDTO userId,final AccountDTO account);
	public void updateAccount(final AccountDTO account);
	public void deleteAccount(final PersistenceIdDTO id);
	public List<SaleOrderDTO> getSaleOrders(final PersistenceIdDTO accountId);
	public void addSaleOrder(final PersistenceIdDTO accountId, final SaleOrderDTO order);
	
	/*****************************************************************************
	 * Products
	 *****************************************************************************/
	public List<ProductDTO> getProducts();
	public ProductDTO saveProduct(final ProductDTO product);
	public void deleteProduct(final PersistenceIdDTO id);
	
	public List<ProductGroupDTO> getProductGroups();
	public ProductGroupDTO saveProductGroup(final ProductGroupDTO group);
	public void deleteProductGroup(final PersistenceIdDTO id);
	
	/*****************************************************************************
	 * Product Buttons
	 *****************************************************************************/
	public List<ProductButtonConfigDTO> getProductButtons();
	public ProductButtonConfigDTO saveProductButton(final ProductButtonConfigDTO config);
	public void deleteProductButton(final PersistenceIdDTO id);
	
	public List<ProductGroupButtonConfigDTO> getProductGroupButtons();
	public ProductGroupButtonConfigDTO saveProductGroupButton(final ProductGroupButtonConfigDTO config);
	public void deleteProductGroupButton(final PersistenceIdDTO id);
	
	public void close();
}
