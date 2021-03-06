package com.swoop.data.redis;

import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.JedisShardInfo;

public class SwoopBinaryJedis extends BinaryJedis implements SwoopBinaryJedisCommands
{
	public SwoopBinaryJedis(JedisShardInfo shardInfo)
	{
		super(shardInfo);
	}

	public SwoopBinaryJedis(String host, int port, int timeout)
	{
		super(host, port, timeout);
	}

	public SwoopBinaryJedis(String host, int port)
	{
		super(host, port);
	}

	public SwoopBinaryJedis(String host)
	{
		super(host);
	}

	/**
	 * Strangely absent from 2.1.0.
	 */
	public Long del(byte[] key)
	{
		checkIsInMulti();
		getClient().del(key);
		return getClient().getIntegerReply();
	}
}
