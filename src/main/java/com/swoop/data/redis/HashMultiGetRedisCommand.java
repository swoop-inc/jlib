package com.swoop.data.redis;

import java.io.IOException;
import java.util.List;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Provide template method for a Redis HMGET command.
 */
abstract public class HashMultiGetRedisCommand<T>
	implements RedisCommand<T>
{
	@Override
	public T execute(BinaryJedisCommands jedis)
		throws JedisException, IOException
	{
		return postprocess(jedis.hmget(getBinaryKey(), getBinaryFields()));	
	}

	abstract protected byte[] getBinaryKey()
		throws IOException;

	abstract protected byte[][] getBinaryFields()
		throws IOException;

	abstract protected T postprocess(List<byte[]> data)
		throws IOException;
}
