package com.swoop.data.redis;

public interface RedisConnector
{
	public <T> T execute(RedisCommand<T> command)
		throws java.io.IOException;
}
