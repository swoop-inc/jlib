package com.swoop.data.mongo;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

import java.io.IOException;

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
		DBObject filter = getFilter();
		return postprocess(filter == null
			? dbCollection.findOne(getQuery())
			: dbCollection.findOne(getQuery(), filter));
	}

	abstract protected DBObject getQuery();

	protected DBObject getFilter()
	{
		return null;
	}

	abstract protected T postprocess(DBObject dbo)
		throws IOException;
}
