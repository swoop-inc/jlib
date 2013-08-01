package com.swoop.util.random;

import java.util.Random;

public class RandomNumberGeneratorWrapper implements RandomNumberGenerator
{
	private final Random random;
	
	public RandomNumberGeneratorWrapper(Random random)
	{
		super();
		this.random = random;
	}
	
	@Override
	public void reseed(long seed)
	{
		random.setSeed(seed);
	}

	@Override
	public int nextInt()
	{
		return random.nextInt();
	}

	@Override
	public long nextLong()
	{
		return random.nextLong();
	}

	@Override
	public double nextDouble()
	{
		return random.nextDouble();
	}
}
