package com.swoop.data.redis;

/**
 * A RedisConfigurator is a connector to external configuration data.  It is responsible for
 * resolving keys to their configurations and creating Redis connectors based on those configurations.
 */
public interface RedisConfigurator
{
	/**
	 * Resolve the given key to a configuration, and create the configured connector.
	 * @param configurationKey
	 *     the key of the desired configuration
	 */
	public RedisConnector createConnector(String configurationKey)
		throws java.io.IOException;
}
