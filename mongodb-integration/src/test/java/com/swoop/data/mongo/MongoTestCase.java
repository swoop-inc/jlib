package com.swoop.data.mongo;

import com.mongodb.DB;

import com.swoop.data.util.Connection;

public class MongoTestCase
{
	static final int MONGOD_PORT = 27018;   // must match that in pom.xml
	static final String DB_NAME = "test";

	public static MongoConnector createDbConnector() throws Exception
	{
		String connectionString = "mongodb://localhost:" + MONGOD_PORT + "/" + DB_NAME;
		return new DefaultMongoConnector(connectionString);
	}
}
