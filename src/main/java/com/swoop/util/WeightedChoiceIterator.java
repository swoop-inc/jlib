package com.swoop.util;

/**
 * A data view required by {@link com.swoop.util.Randomizer#select}
 */
public interface WeightedChoiceIterator
{
	public boolean hasNext();
	public double nextWeight();
	public int getCurrentIndex();
}
