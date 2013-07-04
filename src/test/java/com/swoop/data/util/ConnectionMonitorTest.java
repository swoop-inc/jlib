package com.swoop.data.util;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ConnectionMonitorTest
{
	@Test
	public void testFirstUseOpensConnection() throws Exception
	{
		Connection connection = new DummyConnection();
		ConnectionMonitor<Handle> monitor = new ConnectionMonitor<Handle>(connection);
		monitor.use();
		assertTrue(connection.isOpen());
	}

	@Test
	public void testUseReturnsHandle() throws Exception
	{
		ConnectionMonitor<Handle> monitor = new ConnectionMonitor<Handle>(new DummyConnection());
		assertNotNull(monitor.use());
	}

	@Test
	public void testSecondUseDoesNotReopen() throws Exception
	{
		ConnectionMonitor<Handle> monitor = new ConnectionMonitor<Handle>(new DummyConnection());
		Handle h1 = monitor.use();
		for (int i = 0; i < 10; ++i) {
			assertEquals(h1, monitor.use());
		}
	}

	@Test
	public void testDefaultMaxIdleMillis() throws Exception
	{
		ConnectionMonitor<Handle> monitor = new ConnectionMonitor<Handle>(new DummyConnection());
		assertEquals(ConnectionMonitor.DEFAULT_MAX_IDLE_MILLIS, monitor.getMaxIdleMillis());
	}

	@Test
	public void testSetGetMaxIdleMillis() throws Exception
	{
		final int TEST_VALUE = 8686;
		ConnectionMonitor<Handle> monitor = new ConnectionMonitor<Handle>(new DummyConnection());
		monitor.setMaxIdleMillis(TEST_VALUE);
		assertEquals(TEST_VALUE, monitor.getMaxIdleMillis());
	}

	@Test
	public void testAutoClose() throws Exception
	{
		Connection connection = new DummyConnection();
		ConnectionMonitor<Handle> monitor = new ConnectionMonitor<Handle>(connection);
		monitor.use();
		monitor.setMaxIdleMillis(1);
		assertTrue(connection.isOpen());
		monitor.release();
		Thread.sleep(20);
		assertFalse(connection.isOpen());
	}

	@Test
	public void testKeepAlive() throws Exception
	{
		Connection connection = new DummyConnection();
		ConnectionMonitor<Handle> monitor = new ConnectionMonitor<Handle>(connection);
		monitor.setMaxIdleMillis(1000);
		for (int i = 0; i < 100; ++i) {
			monitor.use();
			assertTrue(connection.isOpen());
			monitor.release();
			Thread.yield();
			assertTrue(connection.isOpen());
		}
	}

	private static class DummyConnection implements Connection<Handle>
	{
		Handle handle;

		@Override
		public boolean isOpen()
		{
			return handle != null;
		}

		@Override
		public Handle open()
		{
			if (handle == null) {
				handle = new Handle();
			}
			return handle;
		}

		@Override
		public void close()
		{
			handle = null;
		}
	}

	private static class Handle {}
}
