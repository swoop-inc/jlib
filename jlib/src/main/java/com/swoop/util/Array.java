package com.swoop.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An Array is an ordered collection of values.  It is suitable for representation of a 
 * deserialized JSON array.  It is a subtype of List, which makes it interoperable with
 * the Java Collections Framework.  It supports a chained style of building.
 */
public class Array extends ArrayList<Object>
{
	/**
	 * Construct an empty Array.
	 */
	public Array()
	{
	}

	/**
	 * Construct an Array having the given initial value.
	 * @param value
	 *    value of the initial entry
	 */
	public Array(Object value)
	{
		add(value);
	}

	/**
	 * Construct a Hash initialized from the contents of the given collection.
	 * @param other
	 *    a collection of initial entries
	 */
	public Array(Collection<?> other)
	{
		super(other);
	}

	/**
	 * Add an entry to the end of this Array
	 * @param value
	 *    value of the additional entry
	 * @return this Array
	 */
    public Array a(Object value)
    {
        add(value);
        return this;
    }
}
