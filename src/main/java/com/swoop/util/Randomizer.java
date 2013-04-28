package com.swoop.util;

public class Randomizer
{
	private RandomNumberGenerator rng;

	/**
	 * Default constructor.  Creates a new random number generator.
	 * Not recommended for production use!
	 */
	public Randomizer()
	{
		this(new DefaultRandomNumberGenerator());
	}

	/**
	 * Constructor.
	 * @param rng   A random number generator to use for all randomization
	 */
	public Randomizer(RandomNumberGenerator rng)
	{
		this.rng = rng;
	}

	/**
	 * Make a selection among weighted choices.
	 * @param weights
	 * @return  the index of the selected item, or -1 if no item was selected
	 */
	public <V> V select(WeightedChoiceIterator<V> choices)
	{
		double runningTotalWeight = 0.0;
		V candidate = null;
		while (choices.hasNext()) {
			double prevTotalWeight = runningTotalWeight;
			double weight = choices.nextWeight();
			if (weight > 0.0) {
				runningTotalWeight += weight;
				if ((rng.nextDouble() * runningTotalWeight) >= prevTotalWeight) {
					candidate = choices.getCurrentValue();
				}
			}
		}
		return candidate;
	}
}
