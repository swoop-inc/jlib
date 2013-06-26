package com.swoop.data.mongo;

/**
 * A MongoCollectionClient works with a specific MongoDB collection.
 */
public class MongoCollectionClient
	extends MongoClient
{
	private String collectionName;

	/**
	 * Constructor.
	 * @param configurator  
	 *     a connector to external configuration data
	 * @param configurationKey
	 *     the key of this item's configuration, to be resolved by the configurator
	 * @param collectionName
	 *     the name of the collection to work with
	 */
	public MongoCollectionClient(MongoConfigurator configurator, String configurationKey, String collectionName)
		throws java.io.IOException
	{
		super(configurator, configurationKey);
		this.collectionName = collectionName;
	}

	/**
	 * Constructor.
	 * @param connector
	 *     the connector for execution
	 * @param collectionName
	 *     the name of the collection to work with
	 */
	public MongoCollectionClient(MongoConnector connector, String collectionName)
	{
		super(connector);
		this.collectionName = collectionName;
	}

	/**
	 * MongoCollectionClient is meant to be extended.
	 */
	protected <T> T executeCommand(MongoCollectionCommand<T> command)
		throws java.io.IOException
	{
		return executeCollectionCommand(collectionName, command);
	}

	/**
	 * For error messages.
	 */
	@Override
	public String toString()
	{
		return super.toString() + "/" + collectionName;
	}
}
