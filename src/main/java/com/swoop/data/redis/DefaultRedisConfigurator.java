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
		//jedisConfig.setHost("ec2-67-202-17-40.compute-1.amazonaws.com");
		//jedisConfig.setPort(7963);
		//jedisConfig.setPassword("49893ac6568dc6c37691e42ba1f16d9f");
		return new DefaultRedisConnector(new RedisConnectorConfig());
	}
}
