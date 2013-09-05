package com.swoop.data.redis;

/**
 * Information required to maintain a connection to a Redis server.
 */
public class RedisConnectorConfig 
	implements Cloneable
{
	public final static RedisUri DEFAULT_URI = new RedisUri();
	public final static int DEFAULT_TIMEOUT_MILLIS = 3000;
	public final static int DEFAULT_CONCURRENCY = 3;

	private RedisUri uri = DEFAULT_URI;
	private int timeoutMillis = DEFAULT_TIMEOUT_MILLIS;
	private int concurrency = DEFAULT_CONCURRENCY;

	public void setUri(RedisUri uri)
	{
		this.uri = uri;
	}

	public RedisUri getUri()
	{
		return uri;
	}

	public void setTimeoutMillis(int timeoutMillis)
	{
		this.timeoutMillis = timeoutMillis;
	}

	public int getTimeoutMillis()
	{
		return timeoutMillis;
	}

	public void setConcurrency(int concurrency)
	{
		this.concurrency = concurrency;
	}

	public int getConcurrency()
	{
		return concurrency;
	}

	@Override
	public String toString()
	{
		return uri.toString();
	}

	public RedisConnectorConfig copy()
	{
		try {
			return (RedisConnectorConfig) clone();
		}
		catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
}
