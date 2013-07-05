package com.swoop.data.mongo;

import com.mongodb.*;
import java.io.IOException;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class FindITCase
{
	static final int MONGOD_PORT = 27018;   // must match that in pom.xml
	static final String DB_NAME = "test";
	static final String COLLECTION_NAME = "stuff";

	static MongoCollectionConnector connector;

	@BeforeClass
	public static void setUp() throws Exception
	{
		String connectionString = "mongodb://localhost:" + MONGOD_PORT + "/" + DB_NAME;
		MongoConnector dbConnector = new DefaultMongoConnector(connectionString);
		connector = new DefaultMongoCollectionConnector(dbConnector, COLLECTION_NAME);
		connector.executeCommand(new InsertCommand()
			.withObjectId("abc")
			.withObjectField("a", 1)
			.withObjectField("b", 2)
			.withObjectField("c", 3));
		connector.executeCommand(new InsertCommand()
			.withObjectField("id", "abc")
			.withObjectField("a", 1)
			.withObjectField("b", 2)
			.withObjectField("c", 3));
		connector.executeCommand(new InsertCommand()
			.withObjectField("id", "abc")
			.withObjectField("a", 10)
			.withObjectField("b", 20)
			.withObjectField("c", 30));
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
			MongoCollectionCommand<List<DBObject>> cmd = new FindCommand()
				.withQueryId("lmnop");
			List<DBObject> result = connector.executeCommand(cmd);
			assertNotNull(result);
			assertTrue(result.isEmpty());
		}
	}

	@Test
	public void testFindByIdPositive() throws Exception
	{
		if (connector != null) {
			MongoCollectionCommand<List<DBObject>> cmd = new FindCommand()
				.withQueryId("abc");
			List<DBObject> result = connector.executeCommand(cmd);
			assertNotNull(result);
			assertEquals(1, result.size());
		}
	}

	@Test
	public void testFindByFieldNegative() throws Exception
	{
		if (connector != null) {
			MongoCollectionCommand<List<DBObject>> cmd = new FindCommand()
				.withQueryField("id", "lmnop");
			List<DBObject> result = connector.executeCommand(cmd);
			assertNotNull(result);
			assertTrue(result.isEmpty());
		}
	}

	@Test
	public void testFindByFieldPositive() throws Exception
	{
		if (connector != null) {
			MongoCollectionCommand<List<DBObject>> cmd = new FindCommand()
				.withQueryField("id", "abc");
			List<DBObject> result = connector.executeCommand(cmd);
			assertNotNull(result);
			assertEquals(2, result.size());
		}
	}

	@Test
	public void testFindOneByIdNegative() throws Exception
	{
		if (connector != null) {
			MongoCollectionCommand<DBObject> cmd = new FindCommand()
				.withQueryId("lmnop")
				.expectOne();
			DBObject result = connector.executeCommand(cmd);
			assertNull(result);
		}
	}

	@Test
	public void testFindOneByIdPositive() throws Exception
	{
		if (connector != null) {
			MongoCollectionCommand<DBObject> cmd = new FindCommand()
				.withQueryId("abc")
				.expectOne();
			DBObject result = connector.executeCommand(cmd);
			assertNotNull(result);
		}
	}

	@Test
	public void testFindOneByFieldNegative() throws Exception
	{
		if (connector != null) {
			MongoCollectionCommand<DBObject> cmd = new FindCommand()
				.withQueryField("id", "lmnop")
				.expectOne();
			DBObject result = connector.executeCommand(cmd);
			assertNull(result);
		}
	}

	@Test
	public void testFindOneByFieldPositive() throws Exception
	{
		if (connector != null) {
			MongoCollectionCommand<DBObject> cmd = new FindCommand()
				.withQueryField("id", "abc")
				.expectOne();
			DBObject result = connector.executeCommand(cmd);
			assertNotNull(result);
		}
	}
}
