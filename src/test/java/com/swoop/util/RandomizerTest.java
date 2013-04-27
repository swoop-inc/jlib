package com.swoop.util;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class RandomizerTest
{
	@Test
	public void testSelectAmongNone() throws Exception
	{
		Randomizer rand = new Randomizer(new DefaultRandomNumberGenerator());
		assertEquals(-1, rand.select(new ArrayList<Double>()));
	}

	@Test
	public void testSelectAmongZero() throws Exception
	{
		Randomizer rand = new Randomizer(new DefaultRandomNumberGenerator());
		List<Double> list = Arrays.asList(new Double[] { new Double(0) });
		assertEquals(-1, rand.select(list));
	}

	@Test
	public void testSelectAmongZeroes() throws Exception
	{
		Randomizer rand = new Randomizer(new DefaultRandomNumberGenerator());
		List<Double> list = Arrays.asList(new Double[] { new Double(0), new Double(0) });
		assertEquals(-1, rand.select(list));
	}

	@Test
	public void testSelectAmongOne() throws Exception
	{
		Randomizer rand = new Randomizer(new DefaultRandomNumberGenerator());
		List<Double> list = Arrays.asList(new Double[] { new Double(10) });
		assertEquals(0, rand.select(list));
	}

	@Test
	public void testSelectAmongMany00() throws Exception
	{
		Randomizer rand = new Randomizer(new RiggedRandomNumberGenerator(0));
		List<Double> list = Arrays.asList(new Double[] {
			new Double(1), new Double(2), new Double(4), new Double(8), new Double(16)
		});
		assertEquals(0, rand.select(list));
	}

	@Test
	public void testSelectAmongMany05() throws Exception
	{
		Randomizer rand = new Randomizer(new RiggedRandomNumberGenerator(0.5));
		List<Double> list = Arrays.asList(new Double[] {
			new Double(1), new Double(2), new Double(4), new Double(8), new Double(14)
		});
		assertEquals(3, rand.select(list));
	}

	@Test
	public void testSelectAmongMany10() throws Exception
	{
		Randomizer rand = new Randomizer(new RiggedRandomNumberGenerator(1.0));
		List<Double> list = Arrays.asList(new Double[] {
			new Double(1), new Double(2), new Double(4), new Double(8), new Double(14)
		});
		assertEquals(4, rand.select(list));
	}
}
