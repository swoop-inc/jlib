package com.swoop.data.redis;

import java.io.IOException;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.exceptions.JedisException;

public interface RedisCommand<T>
{
	public T execute(BinaryJedisCommands jedis)
		throws JedisException, IOException;
}
