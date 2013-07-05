package com.swoop.data.mongo;

import java.io.IOException;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * Implementation of a MongoDB findOne command.
 */
class FindOneCommandTemplate<T>
	extends FindCommandBase<T>
	implements MongoCollectionCommand<T>
{
	protected FindOneCommandTemplate(Postprocessor<T> postprocessor)
	{
		super(postprocessor);
	}

	protected FindOneCommandTemplate(FindCommandBase<T> source)
	{
		super(source);
	}

	protected FindOneCommandTemplate(FindCommandBase<?> source, Postprocessor<T> ppOverride)
	{
		super(source, ppOverride);
	}

	/**
	 * Builder method.  Set the ID to query for.
	 */
	public FindOneCommandTemplate<T> withQueryId(Object idValue)
	{
		return (FindOneCommandTemplate<T>) super.withQueryId(idValue);
	}

	/**
	 * Builder method.  Add a (key,value) pair to query for.
	 */
	public FindOneCommandTemplate<T> withQueryField(String key, Object value)
	{
		return (FindOneCommandTemplate<T>) super.withQueryField(key, value);
	}

	/**
	 * Builder method.  Supply a post-processor.
	 */
	public <TT> FindOneCommandTemplate<TT> use(Postprocessor<TT> postprocessor)
	{
		return new FindOneCommandTemplate<TT>(this, postprocessor);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public T execute(DBCollection dbCollection)
		throws MongoException, IOException
	{
		return executeFindOne(dbCollection);
	}
}
