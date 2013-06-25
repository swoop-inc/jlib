package com.swoop.data.util;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A utility that monitors connection usage and automatically closes the connection after a 
 * specified period of disuse.
 */
public class ConnectionMonitor<H>
{
	public final static int DEFAULT_MAX_IDLE_MILLIS = 10 * 10000;

	private Connection<H> connection;
	private int useCount;
	private int maxIdleMillis = DEFAULT_MAX_IDLE_MILLIS;
	private Timer idleTimer;

	public ConnectionMonitor(Connection<H> connection)
	{
		this.connection = connection;
	}

	/**
	 * The maximum time a connection managed by this monitor is allowed to remain idle, before it
	 * is automatically closed.  If this value is non-positive, the connection is closed immediately
	 * when not in use.
	 */
	public int getMaxIdleMillis()
	{
		return maxIdleMillis;
	}

	public void setMaxIdleMillis(int maxIdleMillis)
	{
		this.maxIdleMillis = maxIdleMillis;
	}

	/**
	 * Increase the usage count on the connection.  Open it, if not already open.
	 * @return a typed handle for performing operations on the remote object
	 * @throws java.io.IOException if an error prevents the opening of the connection
	 */
	public synchronized H use()
		throws IOException
	{
		cancelIdleTimeout();
		H handle = connection.open();
		++useCount;
		return handle;
	}

	/**
	 * Decrease the usage count on the connection.
	 */
	public synchronized void release()
	{
		switch (useCount) {
		case 0:
			throw new IllegalStateException("useCount");
		case 1:
			--useCount;
			startIdleTimeout();
			break;
		default:
			--useCount;
		}
	}

	@Override
	public String toString()
	{
		return connection.toString();
	}

	private void startIdleTimeout()
	{
		if (maxIdleMillis > 0) {
			if (idleTimer != null) {
				throw new IllegalStateException("idleTimer");
			}
			idleTimer = new Timer();
			idleTimer.schedule(new CloseTask(), maxIdleMillis);
		}
		else {
			closeIfSafe();
		}
	}

	private void cancelIdleTimeout()
	{
		if (idleTimer != null) {
			idleTimer.cancel();
			idleTimer = null;
		}
	}

	private class CloseTask extends TimerTask
	{
		@Override
		public void run()
		{
			closeIfSafe();
		}
	};

	private synchronized void closeIfSafe()
	{
		// Double-check state, in case this current thread was context-switched-out poised ready to close. 
		if (useCount == 0 && connection.isOpen()) {
			connection.close();
		}
	}
}
