package com.swoop.data.mongo;

import com.mongodb.*;
import java.util.*;
import net.ech.util.Hash;
import org.junit.*;
import static org.junit.Assert.*;

public class FindListITCase
	extends MongoTestCase
{
	static MongoCollectionConnector connector;

	@BeforeClass
	public static void setUp() throws Exception
	{
		connector = new MongoCollectionConnector(createDbConnector(), "stuff");

		// Test data...
		connector.executeCommand(new InsertCommand()
			.objectId("abc")
			.objectField("a", 1)
			.objectField("b", 2)
			.objectField("c", 3));
		connector.executeCommand(new InsertCommand()
			.objectField("id", "abc")
			.objectField("a", 10)
			.objectField("b", 20)
			.objectField("c", 30));
		connector.executeCommand(new InsertCommand()
			.objectField("id", "abc")
			.objectField("a", 100)
			.objectField("b", 200)
			.objectField("c", 300)
			.objectField("d", 400));
	}

	@AfterClass
	public static void tearDown() throws Exception
	{
		if (connector != null) {
			connector.executeCommand(new DropCollectionCommand());
		}
	}

	@Test
	public void testFindByIdNegative() throws Exception
	{
		if (connector != null) {
			List<DBObject> result = connector.executeCommand(new DefaultFindListCommand().queryId("lmnop"));
			assertNotNull(result);
			assertTrue(result.isEmpty());
		}
	}

	@Test
	public void testFindByIdPositive() throws Exception
	{
		if (connector != null) {
			List<DBObject> result = connector.executeCommand(new DefaultFindListCommand().queryId("abc"));
			assertNotNull(result);
			assertEquals(1, result.size());
		}
	}

	@Test
	public void testFindByFieldNegative() throws Exception
	{
		if (connector != null) {
			List<DBObject> result = connector.executeCommand(new DefaultFindListCommand().queryField("id", "lmnop"));
			assertNotNull(result);
			assertTrue(result.isEmpty());
		}
	}

	@Test
	public void testFindByFieldPositive() throws Exception
	{
		if (connector != null) {
			List<DBObject> result = connector.executeCommand(new DefaultFindListCommand().queryField("id", "abc"));
			assertNotNull(result);
			assertEquals(2, result.size());
		}
	}

	@Test
	public void testFindByMultiFieldNegative() throws Exception
	{
		if (connector != null) {
			List<DBObject> result = connector.executeCommand(new DefaultFindListCommand().queryField("id", "abc").queryField("a", 38));
			assertNotNull(result);
			assertTrue(result.isEmpty());
		}
	}

	@Test
	public void testFindByMultiFieldPositive() throws Exception
	{
		if (connector != null) {
			List<DBObject> result = connector.executeCommand(new DefaultFindListCommand().queryField("id", "abc").queryField("a", 10));
			assertNotNull(result);
			assertEquals(1, result.size());
		}
	}

	@Test
	public void testFindByQueryObjectNegative() throws Exception
	{
		if (connector != null) {
			List<DBObject> result = connector.executeCommand(new DefaultFindListCommand().query(new Hash().addEntry("id", "abc").addEntry("a", 38)));
			assertNotNull(result);
			assertTrue(result.isEmpty());
		}
	}

	@Test
	public void testFindByQueryObjectPositive() throws Exception
	{
		if (connector != null) {
			List<DBObject> result = connector.executeCommand(new DefaultFindListCommand().query(new Hash().addEntry("id", "abc").addEntry("a", 10)));
			assertNotNull(result);
			assertEquals(1, result.size());
		}
	}

	@Test
	public void testIncludeField() throws Exception
	{
		if (connector != null) {
			List<DBObject> result = connector.executeCommand(new DefaultFindListCommand()
				.queryId("abc")
				.include("a"));
			assertNotNull(result);
			assertEquals(1, result.size());
			assertEquals(new Hash("_id", "abc").addEntry("a", 1), result.get(0));
		}
	}

	@Test
	public void testExcludeField() throws Exception
	{
		if (connector != null) {
			List<DBObject> result = connector.executeCommand(new DefaultFindListCommand()
				.queryId("abc")
				.exclude("b")
				.exclude("c"));
			assertNotNull(result);
			assertEquals(1, result.size());
			assertEquals(new Hash("_id", "abc").addEntry("a", 1), result.get(0));
		}
	}

	@Test
	public void testUsePostprocessor() throws Exception
	{
		if (connector != null) {
			List<Integer> result = connector.executeCommand(new DefaultFindListCommand()
				.usePostprocessor(new Postprocessor<Integer>() {
					@SuppressWarnings("deprecation")
					@Override
					public Integer postprocess(DBObject dbo)
					{
						return dbo.containsKey("d") ? (Integer) dbo.get("d") : null;
					}
				}));
			assertNotNull(result);
			assertEquals(1, result.size());
			assertEquals(new Integer(400), result.get(0));
		}
	}
}
