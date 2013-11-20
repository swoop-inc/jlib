package net.ech.util;

import java.util.*;

/**
 * Generic map class having chainable methods.
 */
public class Hash extends LinkedHashMap<String,Object>
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 798914374610344341L;

	public Hash()
	{
	}

	public Hash(String key, Object value)
	{
		addEntry(key, value);
	}

	public Hash(Map<String,Object> other)
	{
		super(other);
	}

    public Hash addEntry(String key, Object value)
    {
        put(key, value);
        return this;
    }
}
