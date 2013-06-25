package com.swoop.data.redis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Provide template method for a Redis HMSET command.
 */
abstract public class HashMultiSetRedisCommand
	implements RedisCommand<String>
{
	@Override
	public String execute(BinaryJedisCommands jedis)
		throws JedisException, IOException
	{
		return jedis.hmset(getBinaryKey(), getBinaryFieldValueMap());
	}

	abstract protected byte[] getBinaryKey()
		throws IOException;

	abstract protected Map<byte[],byte[]> getBinaryFieldValueMap()
		throws IOException;
}
