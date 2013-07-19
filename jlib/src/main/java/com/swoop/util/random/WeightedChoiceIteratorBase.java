package com.swoop.util.random;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public abstract class WeightedChoiceIteratorBase<V>
	implements WeightedChoiceIterator<V>
{
	private Iterable<V> choices;
	private Iterator<V> iterator;
	private V current;

	/**
	 * Constructor.
	 */
	public WeightedChoiceIteratorBase()
	{
		this((Iterable<V>)Collections.EMPTY_LIST);
	}

	/**
	 * Constructor.
	 * @param choices   the initial list of choices, expressed as an array
	 */
	public WeightedChoiceIteratorBase(V[] choices)
	{
		reset(choices);
	}

	/**
	 * Constructor.
	 * @param choices   the initial list of choices, expressed as an iterable collection
	 */
	public WeightedChoiceIteratorBase(Iterable<V> choices)
	{
		reset(choices);
	}

	/**
	 * Reset to the beginning of a new list of choices.
	 * @param choices   the list of choices, expressed as an array
	 */
	public void reset(V[] choices)
	{
		reset(Arrays.asList(choices));
	}

	/**
	 * Reset to the beginning of a new list of choices.
	 * @param choices   the list of choices, expressed as an iterable collection
	 */
	public void reset(Iterable<V> choices)
	{
		this.choices = choices;
		reset();
	}

	/**
	 * Reset to the beginning of the current list of choices.
	 */
	public void reset()
	{
		this.iterator = choices.iterator();
		this.current = null;
	}

	@Override
	public boolean hasNext()
	{
		return iterator.hasNext();
	}

	@Override
	public double nextWeight()
	{
		return getWeight(current = iterator.next());
	}

	@Override
	public V getCurrentValue()
	{
		return current;
	}

	abstract protected double getWeight(V value);
}
