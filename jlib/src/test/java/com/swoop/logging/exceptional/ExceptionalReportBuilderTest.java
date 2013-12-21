package com.swoop.logging.exceptional;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Tests exceptional report building.
 */
public class ExceptionalReportBuilderTest
{
    @Test
	public void testByMessage()
	{
		ExceptionalReport report = new ExceptionalReportBuilder("omg").build();
		assertEquals("omg", report.getMessage());
		assertNull(report.getLogLevel());
		assertEquals("testByMessage", report.getMethodName());
		assertTrue("legit line number", Integer.parseInt(report.getLineNumber()) > 10);
		assertNotNull(report.getThreadName());
		assertNull(report.getExceptionClass());
		assertNotNull("non-null backtrace", report.getBacktrace());
		assertTrue("non-empty backtrace", report.getBacktrace().length > 0);
    }

	@Test
	public void testByException()
	{
		try {
			throw new ArrayIndexOutOfBoundsException("hooray");
		}
		catch (ArrayIndexOutOfBoundsException e) {
			ExceptionalReport report = new ExceptionalReportBuilder(e).build();
			assertEquals("hooray", report.getMessage());
			assertNull(report.getLogLevel());
			assertEquals("testByException", report.getMethodName());
			assertTrue("legit line number", Integer.parseInt(report.getLineNumber()) > 20);
			assertNotNull(report.getThreadName());
			assertEquals("java.lang.ArrayIndexOutOfBoundsException", report.getExceptionClass());
			assertNotNull("non-null backtrace", report.getBacktrace());
			assertTrue("non-empty backtrace", report.getBacktrace().length > 0);
		}
	}

	@Test
	public void testLogLevel()
	{
		ExceptionalReport report = new ExceptionalReportBuilder("Nope!").setLogLevel("ERROR").build();
		assertEquals("Nope!", report.getMessage());
		assertEquals("ERROR", report.getLogLevel());
		assertEquals("testLogLevel", report.getMethodName());
	}
}
