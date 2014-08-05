package com.swoop.data;

/**
 * Interface for defining settings/functions for caching functionality.
 */
public interface CacheOperations
{

	/**
	 * Defines an expiration time, in seconds, for cached non-null objects.
	 */
	public int getTtl();

	/**
	 * Defines an expiration time, in seconds, for cached null objects.
	 */
	public int getNegativeTtl();

	/**
	 * Keys used for caching can be different than the originating resource key. For example,
	 * namespacing with a class name or organization name is usually a good idea in order
	 * to allow a heterogeneous set of data to be stored in the same cache.
	 *
	 * If the cache key doesn't need to be different, just return the resourceKey as is.
	 *
	 * @param resourceKey key of the original resource
	 * @return the equivalent cache key for the given resource key
	 */
	public String getCacheKey(String resourceKey);

	/**
	 * Invalidate a cache entry. Make sure to do the proper transform from
	 * resourceKey to cacheKey via {@link #getCacheKey(String)} if needed.
	 *
	 * @param cacheKey the key used to access the cache item
	 */
	public void invalidate(String cacheKey);

}
