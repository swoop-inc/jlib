package com.swoop.data.mongo;

import com.mongodb.DB;
import com.swoop.data.util.Connection;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DefaultMongoConnectorTest
{
	MockMongoConnection connection;
	DefaultMongoConnector connector;

	@Before
	public void setUp() throws Exception
	{
		connection = new MockMongoConnection();
		connector = new DefaultMongoConnector(connection);
		connector.setMaxIdleMillis(0);
	}

	@Test
	public void testNullCommand() throws Exception
	{
		try {
			connector.executeDbCommand(null);
			fail("should not be reached");
		}
		catch (NullPointerException e) {
			// Expected.
		}

		assertEquals(1, connection.openCount);
		assertEquals(0, connection.openLevel);
	}

	private class MockMongoConnection implements Connection<DB>
	{
		int openCount;
		int openLevel;

		@Override
		public boolean isOpen()
		{
			return openLevel > 0;
		}

		@Override
		public DB open()
		{
			++openCount;
			++openLevel;
			return null;
		}

		@Override
		public void close()
		{
			--openLevel;
		}
	}
}
