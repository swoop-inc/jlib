package com.swoop.util;

public abstract class WeightedChoiceIteratorBase<V>
	implements WeightedChoiceIterator<V>
{
	private V[] choices;
	private int index;

	public WeightedChoiceIteratorBase()
	{
	}

	public WeightedChoiceIteratorBase(V[] choices)
	{
		reset(choices);
	}

	public void reset(V[] choices)
	{
		this.choices = choices;
		this.index = 0;
	}

	@Override
	public boolean hasNext()
	{
		return index < choices.length;
	}

	@Override
	public double nextWeight()
	{
		return getWeight(choices[index++]);
	}

	@Override
	public V getCurrentValue()
	{
		return choices[index - 1];
	}

	abstract protected double getWeight(V value);
}
