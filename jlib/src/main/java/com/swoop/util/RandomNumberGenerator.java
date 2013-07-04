package com.swoop.util;

/**
 * RandomNumberGenerator is an interface for a general purpose random
 * number generator.
 * @see java.util.Random
 */
public interface RandomNumberGenerator
{
	/**
	 * Reseed this random number generator.
	 * @param	seed   a seed value
	 */
	public void reseed(long seed);

	/**
	 * Returns the next pseudorandom, uniformly distributed <code>int</code>
	 * value from this random number generator's sequence.
	 *
	 * @return	the next pseudorandom, uniformly distributed <code>int</code>
	 *			value from this random number generator's sequence.
	 */
	public int nextInt();

	/**
	 * Returns the next pseudorandom, uniformly distributed <code>long</code>
	 * value from this random number generator's sequence.
	 *
	 * @return	the next pseudorandom, uniformly distributed <code>long</code>
	 *			value from this random number generator's sequence.
	 */
	public long nextLong();

	/**
	 * Returns the next pseudorandom, uniformly distributed 
	 * <code>double</code> value between <code>0.0</code> and
	 * <code>1.0</code> from this random number generator's sequence.
	 *
	 * @return	the next pseudorandom, uniformly distributed 
	 *			<code>double</code> value between <code>0.0</code> and
	 *			<code>1.0</code> from this random number generator's sequence.
	 */
	public double nextDouble();
}	  
