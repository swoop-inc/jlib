package com.swoop.data.mongo;

import java.util.HashMap;
import java.util.Map;

/**
 * A MongoConfigurator for mocking/testing and simple deployments.
 */
public class DefaultMongoConfigurator
	implements MongoConfigurator
{
	private Map<String,String> uriMap = new HashMap<String,String>();
	private Map<String,MongoConnector> connectorMap = new HashMap<String,MongoConnector>();
	private MongoConnector defaultConnector;
	private int maxIdleMillis = -1;

	/**
	 * The maximum time a connection produced via this configurator is allowed to remain idle, before it
	 * is automatically closed.  If this value is zero, each connection is closed immediately when not
	 * in use.
	 */
	public void setMaxIdleMillis(int maxIdleMillis)
	{
		this.maxIdleMillis = maxIdleMillis;
	}

	/**
	 * Associate a configuration key with a MongoDB connection string (called "URI" but not, strictly
	 * speaking, always a valid URI).
	 * @param configurationKey
	 *    a configuration key string
	 * @param mongoUriString
	 *    the connection string to associated with the configuration key
	 */
	public void addEntry(String configurationKey, String mongoUriString)
	{
		uriMap.put(configurationKey, mongoUriString);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public synchronized MongoConnector createConnector(String configurationKey)
		throws java.io.IOException
	{
		if (uriMap.containsKey(configurationKey)) {
			String uri = uriMap.get(configurationKey);
			if (connectorMap.containsKey(uri)) {
				return connectorMap.get(uri);
			}
			MongoConnector conn = configureConnector(new DefaultMongoConnector(uri));
			connectorMap.put(uri, conn);
			return conn;
		}
		else { 
			if (defaultConnector == null) {
				defaultConnector = configureConnector(new DefaultMongoConnector());
			}
			return defaultConnector;
		}
	}

	private MongoConnector configureConnector(DefaultMongoConnector mongoConnector)
	{
		if (maxIdleMillis >= 0) {
			mongoConnector.setMaxIdleMillis(maxIdleMillis);
		}
		return mongoConnector;
	}
}
