package com.swoop.data.mongo;

import com.mongodb.*;
import net.ech.util.Hash;
import org.junit.*;
import static org.junit.Assert.*;

public class UpdateITCase
	extends MongoTestCase
{
	static MongoCollectionConnector connector;

	@BeforeClass
	public static void setUp() throws Exception
	{
		connector = new DefaultMongoCollectionConnector(createDbConnector(), "stuff");

		// Test data...
		connector.executeCommand(new InsertCommand()
			.objectId("abc")
			.objectField("a", 1)
			.objectField("b", 2)
			.objectField("c", 3));
		connector.executeCommand(new InsertCommand()
			.objectId("def")
			.objectField("id", "abc")
			.objectField("a", 1)
			.objectField("b", 2)
			.objectField("c", 3));
		connector.executeCommand(new InsertCommand()
			.objectId("ghi")
			.objectField("id", "abc")
			.objectField("a", 10)
			.objectField("b", 20)
			.objectField("c", 30));

		assertEquals(2, connector.executeCommand(new DefaultFindListCommand().queryField("id", "abc")).size());
	}

	@AfterClass
	public static void tearDown() throws Exception
	{
		if (connector != null) {
			connector.executeCommand(new DropCollectionCommand());
		}
	}

	@Test
	public void testQueryIdNegative() throws Exception
	{
		if (connector != null) {
			assertEquals(0, connector.executeCommand(new UpdateCommand().queryId("lmnop")).getN());
		assertEquals(2, connector.executeCommand(new DefaultFindListCommand().queryField("id", "abc")).size());
		}
	}

	@Test
	public void testQueryIdPositive() throws Exception
	{
		if (connector != null) {
			assertEquals(1, connector.executeCommand(new UpdateCommand().queryId("abc")).getN());
		assertEquals(2, connector.executeCommand(new DefaultFindListCommand().queryField("id", "abc")).size());
		}
	}

	@Test
	public void testQueryFieldNegative() throws Exception
	{
		if (connector != null) {
			assertEquals(0, connector.executeCommand(new UpdateCommand().queryField("id", "lmnop").setObjectField("d", 4)).getN());
		}
	}

	@Test
	public void testQueryFieldPositive() throws Exception
	{
		if (connector != null) {
			assertEquals(1, connector.executeCommand(new UpdateCommand().queryField("id", "abc").setObjectField("d", 4)).getN());
		}
	}

	@Test
	public void testUpdateByObjectNegative() throws Exception
	{
		if (connector != null) {
			assertEquals(0, connector.executeCommand(new UpdateCommand()
				.query(new Hash().addEntry("id", "abc").addEntry("a", 38))
				.setObjectField("d", 4)
			).getN());
		}
	}

	@Test
	public void testUpdateByObjectPositive() throws Exception
	{
		if (connector != null) {
			assertEquals(1, connector.executeCommand(new UpdateCommand()
				.query(new Hash().addEntry("id", "abc").addEntry("a", 10))
				.setObjectField("d", 4)
			).getN());
		}
	}

	@Test
	public void testUpdateMultiNegative() throws Exception
	{
		if (connector != null) {
			assertEquals(1, connector.executeCommand(new UpdateCommand()
				.query(new Hash().addEntry("id", "abc"))
				.setObjectField("c", 142)
			).getN());
		}
	}

	@Test
	public void testUpdateMultiPositive() throws Exception
	{
		final int NEWVAL = 333;
		if (connector != null) {
			assertEquals(2, connector.executeCommand(new DefaultFindListCommand().queryField("id", "abc")).size());
			assertEquals(2, connector.executeCommand(new UpdateCommand().multi().queryField("id", "abc").setObjectField("c", NEWVAL)).getN());
			for (DBObject dbo : connector.executeCommand(new DefaultFindListCommand().queryField("id", "abc"))) {
				assertEquals(NEWVAL, dbo.get("c"));
			}
		}
	}
}
