package com.swoop.util.random;

public class RiggedRandomNumberGenerator
	implements RandomNumberGenerator
{
	private double[] values;
	private int index;

	public RiggedRandomNumberGenerator(double value)
	{
		this.values = new double[] { value };
	}

	public RiggedRandomNumberGenerator(double[] values)
	{
		this.values = values;
	}
	
	@Override
	public void reseed(long seed)
	{
	}

	@Override
	public int nextInt()
	{
		return (int)(nextDouble() * Integer.MAX_VALUE);
	}

	@Override
	public long nextLong()
	{
		return (nextInt() << 32L) + nextInt();
	}

	@Override
	public double nextDouble()
	{
		double result = values[index];
		index = (index + 1) % values.length;
		return result;
	}
}	  
