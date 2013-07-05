package com.swoop.data.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

import java.io.IOException;

/**
 * A MongoDB insert command.
 */
public class InsertCommand
	implements MongoCollectionCommand<WriteResult>
{
	private DBObject object = new BasicDBObject();

	/**
	 * Builder method.  Set the MongoDB ID of the object to insert.
	 */
	public InsertCommand withObjectId(Object idValue)
	{
		object.put(Constants.ID, idValue);
		return this;
	}

	/**
	 * Builder method.  Add a (key,value) entry to the object to insert.
	 */
	public InsertCommand withObjectField(String key, Object value)
	{
		object.put(key, value);
		return this;
	}

	@Override
	public WriteResult execute(DBCollection dbCollection)
		throws MongoException, IOException
	{
		return dbCollection.insert(object);
	}
}
