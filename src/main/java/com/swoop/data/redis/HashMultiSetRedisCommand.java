package com.swoop.data.redis;

import redis.clients.jedis.exceptions.JedisException;
import java.io.IOException;
import java.util.Map;

/**
 * Provide template method for a Redis HMSET command.
 */
abstract public class HashMultiSetRedisCommand
	implements RedisCommand<String>
{
	@Override
	public String execute(SwoopBinaryJedisCommands jedis)
		throws JedisException, IOException
	{
		return jedis.hmset(getBinaryKey(), getBinaryFieldValueMap());
	}

	abstract protected byte[] getBinaryKey()
		throws IOException;

	abstract protected Map<byte[],byte[]> getBinaryFieldValueMap()
		throws IOException;
}
