package com.lunarsky.minipos.model.dto;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.ui.PersistenceObject;
import com.lunarsky.minipos.model.ui.Product;

public class ProductSaleDTO extends PersistenceObject {

	final private Product product;
	private Integer count;
	
	public ProductSaleDTO(final Product product) {
		this(null,product);
	}
	
	public ProductSaleDTO(final PersistenceId id,final Product product) {
		super(id);
		
		assert(null != product);
		this.product = product;
		
		count = 1;
	}
	
	public Product getProduct() {
		assert(null != product);
		return product;
	}
	
	public void addSale() {
		count += 1;
	}
	
	public Double getCost() {
		final Double cost = product.getPrice() * count;
		return cost;
	}
	
	@Override
	public String toString() {
		return product.getName()+","+count;
	}
	
}
