package com.swoop.data.redis;

import java.io.IOException;
import java.util.Stack;

import com.swoop.data.util.Connection;
import com.swoop.data.util.ConnectionMonitor;

import redis.clients.jedis.exceptions.JedisException;

/**
 * A Redis connector based on the Jedis driver.
 */
public class RedisConnector
	implements IRedisConnector
{
	private RedisConnectorConfig config;
	private Stack<ConnectionMonitor<SwoopBinaryJedisCommands>> available
		= new Stack<ConnectionMonitor<SwoopBinaryJedisCommands>>();

	public RedisConnector()
	{
		this(new RedisConnectorConfig());
	}

	public RedisConnector(RedisConnectorConfig config)
	{
		this(Redis.instance, config);
	}

	/**
	 * Constructor.
	 * @param key 
	 *    key to be mapped to a Redis connection configuration by the singleton
	 *    {@link com.swoop.data.redis.Redis} instance
	 */
	public RedisConnector(String key)
	{
		this(Redis.instance, key);
	}

	/**
	 * Constructor.
	 * @param redis
	 *    system configuration
	 * @param connectionKey 
	 *    logical name that may be mapped to a Redis connection config by the system configuration
	 */
	public RedisConnector(Redis redis, String connectionKey)
	{
		this(redis, redis.getConnectorConfig(connectionKey));
	}

	private RedisConnector(Redis redis, RedisConnectorConfig config)
	{
		this.config = config.copy();
		for (int i = 0; i < config.getConcurrency(); ++i) {
			available.push(redis.monitorConnection(new RedisConnection(this.config)));
		}
	}

	/**
	 * Constructor for internal use only.  Allows for mocking of Redis connection.
	 */
	public RedisConnector(Connection<SwoopBinaryJedisCommands> connection)
	{
		this.config = new RedisConnectorConfig();
		available.push(new ConnectionMonitor<SwoopBinaryJedisCommands>(connection));
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public final <T> T execute(RedisCommand<T> command)
		throws IOException
	{
		try {
			ConnectionMonitor<SwoopBinaryJedisCommands> conn = waitForConnection();
			SwoopBinaryJedisCommands jedis = conn.use();
			try {
				return command.execute(jedis);
			}
			finally {
				reuseConnection(conn);
			}
		}
		catch (JedisException e) {
			// Wrap all exceptions thrown by the driver in IOExceptions.
			throw new IOException(this + ": " + command + ": data transfer error", e);
		}
	}

	@Override
	public String toString()
	{
		return config.toString();
	}

	private ConnectionMonitor<SwoopBinaryJedisCommands> waitForConnection()
	{
		synchronized (available) {
			while (available.isEmpty()) {
				try {
					available.wait();
				}
				catch (InterruptedException e) {
				}
			}
			return available.pop();
		}
	}
	
	private void reuseConnection(ConnectionMonitor<SwoopBinaryJedisCommands> conn)
	{
		conn.release();
		synchronized (available) {
			available.push(conn);
			available.notifyAll();
		}
	}
}
