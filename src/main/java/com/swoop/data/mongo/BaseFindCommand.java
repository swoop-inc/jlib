package com.swoop.data.mongo;

import java.io.IOException;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * Common base for FindCommand and FindOneCommand.
 */
abstract public class BaseFindCommand<T>
	implements MongoCollectionCommand<T>
{
	/**
	 * Get the query object to pass to the MongoDB driver.
	 *
	 * Subclasses must implement this method.
	 */
	abstract protected DBObject getQuery();

	/**
	 * Get the filter object to pass to the MongoDB driver.
	 *
	 * Default implementation returns null (empty filter)
	 */
	protected DBObject getFilter()
	{
		return null;
	}
}
