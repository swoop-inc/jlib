package com.swoop.data.mongo;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

import java.io.IOException;

/**
 * Template method for MongoDB update command.
 */
abstract public class UpdateCommand
	implements MongoCollectionCommand<WriteResult>
{
	@Override
	public WriteResult execute(DBCollection dbCollection)
		throws MongoException, IOException
	{
		return dbCollection.update(getQuery(), getObject(), isUpsert(), false);
	}

	abstract protected DBObject getQuery();

	abstract protected DBObject getObject();

	protected boolean isUpsert()
	{
		return false;
	}
}
