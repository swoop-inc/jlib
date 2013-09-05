package com.swoop.data.redis;

public class RedisTestCase
{
	static final int REDIS_PORT = 6380;   // must match that in ${project-root}/bin/redis_start

	public static RedisConnector createRedisConnector() throws Exception
	{
		RedisConnectorConfig config = new RedisConnectorConfig();
		config.setUri(new RedisUri("redis://localhost:" + REDIS_PORT));
		return new DefaultRedisConnector(config);
	}
}
