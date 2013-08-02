package com.swoop.data.redis;

import java.io.IOException;
import java.util.Stack;

import com.swoop.data.util.Connection;
import com.swoop.data.util.ConnectionMonitor;

import redis.clients.jedis.exceptions.JedisException;

/**
 * Implementation of RedisConnector based on the Jedis driver.
 */
public class DefaultRedisConnector
	implements RedisConnector
{
	private final static int DEFAULT_POOL_SIZE = 2;

	private RedisConnectorConfig config;
	private Stack<ConnectionMonitor<SwoopBinaryJedisCommands>> available
		= new Stack<ConnectionMonitor<SwoopBinaryJedisCommands>>();

	public DefaultRedisConnector()
	{
		this(new RedisConnectorConfig());
	}

	public DefaultRedisConnector(RedisConnectorConfig config)
	{
		this.config = config.copy();
		for (int i = 0; i < DEFAULT_POOL_SIZE; ++i) {
			available.push(new ConnectionMonitor<SwoopBinaryJedisCommands>(new RedisConnection(this.config)));
		}
	}

	/**
	 * Allows for mocking of Redis connection.
	 */
	public DefaultRedisConnector(Connection<SwoopBinaryJedisCommands> connection)
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
