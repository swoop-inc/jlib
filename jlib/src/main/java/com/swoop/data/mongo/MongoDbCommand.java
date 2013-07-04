package com.swoop.data.mongo;

import com.mongodb.DB;
import com.mongodb.MongoException;

public interface MongoDbCommand<T>
{
	public T execute(DB db)
		throws MongoException, java.io.IOException;
}
