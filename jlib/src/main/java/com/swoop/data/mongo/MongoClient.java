package com.swoop.data.mongo;

/**
 * A MongoClient has a MongoConnector and knows how to use it.
 */
public class MongoClient
{
	private MongoConnector connector;

	/**
	 * Constructor.
	 * @param configurator  
	 *     a connector to external configuration data
	 * @param configurationKey
	 *     the key of this item's configuration, to be resolved by the configurator
	 */
	public MongoClient(MongoConfigurator configurator, String configurationKey)
		throws java.io.IOException
	{
		this(configurator.createConnector(configurationKey));
	}

	/**
	 * Constructor.
	 * @param connector   the connector for execution
	 */
	public MongoClient(MongoConnector connector)
	{
		this.connector = connector;
	}

	protected <T> T executeDbCommand(MongoDbCommand<T> command)
		throws java.io.IOException
	{
		return connector.executeDbCommand(command);
	}

	protected <T> T executeCollectionCommand(String collectionName, MongoCollectionCommand<T> command)
		throws java.io.IOException
	{
		return connector.executeCollectionCommand(collectionName, command);
	}

	/**
	 * For error messages.
	 */
	@Override
	public String toString()
	{
		return connector.toString();
	}
}
