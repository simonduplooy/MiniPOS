package com.lunarsky.minipos.ui.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.Product;

public class UiProduct extends UiProductBase {
	private static final Logger log = LogManager.getLogger();
	
	private final Product product;
	
	public UiProduct(final Product product) {
		if(null == product) {throw new IllegalArgumentException();}
		this.product = product;
	}
	
	public Product getProduct() {
		assert(null != product);
		return product;
	}
	
	public String getName() {
		return getProduct().getName();
	}
	
}
