package com.swoop.util.random;

import org.junit.*;
import static org.junit.Assert.*;

public class Mwc1616RandomNumberGeneratorTest
{
	@Test
	public void testRandomization() throws Exception
	{
		RandomNumberGenerator rng = new Mwc1616RandomNumberGenerator();
		long i1 = rng.nextLong();
		assertNotEquals(i1, rng.nextLong());   // fails once in a zillion years.
	}

	@Test
	public void testReseed() throws Exception
	{
		RandomNumberGenerator rng = new Mwc1616RandomNumberGenerator();
		rng.nextInt();
		rng.nextDouble();
		rng.reseed(1000000000L);
		int i = rng.nextInt();
		rng.reseed(1000000000L);
		int j = rng.nextInt();
		assertEquals(i, j);
	}

	@Test
	public void testRangeOfNextDouble() throws Exception
	{
		RandomNumberGenerator rng = new Mwc1616RandomNumberGenerator();
		for (int i = 0; i < 1000; ++i) {
			assertTrue(rng.nextDouble() >= 0.0);
			assertTrue(rng.nextDouble() < 1.0);
		}
	}

	@Test
	public void testFactory() throws Exception
	{
		GeneralRandomNumberGeneratorFactory factory = new GeneralRandomNumberGeneratorFactory(Mwc1616RandomNumberGenerator.class);
		RandomNumberGenerator rng = factory.createRandomNumberGenerator();
		assertTrue(rng instanceof Mwc1616RandomNumberGenerator);
	}
}
