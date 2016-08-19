package com.lunarsky.minipos.ui;

import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.Product;

public interface ProductButtonObserver {
	public void productSelected(final Product product);
	public void productButtonGroupSelected(final PersistenceId id);
	public void createProductButton(final Integer columnIdx, final Integer rowIdx);
	public void createProductButtonGroup(final Integer columnIdx, final Integer rowIdx);
	public void deleteProductButton(final ProductButton button);
	public void deleteProductButtonGroup(final ProductButtonGroup button);
}