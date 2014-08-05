package com.swoop.data.mongo;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import com.google.common.collect.Maps;
import com.swoop.data.CacheOperations;
import org.apache.commons.lang3.ObjectUtils;
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
		extends MongoCollectionConnector implements CacheOperations
{
	private static final Logger LOGGER = LoggerFactory.getLogger(
			MemoryMongoCollectionConnector.class);

	private final Cache<String, Object> cache;
	private final Cache<String, ObjectUtils.Null> negativeCache;

	/** Expiration time, in seconds, of cached values */
	private final int ttl;
	/** Expiration time, in seconds, of cached null values */
	private final int negativeTtl;


	/**
	 * Construct an instance of {@link MongoCollectionConnector} that caches
	 * the results of Mongo queries.
	 *
	 * @param connectionKey mongo connection key
	 * @param collectionName mongo collection name
	 * @param cacheSpec cache spec for positive/non-null cache passed into {@link com.google.common.cache.CacheBuilderSpec#parse(String)}
	 * @param ttl expiration time (in seconds) for values written into the cache
	 * @param negativeCacheSpec cache spec for negative/null cache passed into {@link com.google.common.cache.CacheBuilderSpec#parse(String)}. If null then positive cacheSpec is used for negative cache.
	 * @param negativeTtl expiration time (in seconds) for null values written into the cache
	 */
	public MemoryMongoCollectionConnector(
			String connectionKey, String collectionName, String cacheSpec, int ttl, String negativeCacheSpec, int negativeTtl)
	{
		super(connectionKey, collectionName);

		Preconditions.checkArgument(ttl > 0);
		Preconditions.checkArgument(negativeTtl > 0);
		Preconditions.checkNotNull(cacheSpec);

		LOGGER.info("Initializing {} with cacheSpec: {}, ttl: {}, negativeCacheSpec: {}, negativeTtl: {}", getClass().getName(), cacheSpec, ttl, negativeCacheSpec, negativeTtl);
		this.cache = CacheBuilder.from(cacheSpec)
				.expireAfterWrite(ttl, TimeUnit.SECONDS)
				.recordStats()
				.build();

		if(negativeCacheSpec == null) {
			negativeCacheSpec = cacheSpec;
		}
		this.negativeCache = CacheBuilder.from(negativeCacheSpec)
				.expireAfterWrite(negativeTtl, TimeUnit.SECONDS)
				.recordStats()
				.build();
		this.ttl = ttl;
		this.negativeTtl = negativeTtl;
	}

	@Override
	public int getTtl()
	{
		return ttl;
	}

	@Override
	public int getNegativeTtl()
	{
		return negativeTtl;
	}

	@Override
	public String getCacheKey(String resourceKey)
	{
		// Doesn't need to transform the key since this is a cache specific to this connector
		return resourceKey;
	}

	@Override
	public void invalidate(String cacheKey)
	{
		cache.invalidate(cacheKey);
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
				cacheValue = negativeCache.getIfPresent(cacheKey);
				if(cacheValue != null) {
					return null;
				}
				T originatingObject = super.executeCollectionCommand(collectionName, command);
				LOGGER.info("Cache MISS (ttl={}) (negativeTtl={}) {}", getTtl(), getNegativeTtl(), cacheKey);
				if(originatingObject != null) {
					cache.put(cacheKey, originatingObject);
				} else {
					negativeCache.put(cacheKey, ObjectUtils.NULL);
				}
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

		CacheStats negativeCacheStats = negativeCache.stats();
		stats.put("negativeHitCount", negativeCacheStats.hitCount());
		stats.put("negativeMissCount", negativeCacheStats.missCount());
		stats.put("negativeHitRate", negativeCacheStats.hitRate());
		stats.put("negativeMissRate", negativeCacheStats.missRate());
		stats.put("negativeEvictionCount", negativeCacheStats.evictionCount());
		stats.put("negativeRequestCount", negativeCacheStats.requestCount());
		stats.put("negativeLoadCount", negativeCacheStats.loadCount());

		return stats;
	}

}
