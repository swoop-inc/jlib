package com.swoop.data.mongo;

import java.io.IOException;

/**
 * Default implementation of MongoCollectionConnector.
 */
public class DefaultMongoCollectionConnector
	implements MongoCollectionConnector
{
	private MongoConnector dbConnector;
	private String collectionName;

	/**
	 * Constructor.
	 * @param dbConnector    a MongoConnector
	 * @param collectionName the collection name
	 */
	public DefaultMongoCollectionConnector(MongoConnector dbConnector, String collectionName)
	{
		this.dbConnector = dbConnector;
		this.collectionName = collectionName;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public <T> T executeCommand(final MongoCollectionCommand<T> command)
		throws IOException
	{
		return dbConnector.executeCollectionCommand(collectionName, command);
	}

	@Override
	public String toString()
	{
		return dbConnector.toString() + "/" + collectionName;
	}
}
