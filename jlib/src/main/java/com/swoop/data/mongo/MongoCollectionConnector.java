package com.swoop.data.mongo;

import java.io.IOException;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.mongodb.DB;
import com.swoop.data.DatastoreStats;
import com.swoop.data.util.Connection;

/**
 * A MongoDB connector with an implicit collection name. 
 */
public class MongoCollectionConnector
		extends MongoConnector implements DatastoreStats
{
	private String collectionName;
	private String keyField;

	/**
	 * Constructor.
	 * @param connectionKey 
	 *    logical name that may be mapped to a MongoDB connection string by the singleton
	 *    {@link com.swoop.data.mongo.MongoConfiguration} instance
	 * @param collectionName
	 *    the name of the implicit collection
	 */
	public MongoCollectionConnector(String connectionKey, String collectionName)
	{
		this(MongoConfiguration.instance, connectionKey, collectionName);
	}

	/**
	 * Constructor.
	 * @param configuration
	 *    system configuration
	 * @param connectionKey 
	 *    logical name that may be mapped to a MongoDB connection string by the system
	 *    configuration
	 * @param collectionName
	 *    the name of the implicit collection
	 */
	public MongoCollectionConnector(MongoConfiguration configuration, String connectionKey, String collectionName)
	{
		this(configuration, new MongoConnection(configuration.getConnectionString(connectionKey)), collectionName);
	}

	/**
	 * Constructor.
	 * @param otherConnector
	 *    a previously configured connector to reuse
	 * @param collectionName
	 *    the name of the implicit collection
	 */
	public MongoCollectionConnector(MongoConnector otherConnector, String collectionName)
	{
		super(otherConnector);
		this.collectionName = collectionName;
	}

	/**
	 * Constructor for internal use only.  Enables mocking of the MongoDB connection.
	 */
	protected MongoCollectionConnector(MongoConfiguration configuration, Connection<DB> connection, String collectionName)
	{
		super(configuration, connection);
		this.collectionName = collectionName;
	}

	/**
	 * The field to use as document ID in place of the default (_id). 
	 * @param keyField
	 * @return a reference to this, for chaining
	 */
	public MongoCollectionConnector setKeyField(String keyField)
	{
		this.keyField = keyField;
		return this;
	}

	/**
	 * Create a "find one" command that returns the document identified by the given value
	 * of the key field.  If the key field is null, use the MongoDB document ID ("_id").
	 * @see #setKeyField(String)
	 */
	public FindOneCommand<?> createDefaultFindOneCommand(Object keyValue)
	{
		FindOneCommand<?> findOneCommand = new DefaultFindOneCommand();
		findOneCommand = keyField == null ? findOneCommand.queryId(keyValue) : findOneCommand.queryField(keyField, keyValue);
		return findOneCommand;
	}

	/**
	 * Execute a command on the implicit collection.
	 * @param command
	 *    the command to execute
	 * @return whatever the command returns
	 */
	public <T> T executeCommand(MongoCollectionCommand<T> command)
		throws IOException
	{
		return executeCollectionCommand(collectionName, command);
	}

	/**
	 * Create a Timed "find one" command that returns the document identified by the
	 * given value of the key field. If the key field is null, use the MongoDB document
	 * ID ("_id").
	 * 
	 * @param command
	 *            the command to execute
	 * @param timeOut
	 *            timeOut
	 * @return the "find one" command
	 * 
	 * @see #setKeyField(String)
	 */
	public TimedFindOneCommand createTimedFindOneCommand(Object keyValue, int timeout)
	{
		TimedFindOneCommand findOneCommand = new TimedFindOneCommand(timeout);
		if (keyField == null)
			findOneCommand.queryId(keyValue);
		else
			findOneCommand.queryField(keyField, keyValue);
		return findOneCommand;
	}

	@Override
	public String toString()
	{
		return super.toString() + "/" + collectionName;
	}

	@Override
	public Map<String, Object> getStats()
	{
		return ImmutableMap.of(STAT_TYPE_KEY, (Object)getClass().getName().toString());
	}

}
