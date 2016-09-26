package com.lunarsky.minipos.model.dto;

import java.util.Date;

public class ProductSaleDTO extends SaleDTO {

	private final ProductDTO product;
	private final Integer count;
	private final Double discount;
	
	public ProductSaleDTO(final PersistenceIdDTO id,final ProductDTO product, final Integer count, final Double discount) {
		super(id);
		
		this.product = product;
		this.count = count;
		this.discount = discount;
	}
	
	
	public ProductDTO getProduct() {
		return product;
	}
	
	public Integer getCount() {
		return count;
	}
	
	public Double getDiscount() {
		return discount;
	}
	
	@Override
	public String toString() {
		return String.format("id:[%s] product:[%s] count:[%s] discount:[%.2f]",getId(),getProduct(),getCount(),getDiscount());
	}
	
}
