package com.swoop.data.redis;

/**
 * A RedisConnector that proxies to a second RedisConnector.
 */
public class ProxyRedisConnector
	implements IRedisConnector
{
	private IRedisConnector inner;

	/**
	 * Constructor.
	 * @param connector   the connector for execution
	 */
	public ProxyRedisConnector(IRedisConnector inner)
	{
		this.inner = inner;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public <T> T execute(RedisCommand<T> command)
		throws java.io.IOException
	{
		return inner.execute(command);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String toString()
	{
		return inner.toString();
	}
}
