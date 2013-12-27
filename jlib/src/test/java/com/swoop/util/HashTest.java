package com.swoop.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.assertEquals;

public class HashTest
{
    @Test
    public void testInitiallyEmpty() throws Exception
    {
		assertEquals(0, new Hash().size());
    }

	@Test
	public void testInitialEntry() throws Exception
	{
		Hash hash = new Hash("key", "value");
		assertEquals(1, hash.size());
		assertEquals(Collections.singleton("key"), hash.keySet());
		assertEquals("value", hash.get("key"));
	}

	@Test
	public void testInitialMap() throws Exception
	{
		Map<String,String> iniMap = new HashMap<String,String>();
		iniMap.put("a", "Apple");
		iniMap.put("b", "Banana");
		iniMap.put("c", "Cranberry");
		Hash hash = new Hash(iniMap);
		assertEquals(iniMap, hash);
	}

	@Test
	public void testChainedBuild() throws Exception
	{
		Map<String,String> expected = new HashMap<String,String>();
		expected.put("a", "Apple");
		expected.put("b", "Banana");
		expected.put("c", "Cranberry");
		Hash hash = new Hash().a("a", "Apple").a("b", "Banana").a("c", "Cranberry");
		assertEquals(expected, hash);
	}
}
