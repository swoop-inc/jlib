package com.swoop.util;

import java.io.IOException;

/**
 * Abstract TimedTask
 * 
 * Execute a task with a timeout.
 * 
 * @author ychen
 * 
 * @param <T>
 */
public abstract class TimedTask<T> implements Runnable
{
	private T			result		= null;
	private IOException	exception	= null;

	public T execute(int time) throws IOException
	{
		AsyncExecutor.runTask(this);
		synchronized (this) {
			if (result == null && exception == null) {
				try {
					wait(time);
				}
				catch (InterruptedException e) {
				}
			}
		}
		if (exception != null)
			throw exception;
		return result;
	}

	public abstract T doTask() throws IOException;

	@Override
	public void run()
	{
		try {
			result = doTask();
		}
		catch (IOException e) {
			exception = e;
		}
		synchronized (this) {
			notify();
		}
	}
}
