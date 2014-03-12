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
	private T					result		= null;
	private Exception			exception	= null;
	private volatile boolean	expired		= false;

	public T execute(int time) throws IOException
	{
		AsyncExecutor.runTask(this);
		synchronized (this) {
			if (result == null && exception == null) {
				try {
					wait(time);
					expired = true;
				}
				catch (InterruptedException e) {
				}
			}
		}
		if (exception != null) {
			if (exception instanceof IOException)
				throw (IOException)exception;
			throw (RuntimeException)exception;
		}
		return result;
	}

	public abstract T doTask() throws IOException;

	public boolean isTimedOut()
	{
		return expired;
	}

	@Override
	public void run()
	{
		// no need to synchronize here
		if (expired)
			return;

		try {
			result = doTask();
		}
		catch (IOException e) {
			exception = e;
		}
		catch (RuntimeException e) {
			exception = e;
		}
		finally {
			synchronized (this) {
				notify();
			}
		}
	}
}
