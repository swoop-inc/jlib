package com.swoop.data.mongo;

import java.io.IOException;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * Template method for MongoDB findOne command.
 */
abstract public class FindOneCommand<T>
	implements MongoCollectionCommand<T>
{
	@Override
	public T execute(DBCollection dbCollection)
		throws MongoException, IOException
	{
		final T filter = getFilter();
		return postprocess((T) (filter == null
			? dbCollection.findOne(getQuery())
			: dbCollection.findOne(getQuery(), (DBObject) filter)));
	}

	abstract protected T getQuery();

	protected T getFilter()
	{
		return null;
	}

	abstract protected T postprocess(T dbo)
		throws IOException;
}
