package com.swoop.util.random;

/**
 * A base class for a RNG implementation.  Implements nextLong() and nextDouble() in terms of nextInt().
 * Subclasses may provide just a 32-bit randomization.
 */
abstract public class AbstractRandomNumberGenerator
	implements RandomNumberGenerator
{
	/**
	 * @inheritDoc
	 */
	@Override
	abstract public void reseed(long seed);

	/**
	 * @inheritDoc
	 */
	@Override
	abstract public int nextInt();

	/**
	 * @inheritDoc
	 */
	public long nextLong()
	{
		// it's okay that the bottom word remains signed.
		return ((long)(nextInt()) << 32) + nextInt();
	}

	/**
	 * @inheritDoc
	 */
	public double nextDouble()
	{
		long l = ((long)(nextInt() & 0x3ffffff) << 27) + (nextInt() & 0x7ffffff);
		return l / (double)(1L << 53);
	}
}
