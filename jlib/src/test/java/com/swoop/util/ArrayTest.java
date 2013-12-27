package com.swoop.util;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.assertEquals;

public class ArrayTest
{
    @Test
    public void testInitiallyEmpty() throws Exception
    {
		assertEquals(0, new Array().size());
    }

	@Test
	public void testInitialEntry() throws Exception
	{
		Array array = new Array("value");
		assertEquals(1, array.size());
		assertEquals(Collections.singletonList("value"), array);
		assertEquals("value", array.get(0));
	}

	@Test
	public void testInitialCollection() throws Exception
	{
		List<String> iniList = new ArrayList<String>();
		iniList.add("Apple");
		iniList.add("Banana");
		iniList.add("Cranberry");
		Array array = new Array(iniList);
		assertEquals(iniList, array);
	}

	@Test
	public void testChainedBuild() throws Exception
	{
		List<String> expected = new ArrayList<String>();
		expected.add("Apple");
		expected.add("Banana");
		expected.add("Cranberry");
		Array array = new Array().a("Apple").a("Banana").a("Cranberry");
		assertEquals(expected, array);
	}
}
