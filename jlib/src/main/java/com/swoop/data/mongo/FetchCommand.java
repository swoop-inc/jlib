package com.swoop.data.mongo;

import java.io.IOException;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public class FetchCommand
		implements MongoCollectionCommand<DBCursor>
{
	private DBObject	fetch;

	public FetchCommand(DBObject query)
	{
		fetch = query;
	}

	@Override
	public DBCursor execute(DBCollection dbCollection) throws MongoException, IOException
	{
		return dbCollection.find(fetch);
	}
}
