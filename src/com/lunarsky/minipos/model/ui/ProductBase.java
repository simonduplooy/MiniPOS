package com.lunarsky.minipos.model.ui;

import com.lunarsky.minipos.interfaces.PersistenceId;

import javafx.beans.property.StringProperty;

public abstract class ProductBase extends PersistenceObject {

	public abstract String getName();
	public abstract PersistenceId getParentId();

}
