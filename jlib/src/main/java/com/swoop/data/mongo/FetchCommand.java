package com.swoop.data.mongo;

import java.io.IOException;
import java.util.Date;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public class FetchCommand
		implements MongoCollectionCommand<Date>
{
	private DBObject		fetch;
	private FetchProcessor	processor;

	public FetchCommand(DBObject query, FetchProcessor processor)
	{
		fetch = query;
		this.processor = processor;
	}

	@Override
	public Date execute(DBCollection dbCollection) throws MongoException, IOException
	{
		return processor.process(dbCollection.find(fetch));
	}
}
