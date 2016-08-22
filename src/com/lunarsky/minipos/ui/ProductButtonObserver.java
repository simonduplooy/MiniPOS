package com.lunarsky.minipos.ui;

import com.lunarsky.minipos.model.Product;
import com.lunarsky.minipos.model.ProductButtonGroupConfig;

public interface ProductButtonObserver {
	public void createProductButton(final Integer columnIdx, final Integer rowIdx);
	public void updateProductButton(final ProductButton button);
	public void productSelected(final Product product);
	public void deleteProductButton(final ProductButton button);

	public void createProductButtonGroup(final Integer columnIdx, final Integer rowIdx);
	public void updateProductButtonGroup(final ProductButtonGroup button);
	public void productButtonGroupSelected(final ProductButtonGroupConfig config);
	public void deleteProductButtonGroup(final ProductButtonGroup button);
}