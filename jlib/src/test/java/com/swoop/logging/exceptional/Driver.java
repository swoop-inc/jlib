package com.swoop.logging.exceptional;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Driver
{
	ExceptionalConnector connector;

	public static void main(String[] args)
	{
		Driver driver = new Driver();
		driver.postException();
		driver.postMessage();
	}

	public Driver()
	{
		this.connector = new ExceptionalConnectorBuilder()
			.setApiKey(System.getProperty("exceptional.api.key"))
			.setExecutorService(Executors.newSingleThreadExecutor())
			.setReportFormatter(new ExceptionalReportFormatter())
			.setLog(System.out)
			.build();
	}

	private void postException()
	{
		try {
			System.out.println((new Object[0])[1]);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			connector.post(new Exception(e));
		}
	}

	private void postMessage()
	{
		connector.post(new ExceptionalReport("this is a test"));
	}
}
