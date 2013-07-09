package com.swoop.data.redis;

import redis.clients.jedis.exceptions.JedisException;
import java.io.IOException;

/**
 * Provide template method for a Redis HGET command.
 */
abstract public class HashGetRedisCommand<T>
	implements RedisCommand<T>
{
	@Override
	public T execute(SwoopBinaryJedisCommands jedis)
		throws JedisException, IOException
	{
		return postprocess(jedis.hget(getBinaryKey(), getBinaryField()));	
	}

	abstract protected byte[] getBinaryKey()
		throws IOException;

	abstract protected byte[] getBinaryField()
		throws IOException;

	abstract protected T postprocess(byte[] data)
		throws IOException;
}
