package com.swoop.data.redis;

import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisException;
import java.io.IOException;

public interface RedisPipelinedCommand<T>
{
	void enqueueCommand(Pipeline pipeline)
			throws JedisException, IOException;
	
	T getResult();
}
