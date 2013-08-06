package com.swoop.util.random;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * General implementation of RandomNumberGeneratorFactory.  Works with any
 * RandomNumberGenerator implementation class, provided that it has a default
 * constructor.  Uses a DefaultRandomNumberGenerator to seed each new instance.
 */
public class GeneralRandomNumberGeneratorFactory<T extends RandomNumberGenerator>
	extends DefaultRandomNumberGenerator
	implements RandomNumberGeneratorFactory
{
	private Constructor<T> constructor;

	/**
	 * Constructor.
	 * @param rngClass   A random number generator class, having a default constructor
	 */
	public GeneralRandomNumberGeneratorFactory(Class<T> rngClass)
		throws NoSuchMethodException
	{
		this(rngClass, System.currentTimeMillis());
	}

	/**
	 * Constructor.
	 * @param rngClass   A random number generator class, having a default constructor
	 * @param seed       A seed for the randomization of seeds for the RNGs produced by this factory.
	 */
	public GeneralRandomNumberGeneratorFactory(Class<T> rngClass, long seed)
		throws NoSuchMethodException
	{
		super(seed);
		this.constructor = rngClass.getConstructor();
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public RandomNumberGenerator createRandomNumberGenerator()
	{
		try {
			RandomNumberGenerator newInstance = constructor.newInstance();
			newInstance.reseed(nextLong());
			return newInstance;
		}
		catch (InstantiationException e) {
			throw new RuntimeException(e);   // should not be reached.
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);   // should not be reached.
		}
		catch (IllegalArgumentException e) {
			throw new RuntimeException(e);   // should not be reached.
		}
		catch (InvocationTargetException e) {
			throw new RuntimeException(e);   // should not be reached.
		}
	}
}
