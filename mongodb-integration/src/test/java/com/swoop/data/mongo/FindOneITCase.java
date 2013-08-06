package com.swoop.data.mongo;

import com.mongodb.*;
import net.ech.util.Hash;
import org.junit.*;
import static org.junit.Assert.*;

public class FindOneITCase
	extends MongoTestCase
{
	static MongoCollectionConnector connector;

	@BeforeClass
	public static void setUp() throws Exception
	{
		connector = new MongoCollectionConnector(createDbConnector(), "stuff");

		// Some test data...
		connector.executeCommand(new InsertCommand()
			.objectId("abc")
			.objectField("a", 1)
			.objectField("b", 2)
			.objectField("c", 3));
		connector.executeCommand(new InsertCommand()
			.objectField("id", "abc")
			.objectField("a", 1)
			.objectField("b", 2)
			.objectField("c", 3));
		connector.executeCommand(new InsertCommand()
			.objectField("id", "abc")
			.objectField("a", 10)
			.objectField("b", 20)
			.objectField("c", 30));
	}

	@AfterClass
	public static void tearDown() throws Exception
	{
		if (connector != null) {
			connector.executeCommand(new DropCollectionCommand());
		}
	}

	@Test
	public void testFindOneByIdNegative() throws Exception
	{
		if (connector != null) {
			assertNull(connector.executeCommand(new DefaultFindOneCommand().queryId("lmnop")));
		}
	}

	@Test
	public void testFindOneByIdPositive() throws Exception
	{
		if (connector != null) {
			assertNotNull(connector.executeCommand(new DefaultFindOneCommand().queryId("abc")));
		}
	}

	@Test
	public void testFindOneByFieldNegative() throws Exception
	{
		if (connector != null) {
			assertNull(connector.executeCommand(new DefaultFindOneCommand().queryField("id", "lmnop")));
		}
	}

	@Test
	public void testFindOneByFieldPositive() throws Exception
	{
		if (connector != null) {
			assertNotNull(connector.executeCommand(new DefaultFindOneCommand().queryField("id", "abc")));
		}
	}

	@Test
	public void testFindOneByMultiFieldNegative() throws Exception
	{
		if (connector != null) {
			assertNull(connector.executeCommand(new DefaultFindOneCommand().queryField("id", "abc").queryField("a", 38)));
		}
	}

	@Test
	public void testFindOneByMultiFieldPositive() throws Exception
	{
		if (connector != null) {
			assertNotNull(connector.executeCommand(new DefaultFindOneCommand().queryField("id", "abc").queryField("a", 10)));
		}
	}

	@Test
	public void testFindOneByQueryObjectNegative() throws Exception
	{
		if (connector != null) {
			assertNull(connector.executeCommand(new DefaultFindOneCommand().query(new Hash().addEntry("id", "abc").addEntry("a", 38))));
		}
	}

	@Test
	public void testFindOneByQueryObjectPositive() throws Exception
	{
		if (connector != null) {
			assertNotNull(connector.executeCommand(new DefaultFindOneCommand().query(new Hash().addEntry("id", "abc").addEntry("a", 10))));
		}
	}

	@Test
	public void testIncludeField() throws Exception
	{
		if (connector != null) {
			DBObject result = connector.executeCommand(new DefaultFindOneCommand()
				.queryId("abc")
				.include("a"));
			assertNotNull(result);
			assertEquals(new Hash("_id", "abc").addEntry("a", 1), result);
		}
	}

	@Test
	public void testExcludeField() throws Exception
	{
		if (connector != null) {
			DBObject result = connector.executeCommand(new DefaultFindOneCommand()
				.queryId("abc")
				.exclude("b")
				.exclude("c"));
			assertNotNull(result);
			assertEquals(new Hash("_id", "abc").addEntry("a", 1), result);
		}
	}

	@Test
	public void testUsePostprocessorPositive() throws Exception
	{
		if (connector != null) {
			Integer result = connector.executeCommand(new DefaultFindOneCommand()
				.usePostprocessor(new Postprocessor<Integer>() {
					@SuppressWarnings("deprecation")
					@Override
					public Integer postprocess(DBObject dbo)
					{
						return dbo.containsKey("c") ? (Integer) dbo.get("c") : null;
					}
				}));
			assertNotNull(result);
		}
	}

	@Test
	public void testUsePostprocessorNegative() throws Exception
	{
		if (connector != null) {
			Integer result = connector.executeCommand(new DefaultFindOneCommand()
				.usePostprocessor(new Postprocessor<Integer>() {
					@SuppressWarnings("deprecation")
					@Override
					public Integer postprocess(DBObject dbo)
					{
						return dbo.containsKey("d") ? (Integer) dbo.get("d") : null;
					}
				}));
			assertNull(result);
		}
	}
}
