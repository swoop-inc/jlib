package com.swoop.data.mongo;

import com.mongodb.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A MongoDB find command that returns its results in a list.
 */
public class FindListCommand<T> extends DefaultMongoCollectionCommand<List<T>>
{
	private DBObject query;
	private DBObject projection;
	private Postprocessor<T> postprocessor;

	public FindListCommand(Postprocessor<T> postprocessor)
	{
		this(new BasicDBObject(), null, postprocessor);
	}

	protected FindListCommand(FindListCommand<?> source, Postprocessor<T> postprocessor)
	{
		this(source.query, source.projection, postprocessor);
	}

	private FindListCommand(DBObject query, DBObject projection, Postprocessor<T> postprocessor)
	{
		this.query = query;
		this.projection = projection;
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
	 * Builder method.  Include the given field in the results.
	 */
	public FindListCommand<T> include(String fieldName)
	{
		initProjection();
		projection.put(fieldName, 1);
		return this;
	}

	/**
	 * Builder method.  Exclude the given field in the results.
	 */
	public FindListCommand<T> exclude(String fieldName)
	{
		initProjection();
		projection.put(fieldName, 0);
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
		for (DBCursor cursor = dbCollection.find(query, projection); cursor.hasNext(); ) {
			T ppObj = postprocessor.postprocess(cursor.next());
			if (ppObj != null) {
				results.add(ppObj);
			}
		}
		return Collections.unmodifiableList(results);
	}

	private void initProjection()
	{
		if (projection == null) {
			projection = new BasicDBObject();
		}
	}

}
