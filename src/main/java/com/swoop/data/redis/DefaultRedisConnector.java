package com.swoop.data.redis;

import com.swoop.data.util.Connection;
import com.swoop.data.util.ConnectionMonitor;

import redis.clients.jedis.exceptions.JedisException;
import java.io.IOException;

/**
 * Implementation of RedisConnector based on the Jedis driver.
 */
public class DefaultRedisConnector
	implements RedisConnector
{
	private ConnectionMonitor<SwoopBinaryJedisCommands> conn;

	public DefaultRedisConnector()
	{
		this(new RedisConnection());
	}

	public DefaultRedisConnector(RedisConnectorConfig config)
	{
		this(new RedisConnection(config.copy()));
	}

	/**
	 * Allows for mocking of Redis connection.
	 */
	public DefaultRedisConnector(Connection<SwoopBinaryJedisCommands> connection)
	{
		this.conn = new ConnectionMonitor<SwoopBinaryJedisCommands>(connection);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public final <T> T execute(RedisCommand<T> command)
		throws IOException
	{
		try {
			SwoopBinaryJedisCommands jedis = conn.use();
			try {
				return command.execute(jedis);
			}
			finally {
				conn.release();
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
		return conn.toString();
	}
}
