package com.swoop.data.redis;

import org.junit.*;
import static org.junit.Assert.*;

public class RedisUriTest
{
	@Test
	public void testDefaultToString() throws Exception
	{
		RedisConnectorConfig redisUri = new RedisConnectorConfig();
		assertEquals("redis://localhost/0", redisUri.toString());
	}

	@Test
	public void testSetPasswordToString() throws Exception
	{
		RedisConnectorConfig redisUri = new RedisConnectorConfig();
		redisUri.setPassword("whatever");
		assertEquals("redis://((password))@localhost/0", redisUri.toString());
	}

	@Test
	public void testSetHostToString() throws Exception
	{
		RedisConnectorConfig redisUri = new RedisConnectorConfig();
		redisUri.setHost("redis.example.com");
		assertEquals("redis://redis.example.com/0", redisUri.toString());
	}

	@Test
	public void testSetHostAndPasswordToString() throws Exception
	{
		RedisConnectorConfig redisUri = new RedisConnectorConfig();
		redisUri.setHost("momma.example.com");
		redisUri.setPassword("top secret");
		assertEquals("redis://((password))@momma.example.com/0", redisUri.toString());
	}

	@Test
	public void testSetDatabaseToString() throws Exception
	{
		RedisConnectorConfig redisUri = new RedisConnectorConfig();
		redisUri.setDatabase(10);
		assertEquals("redis://localhost/10", redisUri.toString());
	}

	@Test
	public void testSetPortToString() throws Exception
	{
		RedisConnectorConfig redisUri = new RedisConnectorConfig();
		redisUri.setPort(9898);
		assertEquals("redis://localhost:9898/0", redisUri.toString());
	}
}
