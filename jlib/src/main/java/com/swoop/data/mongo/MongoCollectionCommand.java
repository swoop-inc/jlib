package com.swoop.data.mongo;

import com.google.common.base.Optional;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;

public interface MongoCollectionCommand<T>
{
	/**
	 * Execute a command on the given MongoDB collection and return a result
	 * of the type appropriate to the command.
	 */
	public T execute(DBCollection dbCollection)
		throws MongoException, java.io.IOException;

	/**
	 * The need for this interface is a little upsetting. Since
	 * we're working with MongoCollectionCommands that may
	 * or may not be cacheable, there needs to be a way to
	 * indicate how to construct a key used for interacting
	 * with the cache. This is made Optional so that only
	 * classes that set a value here will be cached.
	 *
	 * Note that this only gives an indication that the command
	 * result can be cached, so it's up to the underlying consumer
	 * of the command to cache or not.
	 *
	 * @return
	 */
	public Optional<String> getCacheKey();

}
