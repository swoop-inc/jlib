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
		return jedis.mset(getBinaryKeysAndValues());
	}
	
	/**
	 * byte[] of mixed keys and values, even position keys and odd position values.
	 * 
	 * @return
	 * @throws IOException
	 */
	abstract protected byte[][] getBinaryKeysAndValues() throws IOException;
}
