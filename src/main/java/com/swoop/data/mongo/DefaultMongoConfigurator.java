package com.swoop.data.mongo;

import java.io.IOException;

/**
 * A MongoConfigurator for mocking/testing and simple deployments.
 */
public class DefaultMongoConfigurator
	implements MongoConfigurator
{
	/**
	 * @inheritDoc
	 */
	@Override
	public MongoConnector createConnector(String configurationKey)
		throws IOException
	{
		return new DefaultMongoConnector();
	}
}
