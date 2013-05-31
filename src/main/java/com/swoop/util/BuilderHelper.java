package com.swoop.util;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import net.ech.doc.Document;

/**
 * Bean building utilities.
 */
public abstract class BuilderHelper
{
	/**
	 * Create a new list on demand.
	 * @param sourceList   the current list
	 * @param forceNew     if true and the current list is non-null, copy the list and return it.
	 * @return a non-null list, possibly the original
	 */
	public static <T> List<T> createLazyList(List<T> sourceList, boolean forceNew)
	{
		if (sourceList == null) {
			return new ArrayList<T>();
		}
		else if (forceNew) {
			return new ArrayList<T>(sourceList);
		}
		else {
			return sourceList;
		}
	}

	/**
	 * Create a new set on demand.
	 * @param sourceSet   the current set
	 * @param forceNew     if true and the current set is non-null, copy the set and return it.
	 * @return a non-null set, possibly the original
	 */
	public static <T> Set<T> createLazySet(Set<T> sourceSet, boolean forceNew)
	{
		if (sourceSet == null) {
			return new HashSet<T>();
		}
		else if (forceNew) {
			return new HashSet<T>(sourceSet);
		}
		else {
			return sourceSet;
		}
	}

	/**
	 * Create a new map on demand.
	 * @param sourceMap   the current map
	 * @param forceNew     if true and the current map is non-null, copy the map and return it.
	 * @return a non-null map, possibly the original
	 */
	public static <K,V> Map<K,V> createLazyMap(Map<K,V> sourceMap, boolean forceNew)
	{
		if (sourceMap == null) {
			return new HashMap<K,V>();
		}
		else if (forceNew) {
			// Deep copy:
			return new Document(sourceMap).copy().get(Map.class);
		}
		else {
			return sourceMap;
		}
	}

	/**
	 * Return a non-null, read-only view of the given list.
	 * @param sourceList  a list, may be null
	 * @return a non-null, read-only list.
	 */
	public static <T> List<T> readOnlyList(List<T> sourceList)
	{
		return sourceList == null ? (List<T>) Collections.EMPTY_LIST : Collections.unmodifiableList(sourceList);
	}

	/**
	 * Return a non-null, read-only view of the given set.
	 * @param sourceSet  a set, may be null
	 * @return a non-null, read-only set.
	 */
	public static <T> Set<T> readOnlySet(Set<T> sourceSet)
	{
		return sourceSet == null ? (Set<T>) Collections.EMPTY_SET : Collections.unmodifiableSet(sourceSet);
	}

	/**
	 * Return a non-null, read-only view of the given map.
	 * @param sourceMap  a map, may be null
	 * @return a non-null, read-only map.
	 */
	public static <K,V> Map<K,V> readOnlyMap(Map<K,V> sourceMap)
	{
		return sourceMap == null ? (Map<K,V>) Collections.EMPTY_MAP : Collections.unmodifiableMap(sourceMap);
	}
}
