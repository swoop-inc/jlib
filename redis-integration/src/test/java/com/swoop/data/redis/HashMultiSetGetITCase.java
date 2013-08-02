package com.swoop.data.redis;

import com.swoop.util.ExecGate;
import java.io.IOException;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class HashMultiSetGetITCase
	extends RedisTestCase
{
	private final static int CONCURRENCY = 20;
	private final static int DATA_SIZE = 100;

	static RedisConnector connector;

	@BeforeClass
	public static void setUp() throws Exception
	{
		connector = createRedisConnector();

		// Test data...
		connector.execute(new HashMultiSetRedisCommand() {
			@Override
			protected byte[] getBinaryKey()
			{
				return HashMultiSetGetITCase.getBinaryKey();
			}

			@Override
			protected Map<byte[],byte[]> getBinaryFieldValueMap() {
				Map<byte[],byte[]> map = new HashMap<byte[],byte[]>();
				for (int i = 0; i < DATA_SIZE; ++i) {
					if (i % 3 != 0) {
						map.put(("k" + i).getBytes(), ("v" + i).getBytes());
					}
				}
				return map;
			}
		});
	}

	@Test
	public void testHashMultiGet() throws Exception
	{
		if (connector != null) {
			
			List<String> result = connector.execute(new HashMultiGetRedisCommand<List<String>>() {
				@Override
				protected byte[] getBinaryKey() {
					return HashMultiSetGetITCase.getBinaryKey();
				}

				@Override
				protected byte[][] getBinaryFields() {
					byte[][] fields = new byte[DATA_SIZE][];
					for (int i = 0; i < fields.length; ++i) {
						fields[i] = ("k" + i).getBytes();
					}
					return fields;
				}

				@Override
				protected List<String> postprocess(List<byte[]> results) {
					List<String> strResults = new ArrayList<String>();
					for (int i = 0; i < results.size(); ++i) {
						byte[] result = results.get(i);
						strResults.add(result == null ? "(null)" : new String(result));
					}
					return strResults;
				}
			});

			assertNotNull(result);
			assertEquals(DATA_SIZE, result.size());
			for (int i = 0; i < result.size(); ++i) {
				String str = result.get(i);
				if (i % 3 == 0) {
					assertEquals("(null)", str);
				}
				else {
					assertEquals("v" + i, str);
				}
			}
		}
	}

	private int successCount;

	@Test
	public void testConcurrentHashMultiGet() throws Exception
	{
		final ExecGate gate = new ExecGate();
		successCount = 0;
		for (int i = 0; i < CONCURRENCY; ++i) {
			gate.lock();
			new Thread() {
				@Override
				public void run()
				{
					try {
						testHashMultiGet();
						++successCount;
					}
					catch (Exception e) {
						e.printStackTrace(System.err);
					}
					finally {
						gate.unlock();
					}
				}
			}.start();
		}
		gate.cross();
		assertEquals(CONCURRENCY, successCount);
	}

	private static byte[] getBinaryKey()
	{
		return "HashMultiSetGetITCase".getBytes();
	}
}
