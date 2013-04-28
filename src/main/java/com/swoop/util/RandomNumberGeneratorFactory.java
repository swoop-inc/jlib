package com.swoop.util;

/**
 * Factory interface for {@link com.swoop.util.RandomNumberGenerator}.
 */
public interface RandomNumberGeneratorFactory
{
	/**
	 * Create a new random number generator, randomly seeded.
	 */
	public RandomNumberGenerator createRandomNumberGenerator();
}
