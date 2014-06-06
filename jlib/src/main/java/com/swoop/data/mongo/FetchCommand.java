package com.swoop.data.mongo;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

import java.io.IOException;
import java.util.Date;

public class FetchCommand extends DefaultMongoCollectionCommand<Date>
{
	private DBObject		fetch;
	private FetchProcessor<Date>	processor;

	public FetchCommand(DBObject query, FetchProcessor<Date> processor)
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
