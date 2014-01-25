package com.swoop.data.mongo;

import java.io.IOException;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * A MongoDB command that returns a single document, or null.
 */
public class FindOneCommand<T>
	implements MongoCollectionCommand<T>
{
	private DBObject query;
	@SuppressWarnings("unused")
	private DBObject projection;
	private Postprocessor<T> postprocessor;

	public FindOneCommand(Postprocessor<T> postprocessor)
	{
		this(new BasicDBObject(), null, postprocessor);
	}

	protected FindOneCommand(FindOneCommand<?> source, Postprocessor<T> postprocessor)
	{
		this(source.query, source.projection, postprocessor);
	}

	private FindOneCommand(DBObject query, DBObject projection, Postprocessor<T> postprocessor)
	{
		this.query = query;
		this.projection = projection;
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
	 * Builder method.  Include the given field in the results.
	 */
	public FindOneCommand<T> include(String fieldName)
	{
		initProjection();
		projection.put(fieldName, 1);
		return this;
	}

	/**
	 * Builder method.  Exclude the given field in the results.
	 */
	public FindOneCommand<T> exclude(String fieldName)
	{
		initProjection();
		projection.put(fieldName, 0);
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
		return postprocessor.postprocess(dbCollection.findOne(query, projection));
	}

	private void initProjection()
	{
		if (projection == null) {
			projection = new BasicDBObject();
		}
	}
}
