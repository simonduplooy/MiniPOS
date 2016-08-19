package com.lunarsky.minipos.db.hibernate;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserManagerTest {

	private HibernatePersistenceProvider persistenceProvider;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		persistenceProvider = new HibernatePersistenceProvider();
	}

	@After
	public void tearDown() throws Exception {
		persistenceProvider.close();
	}

	@Test
	public final void testGetUsers() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetUser() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetUserWithPassword() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSaveUser() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testDeleteUser() {
		fail("Not yet implemented"); // TODO
	}

}
