package com.swoop.data;

import java.util.Map;

/**
 * Exposes statistics for a datastore. Can be anything from
 * number of operations, hits/misses for caches, etc.
 */
public interface DatastoreStats
{

	public static final String STAT_TYPE_KEY = "_type";

	/**
	 * Get statistics for this datastore. Returns a general
	 * key-value map of strings and objects so that values
	 * can be either integers, doubles, other maps, etc.
	 *
	 * The main purpose of this returns stats object is to be
	 * serialized into JSON.
	 *
	 * If no stats need to be exposed, just use:
	 * {@code ImmutableMap.of()}
	 *
	 * @return map of key-value pairs of statistics
	 */
	public Map<String, Object> getStats();

}
