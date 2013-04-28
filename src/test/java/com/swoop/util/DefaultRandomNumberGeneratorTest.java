package com.swoop.util;

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
}
