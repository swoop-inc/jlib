package com.swoop.data.redis;

import redis.clients.jedis.exceptions.JedisException;
import java.io.IOException;

public interface RedisCommand<T>
{
	public T execute(SwoopBinaryJedisCommands jedis)
		throws JedisException, IOException;
}
