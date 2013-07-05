package com.swoop.data.mongo;

import java.io.IOException;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * Template method for MongoDB findOne command.
 * @deprecated
 */
abstract public class FindOneCommand<T>
	extends BaseFindCommand<T>
	implements MongoCollectionCommand<T>
{
	/**
	 * @inheritDoc
	 */
	@Override
	public T execute(DBCollection dbCollection)
		throws MongoException, IOException
	{
		final DBObject filter = getFilter();
		return postprocess(filter == null
			? dbCollection.findOne(getQuery())
			: dbCollection.findOne(getQuery(), filter));
	}

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

	/**
	 * Postprocess the query results to produce whatever result 
	 * type the subclass requires.
	 *
	 * Subclasses must implement this method.
	 *
	 * @param dbo  the result of the findOne() operation - may be null
	 * @return the postprocessed result
	 */
	abstract protected T postprocess(DBObject dbo)
		throws IOException;
}
