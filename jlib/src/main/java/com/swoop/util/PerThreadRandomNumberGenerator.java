package com.swoop.util;

/**
 * A random number generator that internally assigns a separate RNG instance for use 
 * by each client thread.
 */
public class PerThreadRandomNumberGenerator
	implements RandomNumberGenerator
{
	private RandomNumberGeneratorFactory factory;
	private ThreadLocal<RandomNumberGenerator> threadLocal =
		new ThreadLocal<RandomNumberGenerator>() {
			@Override
			protected RandomNumberGenerator initialValue()
			{
				synchronized (factory) {
					return factory.createRandomNumberGenerator();
				}
			}
		};

	/**
	 * Default constructor.  Creates a {@link DefaultRandomNumberGeneratorFactory}
	 * for internal use.
	 */
	public PerThreadRandomNumberGenerator()
	{
		this(new DefaultRandomNumberGeneratorFactory());
	}

	/**
	 * Constructor.
	 * @param factory  a random number generator factory to use internally to create new
	 *                 random number generators.
	 */
	public PerThreadRandomNumberGenerator(RandomNumberGeneratorFactory factory)
	{
		this.factory = factory;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void reseed(long seed)
	{
		getLocal().reseed(seed);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public int nextInt()
	{
		return getLocal().nextInt();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public long nextLong()
	{
		return getLocal().nextLong();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public double nextDouble()
	{
		return getLocal().nextDouble();
	}

	// Get or create a RNG for use by the current thread.
	private RandomNumberGenerator getLocal()
	{
		return threadLocal.get();
	}
}
