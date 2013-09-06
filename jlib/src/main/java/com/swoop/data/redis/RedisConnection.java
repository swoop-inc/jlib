package com.swoop.data.redis;

import com.swoop.data.util.Connection;

import redis.clients.jedis.exceptions.JedisException;
import java.io.IOException;

/**
 * Implementation of RedisConnector based on the Jedis driver.
 */
public class RedisConnection
	implements Connection<SwoopBinaryJedisCommands>
{
	private RedisConnectorConfig config;
	private SwoopBinaryJedis jedis;

	public RedisConnection()
	{
		this.config = new RedisConnectorConfig();
	}

	public RedisConnection(RedisConnectorConfig config)
	{
		this.config = config.copy();
	}

	@Override
	public SwoopBinaryJedisCommands open()
		throws IOException
	{
		try {
			if (!isOpen()) {
				jedis = new SwoopBinaryJedis(config.getUri().getHost(), config.getUri().getPort(), config.getTimeoutMillis());
				// select() implicitly connects.  Set authentication info first.
				if (config.getUri().getPassword() != null) {
					jedis.auth(config.getUri().getPassword());
				}
				jedis.select(config.getUri().getDatabase());
				jedis.connect();
			}
			return jedis;
		}
		catch (JedisException e) {
			throw new IOException(config + ": connection error", e);
		}
	}

	@Override
	public boolean isOpen()
	{
		return jedis != null;
	}

	@Override
	public void close()
	{
		if (isOpen()) {
			jedis.disconnect();
			jedis = null;
		}
	}

	@Override
	public String toString()
	{
		return config.toString();
	}
}
