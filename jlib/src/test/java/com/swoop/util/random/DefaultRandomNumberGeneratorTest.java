package com.swoop.util.random;

import org.junit.*;
import static org.junit.Assert.*;

public class DefaultRandomNumberGeneratorTest
{
	@Test
	public void testDefaultConstructor() throws Exception
	{
		RandomNumberGenerator rng = new DefaultRandomNumberGenerator();
		rng.nextInt();
		rng.nextDouble();
		rng.reseed(1000000000L);
		int i = rng.nextInt();
		rng.reseed(1000000000L);
		int j = rng.nextInt();
		assertEquals(i, j);
	}

	@Test
	public void testSeedingConstructor() throws Exception
	{
		RandomNumberGenerator rng1 = new DefaultRandomNumberGenerator(123456789L);
		RandomNumberGenerator rng2 = new DefaultRandomNumberGenerator(123456789L);
		assertEquals(rng1.nextInt(), rng2.nextInt());
	}

	@Test
	public void testRangeOfNextDouble() throws Exception
	{
		RandomNumberGenerator rng = new DefaultRandomNumberGenerator();
		for (int i = 0; i < 1000; ++i) {
			assertTrue(rng.nextDouble() >= 0.0);
			assertTrue(rng.nextDouble() < 1.0);
		}
	}
}
