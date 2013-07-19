package com.swoop.util.random;

/**
 * Default implementation of RandomNumberGenerator, based on the 
 * Java random number generator.
 * @see java.util.Random
 */
public class DefaultRandomNumberGenerator
	extends java.util.Random
	implements RandomNumberGenerator
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5915246310638751480L;

	public DefaultRandomNumberGenerator()
	{
	}

	public DefaultRandomNumberGenerator(long seed)
	{
		super(seed);
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public void reseed(long seed)
	{
		super.setSeed(seed);
	}
}	  
