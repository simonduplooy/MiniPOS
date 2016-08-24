package com.lunarsky.minipos.model;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.dto.PersistenceObjectDTO;
import com.lunarsky.minipos.model.dto.ProductDTO;

public class ProductSale extends PersistenceObjectDTO {

	final private ProductDTO product;
	private Integer count;
	
	public ProductSale(final ProductDTO product) {
		this(null,product);
	}
	
	public ProductSale(final PersistenceId id,final ProductDTO product) {
		super(id);
		
		assert(null != product);
		this.product = product;
		
		count = 1;
	}
	
	public ProductDTO getProduct() {
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
