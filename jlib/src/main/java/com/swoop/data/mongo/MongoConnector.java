package com.swoop.data.mongo;

import java.io.IOException;

/**
 * An object capable of connecting to a Mongo DB and executing a command.
 */
public interface MongoConnector
{
	/**
	 * Execute a command on this connector's MongoDB database.
	 * @param command
	 *    the command to execute
	 * @return whatever the command returns
	 */
	public <T> T executeDbCommand(MongoDbCommand<T> command)
		throws IOException;

	/**
	 * Execute a command on a specific collection this connector's MongoDB database.
	 * @param collectionName
	 *    the name of the collection to pass to the command
	 * @param command
	 *    the command to execute
	 * @return whatever the command returns
	 */
	public <T> T executeCollectionCommand(String collectionName, MongoCollectionCommand<T> command)
		throws IOException;
}
