package com.lunarsky.minipos.model;

import com.lunarsky.minipos.interfaces.PersistenceId;

public class ProductSale extends PersistenceObject {

	final private Product product;
	private Integer count;
	
	public ProductSale(final Product product) {
		this(null,product);
	}
	
	public ProductSale(final PersistenceId id,final Product product) {
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
