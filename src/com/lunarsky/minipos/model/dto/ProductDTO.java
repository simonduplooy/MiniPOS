package com.lunarsky.minipos.model.dto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.PersistenceObject;

public class ProductDTO extends PersistenceObject implements Comparable<ProductDTO> {
	private static final Logger log = LogManager.getLogger();
	
	final String name;
	final Double price;
	
	public ProductDTO(final String name, final Double price) {
		this(null,name,price);
	}
	
	public ProductDTO(final PersistenceId id, final String name, final Double price) {
		super(id);
		
		assert(null != name);
		assert(null != price);
		
		this.name = name;
		this.price = price;
	}
	
	public String getName() {
		assert(null != name);
		return name;
	}
	
	public Double getPrice() {
		assert(null != price);
		return price;
	}
	
	//TODO Remove
	public ProductDTO duplicate() {
		final ProductDTO product = new ProductDTO(getName(),getPrice());
		return product;
	}
	
	public String toString() {
		return getName();
	}

	//Implements Comparable
	public int compareTo(ProductDTO product) {
		return getName().compareToIgnoreCase(product.getName());
	}
}
