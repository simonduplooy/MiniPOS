package com.lunarsky.minipos.model.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.model.dto.ProductSaleDTO;
import com.lunarsky.minipos.model.dto.SaleDTO;
import com.lunarsky.minipos.model.dto.SaleOrderDTO;

import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SaleOrder extends PersistenceObject {
	private static final Logger log = LogManager.getLogger();
	
	private final Date creationDate;
	private final ObservableList<Sale> saleList;
	private final DoubleProperty totalProperty;
	private NumberBinding totalBinding;

	
	public SaleOrder() {
		creationDate = new Date();
		saleList = FXCollections.observableArrayList();
		totalProperty = new SimpleDoubleProperty();
	}
	
	public SaleOrder(final SaleOrderDTO order) {
		this();
		for(SaleDTO sale: order.getSales()) {
			if(sale instanceof ProductSaleDTO) {
				final ProductSaleDTO productSaleDTO = (ProductSaleDTO)sale;
				final ProductSale productSale = new ProductSale(productSaleDTO);
				addSale(productSale);
			} else {
				throw new IllegalArgumentException();
			}
		}
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void addSale(final Sale sale) {
		saleList.add(sale);
		if(null == totalBinding) {
			totalBinding = sale.totalProperty().add(0.0);
		} else {
			totalBinding = totalBinding.add(sale.totalProperty());
		}
		totalProperty.bind(totalBinding);
	}
	
	public ObservableList<Sale> getSales() {
		return saleList;
	}

	public DoubleProperty totalProperty() {
		return totalProperty;
	}
	
	public ProductSale getProductSale(final Product product) {
		
		for(Sale sale: saleList) {
			if(sale instanceof ProductSale) {
				final ProductSale productSale = (ProductSale)sale;
				if(productSale.getProduct().equals(product)) {
					return productSale;
				}
			}
		}
		
		return null;
	}
	
	public SaleOrderDTO getDTO() {

		final List<SaleDTO> saleDTOList = new ArrayList<SaleDTO>();
		for(Sale sale: saleList) {
			saleDTOList.add(sale.getDTO());
		}
		final SaleOrderDTO dto = new SaleOrderDTO(getId().getDTO(),getCreationDate(),saleDTOList);
		return dto;
	}
	
}
