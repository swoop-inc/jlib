package com.swoop.data.redis;

/**
 * A RedisClient has a RedisConnector and knows how to use it.
 */
public class RedisClient
{
	private RedisConnector connector;

	/**
	 * Constructor.
	 * @param configurator  
	 *     a connector to external configuration data
	 * @param configurationKey
	 *     the key of this item's configuration, to be resolved by the configurator
	 */
	public RedisClient(RedisConfigurator configurator, String configurationKey)
		throws java.io.IOException
	{
		this(configurator.createConnector(configurationKey));
	}

	/**
	 * Constructor.
	 * @param connector   the connector for execution
	 */
	public RedisClient(RedisConnector connector)
	{
		this.connector = connector;
	}

	/**
	 * RedisClient is meant to be extended.
	 */
	protected <T> T execute(RedisCommand<T> command)
		throws java.io.IOException
	{
		return connector.execute(command);
	}

	/**
	 * For error messages.
	 */
	@Override
	public String toString()
	{
		return connector.toString();
	}
}
