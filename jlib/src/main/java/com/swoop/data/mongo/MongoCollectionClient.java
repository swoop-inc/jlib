package com.swoop.data.mongo;

/**
 * A MongoCollectionClient is a MongoClient that works with a specific MongoDB collection by default.
 */
public class MongoCollectionClient
 extends MongoClient
{
	private String	collectionName;
	private String	keyField;

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

	public <T> T executeCommand(MongoCollectionCommand<T> command)
		throws java.io.IOException
	{
		return executeCollectionCommand(collectionName, command);
	}

	/**
	 * The name of the collection that this client queries by default.
	 * @param collectionName
	 */
	public void setCollectionName(String collectionName)
	{
		this.collectionName = collectionName;
	}

	/**
	 * The field to use as document ID in place of the default (_id). 
	 * @param keyField
	 */
	public void setKeyField(String keyField)
	{
		this.keyField = keyField;
	}

	/***
	public String getKeyField()
	{
		return keyField;
	}
	***/

	/**
	 * Create a "find one" command that returns the document identified by the given value
	 * of the key field.  If the key field is null, use the MongoDB document ID ("_id").
	 * @see #setKeyField(String)
	 */
	public MongoCollectionCommand<?> createDefaultFindOneCommand(Object keyValue)
	{
		FindOneCommand findOneCommand = new DefaultFindOneCommand();
		findOneCommand = keyField == null ? findOneCommand.queryId(keyValue) : findOneCommand.queryField(keyField, keyValue);
		return findOneCommand;
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
