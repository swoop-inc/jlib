package com.swoop.data.mongo;

import org.junit.*;
import static org.junit.Assert.*;

public class DefaultMongoConfiguratorTest
{
	DefaultMongoConfigurator configurator;

	@Before
	public void setUp() throws Exception
	{
		configurator = new DefaultMongoConfigurator();
	}

	@Test
	public void testAllDefaultConnectorsAreTheSame() throws Exception
	{
		assertTrue(configurator.createConnector("a") == configurator.createConnector("b"));
	}

	@Test
	public void testInstalledConnectorIsNotDefaultConnector() throws Exception
	{
		configurator.addEntry("a", "mongodb://example");
		assertTrue(configurator.createConnector("a") != configurator.createConnector("b"));
	}

	@Test
	public void testAllInstalledConnectorsOfSameUriAreTheSame() throws Exception
	{
		configurator.addEntry("a", "mongodb://example");
		configurator.addEntry("b", "mongodb://example");
		assertTrue(configurator.createConnector("a") == configurator.createConnector("b"));
	}

	@Test
	public void testInstalledConnectorsOfDifferentUrisAreDifferent() throws Exception
	{
		configurator.addEntry("a", "mongodb://example");
		configurator.addEntry("b", "mongodb://Example");
		assertTrue(configurator.createConnector("b") != configurator.createConnector("a"));
	}

	@Test
	public void testConnectorType() throws Exception
	{
		configurator.addEntry("a", "mongodb://example");
		assertTrue(configurator.createConnector("a") instanceof DefaultMongoConnector);
		assertTrue(configurator.createConnector("b") instanceof DefaultMongoConnector);
	}
}
