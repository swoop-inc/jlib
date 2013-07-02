package com.swoop.data.redis;

import redis.clients.jedis.exceptions.JedisException;
import java.io.IOException;
import java.util.List;

/**
 * Provide template method for a Redis MGET command.
 */
abstract public class MultiGetRedisCommand<T> implements RedisCommand<T>
{

	@Override
	public T execute(SwoopBinaryJedisCommands jedis) throws JedisException, IOException
	{
		return postprocess(jedis.mget(getBinaryKeys()));
	}

	abstract protected byte[][] getBinaryKeys() throws IOException;

	abstract protected T postprocess(List<byte[]> data) throws IOException;
}
