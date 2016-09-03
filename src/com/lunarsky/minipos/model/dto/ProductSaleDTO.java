package com.lunarsky.minipos.model.dto;

import com.lunarsky.minipos.model.ui.Product;

public class ProductSaleDTO extends PersistenceObjectDTO {

	private final Product product;
	private final Integer count;
	
	public ProductSaleDTO(final PersistenceIdDTO id,final Product product, final Integer count) {
		super(id);
		
		this.product = product;
		this.count = count;
	}
	
	public Product getProduct() {
		return product;
	}
	
	public Integer getCount() {
		return count;
	}
	

	@Override
	public String toString() {
		return String.format("id:[%s] product:[%s] count:[%s]",getId(),getProduct(),getCount());
	}
	
}
