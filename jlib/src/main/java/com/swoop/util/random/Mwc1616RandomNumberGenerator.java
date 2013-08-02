package com.swoop.util.random;

/**
 * A simple, fast random number generator for low-precision applications only.  Implementation is based on dual 16-bit
 * multiply-with-carry generators.
 *
 * Reference: <a href="http://www.helsbreth.org/random/rng_mwc1616.html">http://www.helsbreth.org/random/rng_mwc1616.html</a>
 */
public class Mwc1616RandomNumberGenerator
	extends AbstractRandomNumberGenerator
{
	private final static int MA = 18000;
	private final static int MB = 30903;

	private int stateA = 1;
	private int stateB = 2;

	/**
	 * @inheritDoc
	 */
	public void reseed(long seed)
	{
		this.stateA = (int)(seed >> 32) | 1;   // do not seed with 0
		this.stateB = (int)seed | 2;
	}

	/**
	 * @inheritDoc
	 */
	public int nextInt()
	{
		stateA = ((stateA & 0xffff) * MA) + (stateA >>> 16);
		stateB = ((stateB & 0xffff) * MB) + (stateB >>> 16);
		return (stateA << 16) + (stateB & 0xffff);
	}
}
