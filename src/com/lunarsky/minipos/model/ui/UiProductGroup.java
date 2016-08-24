package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.ProductGroup;
import com.lunarsky.minipos.model.dto.ProductDTO;

public class UiProductGroup extends UiProductBase {
	private static final Logger log = LogManager.getLogger();
	
	private final ProductGroup group;
	
	public UiProductGroup(final ProductGroup group) {
		if(null == group) {throw new IllegalArgumentException();}
		this.group = group;
	}
	
	public ProductGroup getProductGroup() {
		assert(null != group);
		return group;
	}
	
	public String getName() {
		return getProductGroup().getName();
	}
	
}
