package com.swoop.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A Hash is an ordered collection of key/value entries.  It is suitable for representation of a 
 * deserialized JSON object - its keys are strings.  It is a subtype of Map, which makes it 
 * interoperable with the Java Collections Framework.  It supports a chained style of building.
 */
public class Hash extends LinkedHashMap<String,Object>
{
	/**
	 * Construct an empty Hash.
	 */
	public Hash()
	{
	}

	/**
	 * Construct a Hash having the given initial key/value entry.
	 * @param key
	 *    key of the initial entry
	 * @param value
	 *    value of the initial entry
	 */
	public Hash(String key, Object value)
	{
		put(key, value);
	}

	/**
	 * Construct a Hash initialized from the contents of the given map.
	 * @param other
	 *    a map of initial entries
	 */
	public Hash(Map<String,?> other)
	{
		super(other);
	}

	/**
	 * Add an entry to this Hash, replacing any prior entry with the same key.
	 * @param key
	 *    key of the additional entry
	 * @param value
	 *    value of the additional entry
	 * @return this Hash
	 */
    public Hash a(String key, Object value)
    {
        put(key, value);
        return this;
    }

	/**
	 * Add an entry to this Hash, unless the given value is null, in which
	 * case no entry is added.  In either case, any prior entry with the same
	 * key is lost.
	 * @param key
	 *    key of the additional entry
	 * @param value
	 *    value of the additional entry
	 * @return this Hash
	 */
    public Hash ap(String key, Object value)
    {
		if (value == null) {
			remove(key);
		}
		else {
			put(key, value);
		}
        return this;
    }

	/**
	 * @deprecated use {@link #a(String,Object)}
	 */
    public Hash addEntry(String key, Object value)
    {
        return a(key, value);
    }
}
