package com.swoop.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.swoop.util.BuilderHelper;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BuilderHelperTest
{
	@Test
	public void testCreateLazyList() throws Exception
	{
		List<Object> list1 = BuilderHelper.createLazyList((List<Object>) null, false);
		assertNotNull(list1);
		assertTrue(list1 == BuilderHelper.createLazyList(list1, false));
		assertTrue(list1 != BuilderHelper.createLazyList(list1, true));
	}

	@Test
	public void testCreateLazySet() throws Exception
	{
		Set<Object> set1 = BuilderHelper.createLazySet((Set<Object>) null, false);
		assertNotNull(set1);
		assertTrue(set1 == BuilderHelper.createLazySet(set1, false));
		assertTrue(set1 != BuilderHelper.createLazySet(set1, true));
	}

	@Test
	public void testCreateLazyMap() throws Exception
	{
		Map<Object,Object> map1 = BuilderHelper.createLazyMap((Map<Object,Object>) null, false);
		assertNotNull(map1);
		assertTrue(map1 == BuilderHelper.createLazyMap(map1, false));
		assertTrue(map1 != BuilderHelper.createLazyMap(map1, true));
	}
}
