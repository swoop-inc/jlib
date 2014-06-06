package com.swoop.data.mongo;

import com.google.common.base.Optional;

/**
 *
 */
public abstract class DefaultMongoCollectionCommand<T> implements MongoCollectionCommand<T>
{

	private String cacheKey;

	@Override
	public Optional<String> getCacheKey()
	{
		return Optional.fromNullable(cacheKey);
	}

	public void setCacheKey(String cacheKey)
	{
		this.cacheKey = cacheKey;
	}

}
