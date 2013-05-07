package com.swoop.util;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class RandomizerTest
{
	@Test
	public void testSelectAmongNone() throws Exception
	{
		assertNull(new Randomizer().select(new WeightedChoiceAdapter(new double[0])));
	}

	@Test
	public void testSelectAmongZero() throws Exception
	{
		assertNull(new Randomizer().select(new WeightedChoiceAdapter(new double[] { 0 })));
	}

	@Test
	public void testSelectAmongZeroes() throws Exception
	{
		assertNull(new Randomizer().select(new WeightedChoiceAdapter(new double[] { 0, 0 })));
	}

	@Test
	public void testSelectAmongOne() throws Exception
	{
		assertEquals(0, new Randomizer().select(new WeightedChoiceAdapter(new double[] { 10 })).intValue());
	}

	@Test
	public void testSelectAmongMany00() throws Exception
	{
		Randomizer rand = new Randomizer(new RiggedRandomNumberGenerator(0));
		assertEquals(0, rand.select(new WeightedChoiceAdapter(new double[] {
			1.0, 2.0, 4.0, 8.0, 16.0
		})).intValue());
	}

	@Test
	public void testSelectAmongMany05() throws Exception
	{
		Randomizer rand = new Randomizer(new RiggedRandomNumberGenerator(0.5));
		assertEquals(3, rand.select(new WeightedChoiceAdapter(new double[] {
			1.0, 2.0, 4.0, 8.0, 14.0
		})).intValue());
	}

	@Test
	public void testSelectAmongMany10() throws Exception
	{
		Randomizer rand = new Randomizer(new RiggedRandomNumberGenerator(1.0));
		assertEquals(4, rand.select(new WeightedChoiceAdapter(new double[] {
			1.0, 2.0, 4.0, 8.0, 14.0
		})).intValue());
	}

	@Test
	public void testRandomnessOfSelection() throws Exception
	{
		double[] weights = { 1.0, 4.0, 16.0, 64.0, 256.0 };
		boolean[] covered = new boolean[weights.length];
		int ncovered = 0;
		Randomizer rand = new Randomizer();
		for (int trials = 0; trials < 1000000; ++trials) {
			int sel = rand.select(new WeightedChoiceAdapter(weights));
			if (!covered[sel]) {
				covered[sel] = true;
				ncovered += 1;
				if (ncovered == weights.length) {
					return;
				}
			}
		}
		fail("should not be reached");
	}

	private static class WeightedChoiceAdapter
		implements WeightedChoiceIterator<Integer>
	{
		double[] weights;
		int index;

		WeightedChoiceAdapter(double[] weights)
		{
			this.weights = weights;
		}

		@Override
		public boolean hasNext()
		{
			return index < weights.length;
		}

		@Override
		public double nextWeight()
		{
			return weights[index++];
		}

		@Override
		public Integer getCurrentValue()
		{
			return index - 1;
		}
	}
}
