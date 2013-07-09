package com.swoop.data.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

import java.io.IOException;
import java.util.Map;

/**
 * A MongoDB insert command.
 */
public class InsertCommand
	implements MongoCollectionCommand<WriteResult>
{
	private DBObject object = new BasicDBObject();

	/**
	 * Builder method.  Set the ID of the new object.
	 */
	public InsertCommand objectId(Object idValue)
	{
		return objectField(Constants.ID, idValue);
	}

	/**
	 * Builder method.  Add a (key,value) pair to the object.
	 */
	public InsertCommand objectField(String key, Object value)
	{
		object.put(key, value);
		return this;
	}

	/**
	 * Builder method.  Extend the new object.
	 */
	public InsertCommand object(Map<String,Object> query)
	{
		this.object.putAll(object);
		return this;
	}

	@Override
	public WriteResult execute(DBCollection dbCollection)
		throws MongoException, IOException
	{
		return dbCollection.insert(object);
	}
}
