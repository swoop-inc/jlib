package com.swoop.data.redis;

/**
 * Information required to maintain a connection to a Redis server.
 */
public class RedisConnectorConfig 
	implements Cloneable
{
	public final static String DEFAULT_HOST = "localhost";
	public final static int DEFAULT_PORT = 6379;
	public final static int DEFAULT_DATABASE = 0;
	public final static int DEFAULT_TIMEOUT_MILLIS = 3000;

	private String host = DEFAULT_HOST;
	private int port = DEFAULT_PORT;
	private String password;
	private int database = DEFAULT_DATABASE;
	private int timeoutMillis = DEFAULT_TIMEOUT_MILLIS;

	public void setHost(String host)
	{
		this.host = host;
	}

	public String getHost()
	{
		return host;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public int getPort()
	{
		return port;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getPassword()
	{
		return password;
	}

	public void setDatabase(int database)
	{
		this.database = database;
	}

	public int getDatabase()
	{
		return database;
	}

	public void setTimeoutMillis(int timeoutMillis)
	{
		this.timeoutMillis = timeoutMillis;
	}

	public int getTimeoutMillis()
	{
		return timeoutMillis;
	}

	public String toString()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("redis://");
		// Caution: Do not display password!
		if (getPassword() != null) {
			buf.append("((password))@");
		}
		buf.append(getHost());
		if (getPort() != DEFAULT_PORT) {
			buf.append(":");
			buf.append(getPort());
		}
		buf.append("/");
		buf.append(getDatabase());
		return buf.toString();
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
