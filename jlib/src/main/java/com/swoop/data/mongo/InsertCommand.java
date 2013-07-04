package com.swoop.data.mongo;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

import java.io.IOException;

/**
 * Template method for MongoDB update command.
 */
abstract public class InsertCommand
	implements MongoCollectionCommand<WriteResult>
{
	@Override
	public WriteResult execute(DBCollection dbCollection)
		throws MongoException, IOException
	{
		return dbCollection.insert(getObject());
	}

	abstract protected DBObject getObject();
}
