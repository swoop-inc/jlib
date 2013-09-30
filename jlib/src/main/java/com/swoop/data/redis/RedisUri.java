package com.swoop.data.redis;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Identification of a Redis database.  Format: redis://password@host:port/database
 * A RedisUri is immutable.
 */
public class RedisUri 
	implements Cloneable
{
	public final static String SCHEME = "redis";
	public final static String DEFAULT_HOST = "localhost";
	public final static int DEFAULT_PORT = 6379;
	public final static int DEFAULT_DATABASE = 0;

	private String host = DEFAULT_HOST;
	private int port = DEFAULT_PORT;
	private String password;
	private int database = DEFAULT_DATABASE;

	public RedisUri()
	{
	}

	public RedisUri(String uriString)
		throws URISyntaxException
	{
		this(new URI(uriString));
	}

	public RedisUri(URI uri)
		throws IllegalArgumentException
	{
		if (uri.getScheme() != null && !uri.getScheme().equals(SCHEME)) {
			throw new IllegalArgumentException("Redis URI scheme: expected " + SCHEME + ", found " + uri.getScheme());
		}
		if (uri.getHost() != null) {
			this.host = uri.getHost();
		}
		if (uri.getPort() >= 0) {
			this.port = uri.getPort();
		}
		if (uri.getUserInfo() != null) {
			String userInfo = uri.getUserInfo();
			int colon = userInfo.indexOf(':');
			this.password = colon >= 0 ? userInfo.substring(colon + 1) : userInfo;
		}
		if (uri.getPath() != null && !uri.getPath().equals("") && !uri.getPath().equals("/")) {
			if (!uri.getPath().matches("/[\\d]{1,}")) {
				throw new IllegalArgumentException("Redis URI: path must be decimal, found " + uri.getPath());
			}
			try {
				this.database = Integer.parseInt(uri.getPath().substring(1));
			}
			catch (NumberFormatException e) {
				// Should not be reached.
			}
		}
	}

	/**
	 * Return the host, never null.
	 */
	public String getHost()
	{
		return host;
	}

	/**
	 * Return the port number, always valid.
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * Return the password, or null if no password is specified.
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * Return the database number, always valid.
	 */
	public int getDatabase()
	{
		return database;
	}

	/**
	 * For logging and tracing only.  Password is scrubbed.
	 */
	@Override
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

	/**
	 * Create a copy of this object.
	 */
	public RedisUri copy()
	{
		try {
			return (RedisUri) clone();
		}
		catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
}
