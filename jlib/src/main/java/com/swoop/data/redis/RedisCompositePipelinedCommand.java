package com.swoop.data.redis;

import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisException;
import java.io.IOException;

/**
 * Pipelined composite command
 * 
 * On its execute method it creates a pipeline and enqueues each of the RedisPipelinedCommand
 * Then syncs for Redis to get all results
 * 
 * Finally response values must be asked to each RedisPipelinedCommand, on execute return.
 */
public class RedisCompositePipelinedCommand implements RedisCommand<Object>
{
	private final RedisPipelinedCommand<Object>[] pipelinedCommands;
	
	@SafeVarargs
	public RedisCompositePipelinedCommand(RedisPipelinedCommand<Object>...pipelinedCommands)
	{
		super();
		this.pipelinedCommands = pipelinedCommands;
	}
	
	@Override
	public Object execute(SwoopBinaryJedisCommands jedis) throws JedisException, IOException
	{
		Pipeline pipeline = jedis.pipelined();
		for (RedisPipelinedCommand<Object> pipelinedCommand : pipelinedCommands) {
			pipelinedCommand.enqueueCommand(pipeline);
		}
		pipeline.sync();
		return null;
	}
}
