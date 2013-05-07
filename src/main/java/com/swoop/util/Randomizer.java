package com.swoop.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

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
	 * @param choices  a weighted view of the choices
	 * @return  the selected item, or null if no item was selected
	 */
	public <V> V select(WeightedChoiceIterator<V> choices)
	{
		double runningTotalWeight = 0.0;
		V candidate = null;
		while (choices.hasNext()) {
			double weight = choices.nextWeight();
			if (weight > 0.0) {
				double prevTotalWeight = runningTotalWeight;
				runningTotalWeight += weight;
				if ((rng.nextDouble() * runningTotalWeight) >= prevTotalWeight) {
					candidate = choices.getCurrentValue();
				}
			}
		}
		return candidate;
	}

	/**
	 * Make a top N selection among weighted choices.
	 * @param choices  a weighted view of the choices
	 * @param n   the maximum number of items to return
	 * @return a non-null list of selected items, the size of which never exceeds n
	 */
	public <V> List<V> select(WeightedChoiceIterator<V> choices, int n)
	{
		double runningTotalWeight = 0.0;
		LinkedList<ChoiceNode<V>> top = null;
		while (choices.hasNext()) {
			double weight = choices.nextWeight();
			if (weight > 0.0) {
				if (top == null) {
					top = new LinkedList<ChoiceNode<V>>();
				}
				double prevTotalWeight = runningTotalWeight;
				runningTotalWeight += weight;
				double workingTotalWeight = runningTotalWeight;
				ListIterator<ChoiceNode<V>> candidates = top.listIterator(); 
				boolean inserted = false;
				while (candidates.hasNext()) {
					ChoiceNode<V> candidate = candidates.next();
					if ((rng.nextDouble() * workingTotalWeight) >= prevTotalWeight) {
						candidates.set(new ChoiceNode(choices.getCurrentValue(), weight));
						candidates.add(candidate);
						if (top.size() > n) {
							top.removeLast();
						}
						inserted = true;
						break;
					}
					workingTotalWeight -= candidate.weight;
					prevTotalWeight -= candidate.weight;
				}
				if (!inserted && top.size() < n) {
					top.add(new ChoiceNode(choices.getCurrentValue(), weight));
				}
			}
		}
		return toValueList(top);
	}

	private static class ChoiceNode<V>
	{
		V value;
		double weight;

		ChoiceNode(V value, double weight) {
			this.value = value;
			this.weight = weight;
		}
	}

	private static <V> List<V> toValueList(List<ChoiceNode<V>> nodeList)
	{
		List<V> valueList = new ArrayList<V>(nodeList == null ? 0 : nodeList.size());
		if (nodeList != null) {
			for (ChoiceNode<V> node : nodeList) {
				valueList.add(node.value);
			}
		}
		return valueList;
	}
}
