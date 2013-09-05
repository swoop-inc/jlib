package com.swoop.data.redis;

import static com.swoop.data.redis.RedisUri.*;
import org.junit.*;
import static org.junit.Assert.*;

public class RedisUriTest
{
	@Test
	public void testDefaults() throws Exception
	{
		RedisUri redisUri = new RedisUri();
		assertEquals(DEFAULT_HOST, redisUri.getHost());
		assertEquals(DEFAULT_PORT, redisUri.getPort());
		assertEquals(DEFAULT_DATABASE, redisUri.getDatabase());
		assertNull(redisUri.getPassword());
		assertEquals("redis://localhost/0", redisUri.toString());
	}

	@Test
	public void testHost() throws Exception
	{
		RedisUri redisUri = new RedisUri("//reddy");
		assertEquals("reddy", redisUri.getHost());
		assertEquals(DEFAULT_PORT, redisUri.getPort());
		assertEquals(DEFAULT_DATABASE, redisUri.getDatabase());
		assertNull(redisUri.getPassword());
		assertEquals("redis://reddy/0", redisUri.toString());
	}

	@Test
	public void testHostAndPort() throws Exception
	{
		RedisUri redisUri = new RedisUri("//reddy:9898");
		assertEquals("reddy", redisUri.getHost());
		assertEquals(9898, redisUri.getPort());
		assertEquals(DEFAULT_DATABASE, redisUri.getDatabase());
		assertNull(redisUri.getPassword());
		assertEquals("redis://reddy:9898/0", redisUri.toString());
	}

	@Test
	public void testPasswordAndHost() throws Exception
	{
		RedisUri redisUri = new RedisUri("redis://whatever@localhost/");
		assertEquals("localhost", redisUri.getHost());
		assertEquals(DEFAULT_PORT, redisUri.getPort());
		assertEquals(DEFAULT_DATABASE, redisUri.getDatabase());
		assertEquals("whatever", redisUri.getPassword());
		assertEquals("redis://((password))@localhost/0", redisUri.toString());
	}

	@Test
	public void testDatabase() throws Exception
	{
		RedisUri redisUri = new RedisUri("/10");
		assertEquals(DEFAULT_HOST, redisUri.getHost());
		assertEquals(DEFAULT_PORT, redisUri.getPort());
		assertEquals(10, redisUri.getDatabase());
		assertNull(redisUri.getPassword());
		assertEquals("redis://localhost/10", redisUri.toString());
	}

	@Test
	public void testHostAndDatabase() throws Exception
	{
		RedisUri redisUri = new RedisUri("redis://big.example.com/5");
		assertEquals("big.example.com", redisUri.getHost());
		assertEquals(DEFAULT_PORT, redisUri.getPort());
		assertEquals(5, redisUri.getDatabase());
		assertNull(redisUri.getPassword());
		assertEquals("redis://big.example.com/5", redisUri.toString());
	}
}
