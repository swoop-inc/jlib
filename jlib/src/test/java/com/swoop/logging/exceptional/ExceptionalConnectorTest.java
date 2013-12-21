package com.swoop.logging.exceptional;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Tests exceptional connector construction.
 */
public class ExceptionalConnectorTest
{
    @Test
	public void testNoApiKey()
	{
		try {
			new ExceptionalConnectorBuilder().build();
			fail("should not be reached");
		}
		catch (IllegalStateException e) {
			// Expected.
		}
    }

	@Test
	public void testMinimal()
	{
		ExceptionalConnector connector = new ExceptionalConnectorBuilder().setApiKey("abc").build();
		assertEquals(ExceptionalConnectorBuilder.DEFAULT_URL_PATTERN, connector.urlPattern);
		assertEquals("abc", connector.apiKey);
		assertNotNull(connector.reportFormatter);
		assertNotNull(connector.executorService);
		assertNotNull(connector.httpClient);
		assertNotNull(connector.log);
	}

	@Test
	public void testSetUrlPattern()
	{
		ExceptionalConnector connector = new ExceptionalConnectorBuilder()
			.setApiKey("abc").setUrlPattern("http://nope.com").build();
		assertEquals("http://nope.com", connector.urlPattern);
	}

	@Test
	public void testSetReportFormatter()
	{
		ExceptionalReportFormatter reportFormatter = new ExceptionalReportFormatter();
		ExceptionalConnector connector = new ExceptionalConnectorBuilder()
			.setApiKey("abc").setReportFormatter(reportFormatter).build();
		assertTrue("same report formatter", reportFormatter == connector.reportFormatter);
	}

	@Test
	public void testSetExecutorService()
	{
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		ExceptionalConnector connector = new ExceptionalConnectorBuilder()
			.setApiKey("abc").setExecutorService(executorService).build();
		assertTrue("same executor service", executorService == connector.executorService);
	}

	@Test
	public void testSetHttpClient()
	{
		HttpClient httpClient = HttpClientBuilder.create().build();
		ExceptionalConnector connector = new ExceptionalConnectorBuilder()
			.setApiKey("abc").setHttpClient(httpClient).build();
		assertTrue("same http client", httpClient == connector.httpClient);
	}

	@Test
	public void testSetLog()
	{
		ExceptionalConnector connector = new ExceptionalConnectorBuilder()
			.setApiKey("abc").setLog(System.out).build();
		assertTrue("same log", System.out == connector.log);
	}
}
