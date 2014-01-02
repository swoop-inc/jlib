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
		assertEquals("omg", report.getErrors()[0].getMessage());
		assertNull(report.getLogLevel());
		assertEquals("testByMessage", report.getErrors()[0].getMethodName());
		assertTrue("legit line number", Integer.parseInt(report.getErrors()[0].getLineNumber()) > 10);
		assertNotNull(report.getThreadName());
		assertNull(report.getErrors()[0].getExceptionClass());
		assertNotNull("non-null backtrace", report.getErrors()[0].getBacktrace());
		assertTrue("non-empty backtrace", report.getErrors()[0].getBacktrace().length > 0);
    }

	@Test
	public void testByException()
	{
		try {
			throw new ArrayIndexOutOfBoundsException("hooray");
		}
		catch (ArrayIndexOutOfBoundsException e) {
			ExceptionalReport report = new ExceptionalReportBuilder(e).build();
			assertEquals("hooray", report.getErrors()[0].getMessage());
			assertNull(report.getLogLevel());
			assertEquals("testByException", report.getErrors()[0].getMethodName());
			assertTrue("legit line number", Integer.parseInt(report.getErrors()[0].getLineNumber()) > 20);
			assertNotNull(report.getThreadName());
			assertEquals("java.lang.ArrayIndexOutOfBoundsException", report.getErrors()[0].getExceptionClass());
			assertNotNull("non-null backtrace", report.getErrors()[0].getBacktrace());
			assertTrue("non-empty backtrace", report.getErrors()[0].getBacktrace().length > 0);
		}
	}

	@Test
	public void testByChainedException()
	{
		try {
			throw new ArrayIndexOutOfBoundsException("hooray");
		}
		catch (ArrayIndexOutOfBoundsException e) {
			try {
				throw new IllegalStateException("oh no", e);
			}
			catch (IllegalStateException e2) {
				ExceptionalReport report = new ExceptionalReportBuilder(e2).build();
				assertNull(report.getLogLevel());
				assertNotNull(report.getThreadName());

				assertEquals("oh no", report.getErrors()[0].getMessage());
				assertEquals("testByChainedException", report.getErrors()[0].getMethodName());
				assertTrue("legit line number", Integer.parseInt(report.getErrors()[0].getLineNumber()) > 30);
				assertEquals("java.lang.IllegalStateException", report.getErrors()[0].getExceptionClass());
				assertNotNull("non-null backtrace", report.getErrors()[0].getBacktrace());
				assertTrue("non-empty backtrace", report.getErrors()[0].getBacktrace().length > 0);

				assertEquals("hooray", report.getErrors()[1].getMessage());
				assertEquals("testByChainedException", report.getErrors()[1].getMethodName());
				assertTrue("legit line number", Integer.parseInt(report.getErrors()[1].getLineNumber()) > 30);
				assertEquals("java.lang.ArrayIndexOutOfBoundsException", report.getErrors()[1].getExceptionClass());
				assertNotNull("non-null backtrace", report.getErrors()[1].getBacktrace());
				assertTrue("non-empty backtrace", report.getErrors()[1].getBacktrace().length > 0);
			}
		}
	}

	@Test
	public void testLogLevel()
	{
		ExceptionalReport report = new ExceptionalReportBuilder("Nope!").setLogLevel("ERROR").build();
		assertEquals("Nope!", report.getErrors()[0].getMessage());
		assertEquals("ERROR", report.getLogLevel());
		assertEquals("testLogLevel", report.getErrors()[0].getMethodName());
	}
}
