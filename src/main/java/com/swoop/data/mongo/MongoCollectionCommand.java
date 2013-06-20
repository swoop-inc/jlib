package com.swoop.data.mongo;

import com.mongodb.DBCollection;
import com.mongodb.MongoException;

public interface MongoCollectionCommand<T>
{
	public T execute(DBCollection dbCollection)
		throws MongoException, java.io.IOException;
}
