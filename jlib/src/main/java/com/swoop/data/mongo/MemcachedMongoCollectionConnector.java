package com.swoop.data.mongo;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.swoop.data.CacheOperations;
import com.swoop.framework.InitializationException;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Memcached version of {@link MongoCollectionConnector} that caches the results of
 * Mongo queries. The {@link com.swoop.data.mongo.MongoCollectionCommand} passed in
 * needs to implement (or set) the {@link MongoCollectionCommand#getCacheKey()} method
 * for it to write an entry into the cache.
 */
public class MemcachedMongoCollectionConnector
		extends MongoCollectionConnector implements CacheOperations
{
	private static final Logger LOGGER = LoggerFactory.getLogger(
			MemcachedMongoCollectionConnector.class);

	/** Key in the memcached stats for the number of get hits */
	private static final String GET_HITS_KEY = "get_hits";

	/** Key in the memcached stats for the number of get misses */
	private static final String GET_MISSES_KEY = "get_misses";

	/** This classe's calculated hit ratio based on stats from memcached */
	private static final String AGGREGATE_GET_HIT_RATIO = "get_hit_ratio";

	private final MemcachedClient client;

	/** Expiration time, in seconds, of cached values */
	private final int ttl;

	/** Stores cache entries prefixed with the collection name and a separator */
	private final String cachePrefix;

	/**
	 * Sets the timeout for memcached operations. After the time out is reached,
	 * it'll hit the originating datastore
	 * */
	private int timeout = 500;

	/**
	 * Construct an instance of {@link MongoCollectionConnector} that caches
	 * the results of Mongo queries.
	 *
	 * @param connectionKey mongo connection key
	 * @param collectionName mongo collection name
	 * @param addressString passed to the spymemcached {@link AddrUtil#getAddresses(String)}
	 * @param ttl expiration time (in seconds) for values written into the cache
	 * @throws InitializationException if a connection to memcached couldn't be established
	 */
	public MemcachedMongoCollectionConnector(
			String connectionKey, String collectionName, String addressString, int ttl
	) throws InitializationException
	{
		super(connectionKey, collectionName);

		this.cachePrefix = collectionName + "::";

		LOGGER.info("Initializing {} with address: {}", getClass().getName(), addressString);
		try {
			this.client = new MemcachedClient(AddrUtil.getAddresses(addressString));
		} catch (IOException e) {
			LOGGER.error("Error while initializing {} with {}\n",
					MemcachedClient.class.getName(), addressString, e);
			throw new InitializationException(e);
		}

		this.ttl = ttl;
	}

	public MemcachedClient getClient()
	{
		return client;
	}

	public String getCachePrefix()
	{
		return cachePrefix;
	}

	public int getTimeout()
	{
		return timeout;
	}

	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	@Override
	public int getTtl()
	{
		return ttl;
	}

	@Override
	public String getCacheKey(String resourceKey)
	{
		return getCachePrefix() + resourceKey;
	}

	@Override
	public void invalidate(String cacheKey)
	{
		getClient().delete(cacheKey);
	}

	@Override
	public <T> T executeCollectionCommand(
			String collectionName, MongoCollectionCommand<T> command
	) throws IOException
	{
		Optional<String> commandCacheKey = command.getCacheKey();
		try {
			// The MongoCollectionCommand object should set a cacheKey, otherwise
			// we'll just use the originating datastore
			if (commandCacheKey.isPresent()) {
				String cacheKey = getCacheKey(commandCacheKey.get());
				Future<Object> f = client.asyncGet(cacheKey);
				try {
					String cacheValue = (String)f.get(getTimeout(), TimeUnit.MILLISECONDS);
					if (cacheValue != null) {
						return (T) JSON.parse(cacheValue);
					} else {
						// This is ugly, but since jlib already punches the type system in
						// the face, let's keep doing it
						DBObject originatingValue = (DBObject)super.executeCollectionCommand(collectionName, command);
						cacheValue = JSON.serialize(originatingValue);
						LOGGER.info("Cache MISS (ttl={}) {}", getTtl(), cacheKey);
						client.set(cacheKey, getTtl(), cacheValue);
						return (T) originatingValue;
					}
				} catch(Exception e) {
					// Errors thrown by spymemcached shouldn't cause the return to fail. We'll just hit
					// the originating store
					f.cancel(true);
					LOGGER.error("Error encountered while attempting operations against memcached", e);
				}
			}
		} catch(IllegalStateException e) {
			LOGGER.error("Error encountered while connecting to memcached", e);
		}
		return super.executeCollectionCommand(collectionName, command);
	}

	@Override
	public Map<String, Object> getStats()
	{
		Map<String, Object> memcachedStats = Maps.newLinkedHashMap();
		long aggGetHits = 0L;
		long aggGetMisses = 0L;

		Map<SocketAddress, Map<String, String>> memcacheStats = client.getStats();
		if (memcacheStats != null) {
			for (Map.Entry<SocketAddress, Map<String, String>> entry : memcacheStats.entrySet()) {
				memcachedStats.put(entry.getKey().toString(), entry.getValue());
				aggGetHits += getLong(entry.getValue(), GET_HITS_KEY);
				aggGetMisses += getLong(entry.getValue(), GET_MISSES_KEY);
			}
		}

		// Constructed this way so that the ordering has the aggregate stats
		// as the first elements (when traversing the Map)
		Map<String, Object> stats = Maps.newLinkedHashMap();
		stats.put(STAT_TYPE_KEY, getClass().getName().toString());
		stats.put(AGGREGATE_GET_HIT_RATIO, getRatio(aggGetHits, aggGetMisses));
		stats.putAll(memcachedStats);
		return stats;
	}

	private long getLong(Map<String, String> stats, String key)
	{
		String value = stats.get(key);
		if (value != null) {
			try {
				return Long.parseLong(value);
			} catch (NumberFormatException e) {
				// ignored
			}
		}
		return 0L;
	}

	private String getRatio(long hits, long misses)
	{
		long total = hits + misses;
		return Double.toString((double) hits / (double) total);
	}

}
