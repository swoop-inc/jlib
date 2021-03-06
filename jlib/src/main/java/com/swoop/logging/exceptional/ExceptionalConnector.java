package com.swoop.logging.exceptional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;

/**
 * ExceptionalConnector posts reports to the exceptional.io API.
 *
 * The default constructor (<code>new ExceptionalConnector()</code>) constructs a non-operational connector.
 * Use the builder class {@link ExceptionalConnectorBuilder} to construct an operational connector.
 */
public class ExceptionalConnector
{
	boolean enabled;	  // must be explicitly enabled.
	String urlPattern;
	String apiKey;
	ExceptionalReportFormatter reportFormatter;
	ExecutorService executorService;
	HttpClient httpClient;
	// Logging via some abstract plug-in might perpetuate an endless cycle.	 Well, there's always...
	PrintStream log;

	/**
	 * Post an exception to the exceptional.io API.
	 * @param exception	   the exception to post
	 */
	public void post(final Exception exception)
	{
		post(new ExceptionalReport(exception));
	}

	/**
	 * Post a general report to the exceptional.io API.
	 * @param report	the report to post
	 */
	public void post(final ExceptionalReport report)
	{
		if (enabled && !throttle(report)) {
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					try {
						doPost(report);
					}
					catch (Throwable e) {
						e.printStackTrace(log);
					}
				}
			});
		}
	}

	// TODO: enable throttling.
	private boolean throttle(final ExceptionalReport report)
	{
		return false;
	}

	private void doPost(final ExceptionalReport report)
		throws IOException
	{
		final String url = String.format(urlPattern, apiKey, ExceptionalClient.PROTOCOL_VERSION);
		log.println("POST report to " + url);
		final HttpPost post = new HttpPost(url);
		post.setHeader(HttpHeaders.CONTENT_ENCODING, "gzip");
		post.setEntity(new ByteArrayEntity(toByteArray(report)));
		try {
			final HttpResponse response = httpClient.execute(post);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode < 200 || statusCode > 299) {
				logErrorResponse(statusCode, response);
			}
			else {
				// We always have to read the body.
				EntityUtils.consume(response.getEntity());
			}
		}
		finally {
			// post.reset();   // requires httpclient 4.3
		}
	}

	private byte[] toByteArray(ExceptionalReport report)
		throws IOException
	{
		final ByteArrayOutputStream outbuf = new ByteArrayOutputStream();
		final GZIPOutputStream gzip = new GZIPOutputStream(outbuf);
		try {
			gzip.write(reportFormatter.format(report).getBytes("UTF-8"));
		}
		finally {
			IOUtils.closeQuietly(gzip);
		}
		return outbuf.toByteArray();
	}

	private void logErrorResponse(final int statusCode, final HttpResponse response)
		throws IOException
	{
		log.println("ERROR " + statusCode);
		log.println(IOUtils.toString(response.getEntity().getContent()));
		for (Header header : response.getAllHeaders()) {
			log.println(header);
		}
	}
}
