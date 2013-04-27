package com.swoop.util;

public class RiggedRandomNumberGenerator
	implements RandomNumberGenerator
{
	private double value;

	public RiggedRandomNumberGenerator(double value)
	{
		this.value = value;
	}
	
	@Override
	public void reseed(long seed)
	{
	}

	@Override
	public int nextInt()
	{
		return (int)(value * Integer.MAX_VALUE);
	}

	@Override
	public double nextDouble()
	{
		return value;
	}
}	  
