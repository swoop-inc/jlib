package com.swoop.data.redis;

import java.io.IOException;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MultiSetGetITCase
	extends RedisTestCase
{
	static RedisConnector connector;

	@BeforeClass
	public static void setUp() throws Exception
	{
		connector = createRedisConnector();

		// Test data...
		connector.execute(new MultiSetRedisCommand() {
			@Override
			protected byte[][] getBinaryKeysAndValues() {
				byte[][] knv = new byte[100*2/3 * 2][];
				int knvx = 0;
				for (int i = 0; i < 100; ++i) {
					if (i % 3 != 0) {
						knv[knvx++] = makeKey(i);
						knv[knvx++] = makeValue(i);
					}
				}
				assertEquals(knv.length, knvx);
				return knv;
			}
		});
	}

	@Test
	public void testMultiGet() throws Exception
	{
		if (connector != null) {
			
			List<String> result = connector.execute(new MultiGetRedisCommand<List<String>>() {
				@Override
				protected byte[][] getBinaryKeys() {
					byte[][] binkeys = new byte[100][];
					for (int i = 0; i < 100; ++i) {
						binkeys[i] = makeKey(i);
					}
					return binkeys;
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
			assertEquals(100, result.size());
			for (int i = 0; i < result.size(); ++i) {
				String str = result.get(i);
				if (i % 3 == 0) {
					assertEquals("(null)", str);
				}
				else {
					assertEquals(Integer.toString(i), str);
				}
			}
		}
	}

	private static byte[] makeKey(int index)
	{
		return ("test: " + index).getBytes();
	}

	private static byte[] makeValue(int index)
	{
		return Integer.toString(index).getBytes();
	}
}
