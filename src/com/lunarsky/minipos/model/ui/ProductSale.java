package com.lunarsky.minipos.model.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.dto.ProductSaleDTO;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ProductSale extends PersistenceObject {
	private static final Logger log = LogManager.getLogger();
	
	private final ObjectProperty<Product> productProperty;
	private final IntegerProperty productCountProperty;
	
	public ProductSale() {
		productProperty = new SimpleObjectProperty<Product>();
		productCountProperty = new SimpleIntegerProperty();
	}
	
	public ProductSale(final Product product, final Integer productCount) {
		this();
		setProduct(product);
		setProductCount(productCount);
	}
	
	public ProductSale(final PersistenceId id, final Product product, final Integer productCount) {
		this(product,productCount);
		setId(id);
	}
	
	public ProductSale(final ProductSale sale) {
		this(sale.getId(),sale.getProduct(),sale.getProductCount());
	}
	
	public ProductSale(final ProductSaleDTO saleDTO) {
		this();
		setDTO(saleDTO);
	}
	
	public ProductSaleDTO getDTO() {
		final ProductSaleDTO saleDTO = new ProductSaleDTO(getId().getDTO(),getProduct().getDTO(),getProductCount());
		return saleDTO;
	}
	
	public void setDTO(final ProductSaleDTO saleDTO) {
		setId(new PersistenceId(saleDTO.getId()));
		setProduct(new Product(saleDTO.getProduct()));
		setProductCount(saleDTO.getProductCount());
	}
	
	public ObjectProperty<Product> productProperty() {
		return productProperty;
	}
	
	public Product getProduct() {
		return productProperty.getValue();
	}
	
	public void setProduct(final Product product) {
		productProperty.setValue(product);
	}
	
	public IntegerProperty productCountProperty() {
		return productCountProperty;
	}
	
	public Integer getProductCount() {
		return productCountProperty.getValue();
	}
	
	public void setProductCount(final Integer count) {
		productCountProperty.setValue(count);
	}
	
	public Double getCost() {
		final Double cost = getProductCount() * getProduct().getPrice();
		return cost;
	}
	
	@Override
	public String toString() {
		return String.format("product:[%s] id:[%s] productCount:[%d]",getProduct(),getId(),getProductCount());
	}

}
