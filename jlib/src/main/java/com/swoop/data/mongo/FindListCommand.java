package com.swoop.data.mongo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * A MongoDB find command that returns its results in a list.
 */
public class FindListCommand<T>
	implements MongoCollectionCommand<List<T>>
{
	private DBObject query;
	private DBObject filter;
	private Postprocessor<T> postprocessor;

	public FindListCommand(Postprocessor<T> postprocessor)
	{
		this(new BasicDBObject(), null, postprocessor);
	}

	protected FindListCommand(FindListCommand<?> source, Postprocessor<T> postprocessor)
	{
		this(source.query, source.filter, postprocessor);
	}

	private FindListCommand(DBObject query, DBObject filter, Postprocessor<T> postprocessor)
	{
		this.query = query;
		this.filter = filter;
		this.postprocessor = postprocessor;
	}

	/**
	 * Builder method.  Set the ID to query for.
	 */
	public FindListCommand<T> queryId(Object idValue)
	{
		return queryField(Constants.ID, idValue);
	}

	/**
	 * Builder method.  Add a (key,value) pair to query for.
	 */
	public FindListCommand<T> queryField(String key, Object value)
	{
		query.put(key, value);
		return this;
	}

	/**
	 * Builder method.  Add a MongoDB query object.
	 */
	public FindListCommand<T> query(Map<String,Object> query)
	{
		this.query.putAll(query);
		return this;
	}

	/**
	 * Builder method.  Supply a post-processor.
	 */
	public <TT> FindListCommand<TT> usePostprocessor(Postprocessor<TT> postprocessor)
	{
		return new FindListCommand<TT>(this, postprocessor);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public List<T> execute(DBCollection dbCollection)
		throws MongoException, IOException
	{
		List<T> results = new ArrayList<T>();
		for (DBCursor cursor = dbCollection.find(query, filter); cursor.hasNext(); ) {
			T ppObj = postprocessor.postprocess(cursor.next());
			if (ppObj != null) {
				results.add(ppObj);
			}
		}
		return Collections.unmodifiableList(results);
	}
}
