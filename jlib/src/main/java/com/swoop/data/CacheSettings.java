package com.swoop.data;

/**
 *
 */
public interface CacheSettings
{

	public int getTtl();

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

}
