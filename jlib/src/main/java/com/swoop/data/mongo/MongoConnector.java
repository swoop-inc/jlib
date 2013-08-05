package com.swoop.data.mongo;

import java.io.IOException;

import com.mongodb.DB;
import com.mongodb.MongoException;

import com.swoop.data.util.Connection;
import com.swoop.data.util.ConnectionMonitor;

/**
 * An object capable of connecting to a Mongo DB and executing a command.
 */
public class MongoConnector
{
	private ConnectionMonitor<DB> conn;

	/**
	 * Constructor.
	 * @param connectionKey 
	 *    logical name that may be mapped to a MongoDB connection string by the singleton
	 *    {@link com.swoop.data.mongo.MongoConfiguration} instance
	 */
	public MongoConnector(String connectionKey)
	{
		this(MongoConfiguration.instance, connectionKey);
	}

	/**
	 * Constructor.
	 * @param configuration
	 *    system configuration
	 * @param connectionKey 
	 *    logical name that may be mapped to a MongoDB connection string by the system configuration
	 */
	public MongoConnector(MongoConfiguration configuration, String connectionKey)
	{
		this(configuration, new MongoConnection(configuration.getConnectionString(connectionKey)));
	}

	/**
	 * Copy constructor.
	 */
	public MongoConnector(MongoConnector otherConnector)
	{
		this.conn = otherConnector.conn;
	}

	/**
	 * Constructor for internal use only.  Enables mocking of the MongoDB connection.
	 */
	protected MongoConnector(MongoConfiguration configuration, Connection<DB> connection)
	{
		this.conn = configuration.monitorConnection(connection);
	}

	/**
	 * Execute a command on this connector's MongoDB database.
	 * @param command
	 *    the command to execute
	 * @return whatever the command returns
	 */
	public <T> T executeDbCommand(MongoDbCommand<T> command)
		throws IOException
	{
		try {
			DB db = conn.use();
			try {
				return command.execute(db);
			}
			finally {
				conn.release();
			}
		}
		catch (MongoException e) {
			// Wrap all MongoExceptions thrown by the driver in IOExceptions
			throw new IOException(e);
		}
	}

	/**
	 * Execute a command on a specific collection in this connector's MongoDB database.
	 * @param collectionName
	 *    the name of the collection to pass to the command
	 * @param command
	 *    the command to execute
	 * @return whatever the command returns
	 */
	public <T> T executeCollectionCommand(final String collectionName, final MongoCollectionCommand<T> command)
		throws IOException
	{
		return executeDbCommand(new MongoDbCommand<T>() {
			@Override
			public T execute(DB db)
				throws MongoException, java.io.IOException
			{
				return command.execute(db.getCollection(collectionName));
			}
		});
	}

	@Override
	public String toString()
	{
		return conn.toString();
	}
}
