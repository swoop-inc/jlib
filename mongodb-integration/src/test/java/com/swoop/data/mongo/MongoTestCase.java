package com.swoop.data.mongo;

public class MongoTestCase
{
	static final int	MONGOD_PORT	= 27027;	// must match that in ${project-root}/bin/mongo_start
	static final String DB_NAME = "test";

	static {
		String connectionString = "mongodb://localhost:" + MONGOD_PORT + "/" + DB_NAME;
		MongoConfiguration.instance.setDefaultConnectionString(connectionString);
	}

	public static MongoConnector createDbConnector() throws Exception
	{
		return new MongoConnector("(ignored)");
	}
}
