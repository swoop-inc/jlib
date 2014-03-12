package com.swoop.data.mongo;

import com.mongodb.DBCursor;

/**
 * FetchProcessor processes a set of query results
 * 
 * @author ychen
 * 
 */
public interface FetchProcessor<T>
{
	/**
	 * FetchProcessor processes a set of query results.
	 * 
	 * @param cursor
	 *            query result set, non-null
	 * @return the object of type T specific to the implementation
	 */
	public T process(DBCursor cursor);
}
