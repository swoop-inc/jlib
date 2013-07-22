package com.swoop.data.mongo;

public class MongoTestCase
{
	static final int	MONGOD_PORT	= 27027;	// must match that in ${project-root}/bin/mongo_start
	static final String DB_NAME = "test";

	public static MongoConnector createDbConnector() throws Exception
	{
		final String connectionString = "mongodb://localhost:" + MONGOD_PORT + "/" + DB_NAME;
		return new DefaultMongoConnector(connectionString);
	}
}
