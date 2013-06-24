package com.swoop.data.redis;

import java.io.IOException;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Provide template method for a Redis HSET command.
 */
abstract public class HashSetRedisCommand
	implements RedisCommand<Boolean>
{
	private final static Long CREATED = new Long(1);

	@Override
	public Boolean execute(BinaryJedisCommands jedis)
		throws JedisException, IOException
	{
		return CREATED.equals(jedis.hset(getBinaryKey(), getBinaryField(), getBinaryValue()));
	}

	abstract protected byte[] getBinaryKey()
		throws IOException;

	abstract protected byte[] getBinaryField()
		throws IOException;

	abstract protected byte[] getBinaryValue()
		throws IOException;
}
