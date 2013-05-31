package com.swoop.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A base for Iterator implementations, providing boilerplate.
 */
abstract public class AbstractIterator<T>
	implements Iterator<T>
{
	private T nextItem;

	@Override
	public boolean hasNext()
	{
		if (nextItem == null) {
			nextItem = advance();
		}
		return nextItem != null;
	}

	@Override
	public T next()
	{
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		return consumeNextItem();
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Subclass must implement.
	 * @return the next item, or null if there is no next item
	 */
	abstract protected T advance();

	private T consumeNextItem()
	{
		T nextItem = this.nextItem;
		this.nextItem = null;
		return nextItem;
	}
}
