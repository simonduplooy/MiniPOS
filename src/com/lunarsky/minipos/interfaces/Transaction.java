package com.lunarsky.minipos.interfaces;

public interface Transaction {
	public void commit();
	public void rollback();
}