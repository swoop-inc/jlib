package com.swoop.util;

/**
 * A data view required by {@link com.swoop.util.Randomizer#select}
 */
public interface WeightedChoiceIterator<V>
{
	public boolean hasNext();
	public double nextWeight();
	public V getCurrentValue();
}
