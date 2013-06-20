package com.swoop.data.redis;

import java.io.IOException;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Provide template method for a Redis HGET command.
 */
abstract public class HashGetRedisCommand<T>
	implements RedisCommand<T>
{
	@Override
	public T execute(BinaryJedisCommands jedis)
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
