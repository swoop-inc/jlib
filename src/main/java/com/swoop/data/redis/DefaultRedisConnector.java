package com.swoop.data.redis;

import java.io.IOException;

// TODO: try using the redis-protocol driver for better efficiency
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Implementation of RedisConnector based on the Jedis driver.
 */
public class DefaultRedisConnector
	implements RedisConnector
{
	private RedisConnectorConfig config;
	private BinaryJedis jedis;

	public DefaultRedisConnector()
	{
		this.config = new RedisConnectorConfig();
	}

	public DefaultRedisConnector(RedisConnectorConfig config)
	{
		this.config = config.copy();
	}

	@Override
	public final <T> T execute(RedisCommand<T> command)
		throws IOException
	{
		// This initial implementation reconnects for every command!  
		// TODO: add multi-threading, synchronization, and connection pooling.
		BinaryJedisCommands jedis = connect();
		try {
			return command.execute(jedis);
		}
		catch (JedisException e) {
			throw new IOException(this + ": " + command + ": data transfer error", e);
		}
		finally {
			disconnect();
		}
	}

	/**
	 * Exposed for mockery.
	 */
	protected BinaryJedisCommands connect()
		throws IOException
	{
		try {
			jedis = new BinaryJedis(config.getHost(), config.getPort(), config.getTimeoutMillis());
			// select() implicitly connects.  Set authentication info first.
			if (config.getPassword() != null) {
				jedis.auth(config.getPassword());
			}
			jedis.select(config.getDatabase());
			jedis.connect();
			return jedis;
		}
		catch (JedisException e) {
			throw new IOException(config.toString() + ": connection error", e);
		}
	}

	/**
	 * Exposed for mockery.
	 */
	protected void disconnect()
	{
		jedis.disconnect();
	}

	@Override
	public String toString()
	{
		return config.toString();
	}
}
