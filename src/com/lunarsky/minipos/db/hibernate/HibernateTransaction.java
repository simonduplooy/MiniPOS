package com.lunarsky.minipos.db.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.ExceptionUtil;
import com.lunarsky.minipos.interfaces.Transaction;

public class HibernateTransaction implements Transaction {
	private static final Logger log = LogManager.getLogger();

	private final EntityManager entityManager;
	
	public HibernateTransaction(final EntityManagerFactory entityManagerFactory) {
		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
	}
	
	public EntityManager getEntityManager() {
		assert( null != entityManager);
		return entityManager;
	}
	
	public void commit() {
		try {
			//TODO Remove
			/*
			try {Thread.sleep(1000);} catch (InterruptedException e) {}
			*/
			entityManager.getTransaction().commit();
		} catch (RuntimeException exception) {
			ExceptionUtil.translate(exception);
		} finally {
			entityManager.close();
		}
	}
	
	public void rollback() {
		try {
			entityManager.getTransaction().rollback();
		} finally {
			entityManager.close();
		}
	}
	
}
