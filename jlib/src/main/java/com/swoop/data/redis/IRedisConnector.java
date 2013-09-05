package com.swoop.data.redis;

/**
 * A RedisConnector is capable of connecting to a Redis instance and executing commands.
 */
public interface IRedisConnector
{
	/**
	 * Obtain a Jedis handle and execute the given command through it.
	 * @param command
	 *    a redis command
	 * @return the value returned by the command
	 */
	public <T> T execute(RedisCommand<T> command)
		throws java.io.IOException;

	/**
	 * For error messages.
	 */
	public String toString();
}
