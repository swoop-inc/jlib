package com.swoop.data.util;

/**
 * A generic Connection type, client of the ConnectionMonitor utility.
 * @param H  the internal handle type returned from the open() method
 */
public interface Connection<H>
{
	/**
	 * Query for the open state of this connection.
	 */
	public boolean isOpen();

	/**
	 * Open this connection, if not already open.  Return an object of the handle type.
	 * @return a typed handle for performing operations on the remote object
	 * @throws java.io.IOException if an error prevents the opening of the connection
	 */
	public H open()
		throws java.io.IOException;

	/**
	 * Close this connection, if not already closed.
	 */
	public void close();
}
