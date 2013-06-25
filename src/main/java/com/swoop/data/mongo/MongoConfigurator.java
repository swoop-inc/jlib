package com.swoop.data.mongo;

/**
 * A MongoConfigurator is a connector to external configuration data.  It is responsible for
 * resolving keys to their configurations and creating Mongo connectors based on those configurations.
 */
public interface MongoConfigurator
{
	/**
	 * Resolve the given key to a configuration, and create the configured connector.
	 * @param configurationKey
	 *     the key of the desired configuration
	 */
	public MongoConnector createConnector(String configurationKey)
		throws java.io.IOException;
}
