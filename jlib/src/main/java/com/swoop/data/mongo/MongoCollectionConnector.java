package com.swoop.data.mongo;

import java.io.IOException;

/**
 * An object capable of connecting to a Mongo DB and executing a command against
 * a specific collection.
 */
public interface MongoCollectionConnector
{
	/**
	 * Execute a command on the implicit collection.
	 * @param command
	 *    the command to execute
	 * @return whatever the command returns
	 */
	public <T> T executeCommand(MongoCollectionCommand<T> command)
		throws IOException;
}
