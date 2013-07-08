package com.swoop.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.swoop.util.AbstractIterator;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class AbstractIteratorTest
{
	Iterator<String> iter;

	@Before
	public void setUp()
	{
		iter = new AbstractIterator<String>() {
			int state = 0;
			@Override
			protected String advance()
			{
				switch (state++) {
				case 0: return "abc";
				case 1: return "123";
				default: return null;
				}
			}
		};
	}

	@Test
	public void testNormalIteration() throws Exception
	{
		String result = "";
		while (iter.hasNext()) {
			result += iter.next();
		}
		assertEquals("abc123", result);
	}

	@Test
	public void testNoSuchElement() throws Exception
	{
		while (iter.hasNext()) {
			iter.next();
		}
		try {
			iter.next();
			fail("should not be reached");
		}
		catch (NoSuchElementException e) {
		}
	}

	@Test
	public void testRemoveIsUnsupported() throws Exception
	{
		iter.next();
		try {
			iter.remove();
			fail("should not be reached");
		}
		catch (UnsupportedOperationException e) {
		}
	}
}
