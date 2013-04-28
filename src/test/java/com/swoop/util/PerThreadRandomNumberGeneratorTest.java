package com.swoop.util;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class PerThreadRandomNumberGeneratorTest
{
	RandomNumberGeneratorFactory myFactory = new RandomNumberGeneratorFactory() {
		@Override
		public RandomNumberGenerator createRandomNumberGenerator()
		{
			return new AbstractRandomNumberGenerator() {
				int count = 0;
				@Override
				public void reseed(long seed) {
					count = 0;
				}
				@Override
				public int nextInt() {
					return count++;
				}
			};
		}
	};

	RandomNumberGenerator ptRNG = new PerThreadRandomNumberGenerator(myFactory);

	@Test
	public void testPerThread() throws Exception
	{
		final int N = 10;
		final ExecGate gate = new ExecGate(N);
		for (int i = 0; i < N; ++i) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					assertEquals(1L, ptRNG.nextLong());
					assertEquals((2L << 32) + 3L, ptRNG.nextLong());
					assertEquals((4L << 32) + 5L, ptRNG.nextLong());
					assertEquals((6L << 32) + 7L, ptRNG.nextLong());
					gate.unlock();
				}
			}).start();
		}
		gate.cross();
	}
}
