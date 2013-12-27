package com.swoop.logging.exceptional;

import java.io.PrintStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Sends reports to the exceptional.io API.
 */
public class ExceptionalConnectorBuilder
{
	public final static String DEFAULT_URL_PATTERN = 
		"https://www.exceptional.io/api/errors?api_key=%s&protocol_version=%s";

	private String urlPattern = DEFAULT_URL_PATTERN;
	private String apiKey;
	private ExceptionalReportFormatter reportFormatter;
    private ExecutorService executorService;
    private HttpClient httpClient;
	private PrintStream log;

	public ExceptionalConnectorBuilder setUrlPattern(String urlPattern)
	{
		this.urlPattern = urlPattern;
		return this;
	}

	public ExceptionalConnectorBuilder setApiKey(String apiKey)
	{
		this.apiKey = apiKey;
		return this;
	}

	public ExceptionalConnectorBuilder setReportFormatter(ExceptionalReportFormatter reportFormatter)
	{
		this.reportFormatter = reportFormatter;
		return this;
	}

	public ExceptionalConnectorBuilder setExecutorService(ExecutorService executorService)
	{
		this.executorService = executorService;
		return this;
	}

	public ExceptionalConnectorBuilder setHttpClient(HttpClient httpClient)
	{
		this.httpClient = httpClient;
		return this;
	}

	public ExceptionalConnectorBuilder setLog(PrintStream log)
	{
		this.log = log;
		return this;
	}

    public ExceptionalConnector build()
	{
		if (apiKey == null) {
			throw new IllegalStateException("apiKey unset");
		}
		ExceptionalConnector connector = new ExceptionalConnector();
		connector.enabled = true;
		connector.urlPattern = urlPattern;
		connector.apiKey = apiKey;
		connector.reportFormatter = reportFormatter == null ? new ExceptionalReportFormatter() : reportFormatter;
		connector.executorService = executorService == null ? Executors.newSingleThreadExecutor() : executorService;
		connector.httpClient = httpClient == null ? HttpClientBuilder.create().build() : httpClient;
		connector.log = log == null ? System.err : log;
		return connector;
    }
}
