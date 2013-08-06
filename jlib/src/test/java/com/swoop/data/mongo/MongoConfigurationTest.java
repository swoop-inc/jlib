package com.swoop.data.mongo;

import com.mongodb.DB;
import com.mongodb.MongoException;
import com.swoop.data.util.Connection;
import com.swoop.data.util.ConnectionMonitor;
import java.util.Collections;
import org.junit.*;
import static org.junit.Assert.*;

public class MongoConfigurationTest
{
	@Test
	public void testSingletonInstance() throws Exception
	{
		assertNotNull(MongoConfiguration.instance);
	}

	@Test
	public void testDefaultMaxIdleMillis() throws Exception
	{
		ConnectionMonitor<DB> monitor = new MongoConfiguration().monitorConnection(new MockMongoConnection());
		assertEquals(MongoConfiguration.DEFAULT_MAX_IDLE_MILLIS, monitor.getMaxIdleMillis());
	}

	@Test
	public void testSetMaxIdleMillis() throws Exception
	{
		MongoConfiguration configuration = new MongoConfiguration();
		configuration.setMaxIdleMillis(24);
		ConnectionMonitor<DB> monitor = configuration.monitorConnection(new MockMongoConnection());
		assertEquals(24, monitor.getMaxIdleMillis());
	}

	@Test
	public void testDefaultConnectionStringMapping() throws Exception
	{
		assertEquals(MongoConfiguration.DEFAULT_DEFAULT_CONNECTION_STRING, new MongoConfiguration().getConnectionString("a"));
	}

	@Test
	public void testMissingConnectionStringMapping() throws Exception
	{
		MongoConfiguration configuration = new MongoConfiguration();
		configuration.setDefaultConnectionString(null);
		try {
			configuration.getConnectionString("a");
		}
		catch (MongoException e) {
			assertEquals("a: no mapping for connection key", e.getMessage());
		}
	}

	@Test
	public void testConnectionStringMapping() throws Exception
	{
		MongoConfiguration configuration = new MongoConfiguration();
		configuration.setConnectionStringMap(Collections.singletonMap("a", "mongodb://example"));
		assertEquals("mongodb://example", configuration.getConnectionString("a"));
	}
}
