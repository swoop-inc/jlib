package com.swoop.data.mongo;

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
}
