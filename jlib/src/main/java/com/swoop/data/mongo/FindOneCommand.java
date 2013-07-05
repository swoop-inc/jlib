package com.swoop.data.mongo;

import java.io.IOException;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * A MongoDB find command that returns its results in a list.
 */
public class FindOneCommand<T>
	implements MongoCollectionCommand<T>
{
	private DBObject query;
	private DBObject filter;
	private Postprocessor<T> postprocessor;

	public FindOneCommand(Postprocessor<T> postprocessor)
	{
		this(new BasicDBObject(), null, postprocessor);
	}

	protected FindOneCommand(FindOneCommand<?> source, Postprocessor<T> postprocessor)
	{
		this(source.query, source.filter, postprocessor);
	}

	private FindOneCommand(DBObject query, DBObject filter, Postprocessor<T> postprocessor)
	{
		this.query = query;
		this.filter = filter;
		this.postprocessor = postprocessor;
	}

	/**
	 * Builder method.  Set the ID to query for.
	 */
	public FindOneCommand<T> queryId(Object idValue)
	{
		return queryField(Constants.ID, idValue);
	}

	/**
	 * Builder method.  Add a (key,value) pair to query for.
	 */
	public FindOneCommand<T> queryField(String key, Object value)
	{
		query.put(key, value);
		return this;
	}

	/**
	 * Builder method.  Add a MongoDB query object.
	 */
	public FindOneCommand<T> query(Map<String,Object> query)
	{
		this.query.putAll(query);
		return this;
	}

	/**
	 * Builder method.  Supply a post-processor.
	 */
	public <TT> FindOneCommand<TT> usePostprocessor(Postprocessor<TT> postprocessor)
	{
		return new FindOneCommand<TT>(this, postprocessor);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public T execute(DBCollection dbCollection)
		throws MongoException, IOException
	{
		return postprocessor.postprocess(dbCollection.findOne(query, filter));
	}
}
