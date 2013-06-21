package com.swoop.data.redis;

/**
 * A RedisConfigurator for mocking/testing and simple deployments.
 */
public class DefaultRedisConfigurator
	implements RedisConfigurator
{
	/**
	 * @inheritDoc
	 */
	public RedisConnector createConnector(String configurationKey)
	{
		return new DefaultRedisConnector(new RedisConnectorConfig());
	}
}
