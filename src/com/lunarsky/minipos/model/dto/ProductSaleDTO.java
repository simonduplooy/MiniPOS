package com.lunarsky.minipos.model.dto;

import com.lunarsky.minipos.model.ui.Product;

public class ProductSaleDTO extends SaleDTO {

	private final ProductDTO product;
	private final Integer productCount;
	
	public ProductSaleDTO(final PersistenceIdDTO id,final ProductDTO product, final Integer productCount) {
		super(id);
		
		this.product = product;
		this.productCount = productCount;
	}
	
	public ProductDTO getProduct() {
		return product;
	}
	
	public Integer getProductCount() {
		return productCount;
	}
	
	@Override
	public String toString() {
		return String.format("id:[%s] product:[%s] productCount:[%s]",getId(),getProduct(),getProductCount());
	}
	
}
