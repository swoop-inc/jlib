package com.swoop.logging.exceptional;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Tests exceptional report formatting.
 */
public class ExceptionalReportFormatterTest
{
    @Test
	public void testContents()
	{
		String report = new ExceptionalReportFormatter().format(new ExceptionalReportBuilder("omg").build());
		assertTrue("application_environment found", report.indexOf("\"application_environment\":{") > 0);
		assertTrue("language_version found", report.indexOf("\"language_version\":\"") > 0);
		assertTrue("language found", report.indexOf("\"language\":\"java\"") > 0);
		assertTrue("exception found", report.indexOf("\"exception\":{") > 0);
		assertTrue("occurred_at found", report.indexOf("\"occurred_at\":\"") > 0);
		assertTrue("env found", report.indexOf("\"env\":{") > 0);
		assertTrue("client found", report.indexOf("\"client\":{\"") > 0);
		assertTrue("request found", report.indexOf("\"request\":{") > 0);
    }

    @Test
	public void testEnvExclusion()
	{
		String report = new ExceptionalReportFormatter().format(new ExceptionalReportBuilder("omg").build());
		assertTrue("USER env var found", report.indexOf("\"USER\":\"") > 0);
		report = new ExceptionalReportFormatter().excludeFromEnv("USER").format(new ExceptionalReportBuilder("omg").build());
		assertTrue("USER env var not found", report.indexOf("\"USER\":\"") < 0);
    }
}
