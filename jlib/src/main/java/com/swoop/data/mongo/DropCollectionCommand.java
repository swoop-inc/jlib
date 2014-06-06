package com.swoop.data.mongo;

import com.mongodb.DBCollection;
import com.mongodb.MongoException;

/**
 * MongoDB "drop collection" command.
 */
public class DropCollectionCommand extends DefaultMongoCollectionCommand<Void>
{
	@Override
	public Void execute(DBCollection dbCollection)
		throws MongoException
	{
		dbCollection.drop();
		return null;
	}

}
