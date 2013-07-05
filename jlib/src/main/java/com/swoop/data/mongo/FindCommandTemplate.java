package com.swoop.data.mongo;

import java.io.IOException;
import java.util.List;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * Implementation of a MongoDB find or findOne command.
 */
class FindCommandTemplate<T>
	extends FindCommandBase<T>
	implements MongoCollectionCommand<List<T>>
{
	protected FindCommandTemplate(Postprocessor<T> postprocessor)
	{
		super(postprocessor);
	}

	protected FindCommandTemplate(FindCommandBase<?> source, Postprocessor<T> ppOverride)
	{
		super(source, ppOverride);
	}

	/**
	 * Builder method.  Set the ID to query for.
	 */
	@Override
	public FindCommandTemplate<T> withQueryId(Object idValue)
	{
		return (FindCommandTemplate<T>) super.withQueryId(idValue);
	}

	/**
	 * Builder method.  Add a (key,value) pair to query for.
	 */
	@Override
	public FindCommandTemplate<T> withQueryField(String key, Object value)
	{
		return (FindCommandTemplate<T>) super.withQueryField(key, value);
	}

	/**
	 * Builder method.  Switch to "find one".
	 */
	 public FindOneCommandTemplate<T> expectOne()
	 {
		return new FindOneCommandTemplate<T>(this);
	 }

	/**
	 * Builder method.  Supply a post-processor.
	 */
	public <TT> FindCommandTemplate<TT> use(Postprocessor<TT> postprocessor)
	{
		return new FindCommandTemplate<TT>(this, postprocessor);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public List<T> execute(DBCollection dbCollection)
		throws MongoException, IOException
	{
		return executeFind(dbCollection);
	}
}
