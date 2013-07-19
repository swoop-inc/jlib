package com.swoop.util.random;

/**
 * Default implementation of RandomNumberGeneratorFactory, based on 
 * DefaultRandomNumberGenerator.
 */
public class DefaultRandomNumberGeneratorFactory
	extends DefaultRandomNumberGenerator
	implements RandomNumberGeneratorFactory
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1950436770666356706L;

	public DefaultRandomNumberGeneratorFactory()
	{
	}

	public DefaultRandomNumberGeneratorFactory(long seed)
	{
		super(seed);
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public RandomNumberGenerator createRandomNumberGenerator()
	{
		return new DefaultRandomNumberGenerator(nextLong());
	}
}
