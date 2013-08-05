package com.swoop.data.mongo;

import java.util.Collections;
import java.util.Map;

import com.swoop.data.util.Connection;
import com.swoop.data.util.ConnectionMonitor;

import com.mongodb.DB;
import com.mongodb.MongoException;

/**
 * Maintain configuration of jlib MongoDB connectors.  This class maintains a singleton instance of itself
 * for use by default throughout the application.
 */
public class MongoConfiguration
{
	public final static int DEFAULT_MAX_IDLE_MILLIS = 10 * 10000;

	public final static String DEFAULT_DEFAULT_CONNECTION_STRING = "mongodb://localhost/test";

	/**
	 * Singleton instance used by default by MongoConnector constructors.
	 */
	public static MongoConfiguration instance = new MongoConfiguration();

	private int maxIdleMillis = DEFAULT_MAX_IDLE_MILLIS;
	private Map<String,String> connectionStringMap = Collections.EMPTY_MAP;
	private String defaultConnectionString = DEFAULT_DEFAULT_CONNECTION_STRING;

	/**
	 * The maximum time a connection produced via this system is allowed to remain idle, before it
	 * is automatically closed.  If this value is zero, each connection is closed immediately when not
	 * in use.
	 */
	public void setMaxIdleMillis(int maxIdleMillis)
	{
		this.maxIdleMillis = maxIdleMillis;
	}

	/**
	 * Map from connection key to connection string.  A connection string must be in valid
	 * MongoDB URI format.  A connection key may be any string at all.  This enables the application
	 * to map logical names for MongoDB databases to their connection strings dynamically.
	 */
	public void setConnectionStringMap(Map<String,String> connectionStringMap)
	{
		this.connectionStringMap = connectionStringMap;
	}

	/**
	 * Default connection string, used in the case of a connection key for which there is
	 * no mapping.
	 */
	public void setDefaultConnectionString(String defaultConnectionString)
	{
		this.defaultConnectionString = defaultConnectionString;
	}

	/**
	 * Create a connection monitor for the given connection.
	 */
	ConnectionMonitor<DB> monitorConnection(Connection<DB> connection)
	{
		ConnectionMonitor<DB> monitor = new ConnectionMonitor<DB>(connection);

		if (maxIdleMillis >= 0) {
			monitor.setMaxIdleMillis(maxIdleMillis);
		}

		return monitor;
	}

	/**
	 * Resolve the given key to a connection string.
	 * @param configurationKey
	 *     the key to the desired connection string
	 */
	String getConnectionString(String connectionKey)
	{
		String connectionString = connectionStringMap.containsKey(connectionKey) ? connectionStringMap.get(connectionKey) : defaultConnectionString;
		if (connectionString == null) {
			throw new MongoException(connectionKey + ": no mapping for connection key");
		}
		return connectionString;
	}
}
