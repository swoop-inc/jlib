package com.swoop.data.mongo;

import com.mongodb.DB;
import com.mongodb.MongoException;

import com.swoop.data.util.Connection;
import com.swoop.data.util.ConnectionMonitor;

import java.io.IOException;

/**
 * Default implementation of MongoConnector, based on the MongoDB Java driver.
 */
public class DefaultMongoConnector
	implements MongoConnector
{
	private ConnectionMonitor<DB> conn;

	/**
	 * Constructor.  Connects to the default MongoDB database.
	 */
	public DefaultMongoConnector()
	{
		this(new MongoConnection());
	}

	/**
	 * Constructor.  Connects to the database as specified by the URI string, which must conform to 
	 * MongoDB URI specifications.
	 * @param uriString  a MongoDB "URI"
	 */
	public DefaultMongoConnector(String uriString)
	{
		this(new MongoConnection(uriString));
	}

	/**
	 * Constructor.  Enables mocking of the MongoDB connection.
	 */
	protected DefaultMongoConnector(Connection<DB> connection)
	{
		this.conn = new ConnectionMonitor<DB>(connection);
	}

	/**
	 * The maximum time a connection managed by this connector is allowed to remain idle, before it
	 * is automatically closed.  If this value is non-positive, the connection is closed immediately
	 * when not in use.
	 */
	public int getMaxIdleMillis()
	{
		return conn.getMaxIdleMillis();
	}

	public void setMaxIdleMillis(int maxIdleMillis)
	{
		conn.setMaxIdleMillis(maxIdleMillis);
	}

	/**
	 * @inheritDoc
	 */
	@Override
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
	 * @inheritDoc
	 */
	@Override
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
