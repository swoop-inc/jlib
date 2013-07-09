package com.swoop.data.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

import java.io.IOException;
import java.util.Map;

import net.ech.util.Hash;

/**
 * A MongoDB update command.
 */
public class UpdateCommand
	implements MongoCollectionCommand<WriteResult>
{
	private DBObject query = new BasicDBObject();
	private DBObject object = new BasicDBObject();
	private boolean upsert;
	private boolean multi;

	/**
	 * Builder method.  Set the ID to query for.
	 */
	public UpdateCommand queryId(Object idValue)
	{
		return queryField(Constants.ID, idValue);
	}

	/**
	 * Builder method.  Add a (key,value) pair to query for.
	 */
	public UpdateCommand queryField(String key, Object value)
	{
		query.put(key, value);
		return this;
	}

	/**
	 * Builder method.  Add a MongoDB query object.
	 */
	public UpdateCommand query(Map<String,Object> query)
	{
		this.query.putAll(query);
		return this;
	}

	/**
	 * Builder method.  Set a specific field of the updated object.
	 */
	public UpdateCommand setObjectField(String key, Object value)
	{
		if (!object.containsKey("$set")) {
			object.put("$set", new Hash());
		}
		((Hash) object.get("$set")).put(key, value);
		return this;
	}

	/**
	 * Builder method.  Set a specific field of the updated object.
	 */
	public UpdateCommand unsetObjectField(String key)
	{
		if (!object.containsKey("$unset")) {
			object.put("$unset", new Hash());
		}
		((Hash) object.get("$unset")).put(key, 1);
		return this;
	}

	/**
	 * Builder method.  Enable insert if the object does not exist.
	 */
	public UpdateCommand upsert()
	{
		return upsert(true);
	}

	/**
	 * Builder method.  Enable or disable insertion in the case that the object does not exist.
	 */
	public UpdateCommand upsert(boolean upsert)
	{
		this.upsert = upsert;
		return this;
	}

	/**
	 * Builder method.  Enable multiple updates.
	 */
	public UpdateCommand multi()
	{
		return multi(true);
	}

	/**
	 * Builder method.  Enable or disable multiple updates.
	 */
	public UpdateCommand multi(boolean multi)
	{
		this.multi = multi;
		return this;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public WriteResult execute(DBCollection dbCollection)
		throws MongoException, IOException
	{
		return dbCollection.update(query, object, upsert, multi);
	}
}
