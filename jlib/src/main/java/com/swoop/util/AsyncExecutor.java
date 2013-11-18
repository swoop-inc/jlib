package com.swoop.util;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Asynchronous executor
 * 
 * @author ychen
 * 
 */
public class AsyncExecutor
{
	private static final int					THREAD_POOL_SIZE	= 100;

	private static ScheduledThreadPoolExecutor	s_Executor			= new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE);

	/**
	 * Run the given task asynchronously
	 * 
	 * @param task
	 */
	public static synchronized void runTask(final Runnable task)
	{
		s_Executor.execute(task);
	}

	/**
	 * Schedule an asynchronous task for the given delay
	 * 
	 * @param task
	 * @param delay
	 * @param unit
	 * @return
	 */
	public static ScheduledFuture<?> schedule(final Runnable task, final int delay, final TimeUnit unit)
	{
		return s_Executor.schedule(task, delay, unit);
	}
}
