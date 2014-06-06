package com.swoop.data.mongo;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import com.google.common.collect.Maps;
import com.swoop.data.CacheSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * In-memory cached version of {@link com.swoop.data.mongo.MongoCollectionConnector}
 * that caches the results of Mongo queries. All {@link com.swoop.data.mongo.MongoCollectionCommand}
 * passed in need to implement {@link MongoCollectionCommand#getCacheKey()} method for
 * entries to be written into the cache.
 */
public class MemoryMongoCollectionConnector
		extends MongoCollectionConnector implements CacheSettings
{
	private static final Logger LOGGER = LoggerFactory.getLogger(
			MemoryMongoCollectionConnector.class);

	private final Cache<String, Object> cache;

	/** Expiration time, in seconds, of cached values */
	private final int ttl;

	/**
	 * Construct an instance of {@link MongoCollectionConnector} that caches
	 * the results of Mongo queries.
	 *
	 * @param connectionKey mongo connection key
	 * @param collectionName mongo collection name
	 * @param cacheSpec passed into {@link com.google.common.cache.CacheBuilderSpec#parse(String)}
	 * @param ttl expiration time (in seconds) for values written into the cache
	 */
	public MemoryMongoCollectionConnector(
			String connectionKey, String collectionName, String cacheSpec, int ttl)
	{
		super(connectionKey, collectionName);

		LOGGER.info("Initializing {} with cacheSpec: {}", getClass().getName(), cacheSpec);
		this.cache = CacheBuilder.from(Preconditions.checkNotNull(cacheSpec))
				.expireAfterWrite(ttl, TimeUnit.SECONDS)
				.recordStats()
				.build();
		this.ttl = ttl;
	}

	public Cache<String, Object> getCache()
	{
		return cache;
	}

	@Override
	public int getTtl()
	{
		return ttl;
	}

	@Override
	public String getCacheKey(String resourceKey)
	{
		// Doesn't need to transform the key since this is a cache specific to this connector
		return resourceKey;
	}

	@Override
	public <T> T executeCollectionCommand(
			String collectionName, MongoCollectionCommand<T> command
	) throws IOException
	{
		Optional<String> commandCacheKey = command.getCacheKey();
		if (commandCacheKey.isPresent()) {
			String cacheKey = getCacheKey(commandCacheKey.get());
			Object cacheValue = cache.getIfPresent(cacheKey);
			if (cacheValue != null) {
				return (T) cacheValue;
			} else {
				T originatingObject = super.executeCollectionCommand(collectionName, command);
				LOGGER.info("Cache MISS (ttl={}) {}", getTtl(), cacheKey);
				cache.put(cacheKey, originatingObject);
				return originatingObject;
			}
		}
		return super.executeCollectionCommand(collectionName, command);
	}

	@Override
	public Map<String, Object> getStats()
	{
		Map<String, Object> stats = Maps.newLinkedHashMap();
		stats.put(STAT_TYPE_KEY, getClass().getName().toString());

		CacheStats cacheStats = cache.stats();
		stats.put("hitCount", cacheStats.hitCount());
		stats.put("missCount", cacheStats.missCount());
		stats.put("hitRate", cacheStats.hitRate());
		stats.put("missRate", cacheStats.missRate());
		stats.put("evictionCount", cacheStats.evictionCount());
		stats.put("requestCount", cacheStats.requestCount());
		stats.put("loadCount", cacheStats.loadCount());
		return stats;
	}

}
