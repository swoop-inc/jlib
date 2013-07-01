package com.swoop.data.redis;

import redis.clients.jedis.exceptions.JedisException;
import java.io.IOException;

/**
 * Provide template method for a Redis MSET command.
 */
abstract public class MultiSetRedisCommand implements RedisCommand<String>
{
	@Override
	public String execute(SwoopBinaryJedisCommands jedis) throws JedisException, IOException
	{
		
		return jedis.mset(getBinaryKeyValues());
	}

	abstract protected byte[] getBinaryKeyValues() throws IOException;
}
