package com.lunarsky.minipos.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;

public class Product extends PersistenceObject implements Comparable<Product> {
	private static final Logger log = LogManager.getLogger();
	
	final String name;
	final Double price;
	
	public Product(final String name, final Double price) {
		this(null,name,price);
	}
	
	public Product(final PersistenceId id, final String name, final Double price) {
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
	
	public Product duplicate() {
		final Product product = new Product(getName(),getPrice());
		return product;
	}
	
	public String toString() {
		return getName();
	}

	//Implements Comparable
	public int compareTo(Product product) {
		return getName().compareToIgnoreCase(product.getName());
	}
}
