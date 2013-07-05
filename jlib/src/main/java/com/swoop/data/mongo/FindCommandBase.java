package com.swoop.data.mongo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * Skeletal implementation of a MongoDB find or findOne command.
 */
abstract class FindCommandBase<T>
{
	private DBObject query;
	private DBObject filter;
	private Postprocessor<T> postprocessor;

	protected FindCommandBase(Postprocessor<T> postprocessor)
	{
		this(new BasicDBObject(), null, postprocessor);
	}

	protected FindCommandBase(FindCommandBase<T> source)
	{
		this(source.query, source.filter, source.postprocessor);
	}

	protected FindCommandBase(FindCommandBase<?> source, Postprocessor<T> ppOverride)
	{
		this(source.query, source.filter, ppOverride);
	}

	private FindCommandBase(DBObject query, DBObject filter, Postprocessor<T> postprocessor)
	{
		this.query = query;
		this.filter = filter;
		this.postprocessor = postprocessor;
	}

	protected T executeFindOne(DBCollection dbCollection)
		throws MongoException, IOException
	{
		return postprocessor.postprocess(dbCollection.findOne(query, filter));
	}

	protected List<T> executeFind(DBCollection dbCollection)
		throws MongoException, IOException
	{
		List<T> results = new ArrayList<T>();
		for (DBCursor cursor = dbCollection.find(query, filter); cursor.hasNext(); ) {
			results.add(postprocessor.postprocess(cursor.next()));
		}
		return Collections.unmodifiableList(results);
	}

	public FindCommandBase<T> withQueryId(Object idValue)
	{
		query.put(Constants.ID, idValue);
		return this;
	}

	public FindCommandBase<T> withQueryField(String key, Object value)
	{
		query.put(key, value);
		return this;
	}
}
