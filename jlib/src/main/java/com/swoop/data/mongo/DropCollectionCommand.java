package com.swoop.data.mongo;

import com.mongodb.DBCollection;
import com.mongodb.MongoException;

/**
 * MongoDB "drop collection" command.
 */
public class DropCollectionCommand
	implements MongoCollectionCommand<Void>
{
	@Override
	public Void execute(DBCollection dbCollection)
		throws MongoException
	{
		dbCollection.drop();
		return null;
	}
}
