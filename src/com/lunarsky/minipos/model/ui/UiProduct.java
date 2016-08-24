package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.dto.ProductDTO;

public class UiProduct extends UiProductBase {
	private static final Logger log = LogManager.getLogger();
	
	private final ProductDTO product;
	
	public UiProduct(final ProductDTO product) {
		if(null == product) {throw new IllegalArgumentException();}
		this.product = product;
	}
	
	public ProductDTO getProduct() {
		assert(null != product);
		return product;
	}
	
	public String getName() {
		return getProduct().getName();
	}
	
}
