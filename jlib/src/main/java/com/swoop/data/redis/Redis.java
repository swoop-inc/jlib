package com.swoop.data.redis;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

import com.swoop.data.util.Connection;
import com.swoop.data.util.ConnectionMonitor;

import redis.clients.jedis.exceptions.JedisException;

/**
 * Maintain configuration of jlib Redis connectors.  This class maintains a singleton instance of itself
 * for use by default throughout the application.
 */
public class Redis
{
	public final static int DEFAULT_MAX_IDLE_MILLIS = 10 * 10000;
	public final static int DEFAULT_TIMEOUT_MILLIS = 3 * 10000;
	public final static int DEFAULT_CONCURRENCY = 3;

	public final static String DEFAULT_DEFAULT_CONNECTION_STRING = "redis://localhost";

	/**
	 * Singleton instance used by default by RedisConnector constructors.
	 */
	public static Redis instance = new Redis();

	private int maxIdleMillis = DEFAULT_MAX_IDLE_MILLIS;
	private int timeoutMillis = DEFAULT_TIMEOUT_MILLIS;
	private int concurrency = DEFAULT_CONCURRENCY;
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
	 * The application's default timeout period for an I/O operation.
	 */
	public void setTimeoutMillis(int timeoutMillis)
	{
		this.timeoutMillis = timeoutMillis;
	}

	/**
	 * The application's default concurrency level per connector.
	 */
	public void setConcurrency(int concurrency)
	{
		this.concurrency = concurrency;
	}

	/**
	 * Map from connection key to connection string.  A connection string must be in valid
	 * Redis URI format.  A connection key may be any string at all.  This enables the application
	 * to map logical names for Redis databases to their connection strings dynamically.
	 * @see com.swoop.data.redis.RedisUri
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
	ConnectionMonitor<SwoopBinaryJedisCommands> monitorConnection(Connection<SwoopBinaryJedisCommands> connection)
	{
		ConnectionMonitor<SwoopBinaryJedisCommands> monitor = new ConnectionMonitor<SwoopBinaryJedisCommands>(connection);

		if (maxIdleMillis >= 0) {
			monitor.setMaxIdleMillis(maxIdleMillis);
		}

		return monitor;
	}

	/**
	 * Resolve the given key to a Redis connector configuration
	 * @param key
	 *     the key to the desired connector configuration
	 */
	RedisConnectorConfig getConnectorConfig(String key)
	{
		String connectionString = connectionStringMap.containsKey(key) ? connectionStringMap.get(key) : defaultConnectionString;
		if (connectionString == null) {
			throw new JedisException(key + ": no mapping for configuration key");
		}

		try {
			RedisConnectorConfig config = new RedisConnectorConfig();
			config.setUri(new RedisUri(connectionString));
			config.setTimeoutMillis(timeoutMillis);
			config.setConcurrency(concurrency);
			return config;
		}
		catch (URISyntaxException e) {
			throw new JedisException("Redis URI format error", e);
		}
	}
}
