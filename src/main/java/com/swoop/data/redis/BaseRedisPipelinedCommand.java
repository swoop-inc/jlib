package com.swoop.data.redis;

import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.exceptions.JedisException;
import java.io.IOException;

public abstract class BaseRedisPipelinedCommand<T> implements RedisPipelinedCommand<T>
{
	private Response<T> response;
	
	@Override
	public void enqueueCommand(Pipeline pipeline) throws JedisException, IOException
	{
		response = customEnqueueCommand(pipeline);
	}

	protected abstract Response<T> customEnqueueCommand(Pipeline pipeline);
	
	@Override
	public T getResult()
	{
		return response.get();
	}
}
