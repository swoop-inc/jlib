package com.swoop.data.mongo;

import java.io.IOException;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * Template method for MongoDB find command.
 */
abstract public class FindCommand<T>
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
		return postprocess(dbCollection.find(getQuery(), getFilter()));
	}

	/**
	 * Postprocess the query results to produce whatever result 
	 * type the subclass requires.
	 *
	 * Subclasses must implement this method.
	 *
	 * @param dbCursor  the result of the find() operation - a non-null DB cursor
	 * @return the postprocessed result
	 */
	abstract protected T postprocess(DBCursor dbCursor)
		throws IOException;
}
