package com.swoop.data.mongo;

import com.mongodb.DB;
import com.swoop.data.util.Connection;
import org.junit.*;
import static org.junit.Assert.*;

public class MongoConnectorTest
{
	MongoConfiguration configuration;
	MockMongoConnection mockConnection;
	MongoConnector connector;

	@Before
	public void setUp() throws Exception
	{
		configuration = new MongoConfiguration();
		configuration.setMaxIdleMillis(0);    // close immediately
		mockConnection = new MockMongoConnection();
		connector = new MongoConnector(configuration, mockConnection);
	}

	@Test
	public void testOpenOnExec() throws Exception
	{
		executeNullCommand();
		assertEquals(1, mockConnection.openCount);
	}

	@Test
	public void testCloseImmediately() throws Exception
	{
		executeNullCommand();
		assertEquals(0, mockConnection.openLevel);
	}

	private void executeNullCommand() throws Exception
	{
		try {
			connector.executeDbCommand(null);
			fail("should not be reached");
		}
		catch (NullPointerException e) {
			// Expected.
		}
	}
}
