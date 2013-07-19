package com.swoop.util.random;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class RandomizerTest
{
	@Test
	public void testSelectOneAmongNone() throws Exception
	{
		assertNull(new Randomizer().select(new WeightedChoiceAdapter(new double[0])));
	}

	@Test
	public void testSelectOneAmongZero() throws Exception
	{
		assertNull(new Randomizer().select(new WeightedChoiceAdapter(new double[] { 0 })));
	}

	@Test
	public void testSelectOneAmongZeroes() throws Exception
	{
		assertNull(new Randomizer().select(new WeightedChoiceAdapter(new double[] { 0, 0 })));
	}

	@Test
	public void testSelectOneOfOne() throws Exception
	{
		assertEquals(0, new Randomizer().select(new WeightedChoiceAdapter(new double[] { 10 })).intValue());
	}

	@Test
	public void testSelectOneAmongMany00() throws Exception
	{
		Randomizer rand = new Randomizer(new RiggedRandomNumberGenerator(0));
		assertEquals(0, rand.select(new WeightedChoiceAdapter(new double[] {
			1.0, 2.0, 4.0, 8.0, 16.0
		})).intValue());
	}

	@Test
	public void testSelectOneAmongMany05() throws Exception
	{
		Randomizer rand = new Randomizer(new RiggedRandomNumberGenerator(0.5));
		assertEquals(3, rand.select(new WeightedChoiceAdapter(new double[] {
			1.0, 2.0, 4.0, 8.0, 14.0
		})).intValue());
	}

	@Test
	public void testSelectOneAmongMany10() throws Exception
	{
		Randomizer rand = new Randomizer(new RiggedRandomNumberGenerator(1.0));
		assertEquals(4, rand.select(new WeightedChoiceAdapter(new double[] {
			1.0, 2.0, 4.0, 8.0, 14.0
		})).intValue());
	}

	@Test
	public void testRandomnessOfSingleSelection() throws Exception
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

	@Test
	public void testSelectSomeAmongNone() throws Exception
	{
		List<Integer> result = new Randomizer().select(new WeightedChoiceAdapter(new double[0]), 5);
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	@Test
	public void testSelectSomeAmongZero() throws Exception
	{
		List<Integer> result = new Randomizer().select(new WeightedChoiceAdapter(new double[] { 0 }), 5);
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	@Test
	public void testSelectSomeAmongZeroes() throws Exception
	{
		List<Integer> result = new Randomizer().select(new WeightedChoiceAdapter(new double[] { 0, 0 }), 5);
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	@Test
	public void testTakeWhatYouCanGet() throws Exception
	{
		List<Integer> result = new Randomizer().select(new WeightedChoiceAdapter(new double[] { 10 }), 5);
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	public void testTakeWhatYouCanGet2() throws Exception
	{
		List<Integer> result = new Randomizer().select(new WeightedChoiceAdapter(new double[] { 5, 10 }), 5);
		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.get(0).intValue() != result.get(1).intValue());
	}

	@Test
	public void testRandomnessOfMultiSelection() throws Exception
	{
		final double[] weights = { 1.0, 2.0, 4.0, 8.0, 16.0, 32.0, 64.0 };
		final int NVALUES = (7*6*5)/(1*2*3); // C(7,3)
		final boolean[] covered = new boolean[16 + 32 + 64 + 1];
		int ncovered = 0;
		Randomizer rand = new Randomizer();
		for (int trials = 0; trials < 100000000; ++trials) {
			int sel = 0;
			for (int s : rand.select(new WeightedChoiceAdapter(weights), 3)) {
				sel += 1<<s;
			}
			if (!covered[sel]) {
				covered[sel] = true;
				ncovered += 1;
				if (ncovered == NVALUES) {
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
