package com.swoop.data.mongo;

import com.mongodb.*;
import net.ech.util.Hash;
import org.junit.*;
import static org.junit.Assert.*;

public class MongoCollectionClientITCase
	extends MongoTestCase
{
	private static final String COLLECTION_NAME = "collection_client_test";
	private static MongoConnector connector;

	@BeforeClass
	public static void setUpOnce() throws Exception
	{
		connector = createDbConnector();
		MongoCollectionClient client = makeClient();

		// Some test data...
		client.executeCommand(new InsertCommand()
			.objectId("abc")
			.objectField("a", 1)
			.objectField("b", 2)
			.objectField("c", 3));
		client.executeCommand(new InsertCommand()
			.objectField("id", "abc")
			.objectField("a", 1)
			.objectField("b", 2)
			.objectField("c", 3));
		client.executeCommand(new InsertCommand()
			.objectField("id", "abc")
			.objectField("a", 10)
			.objectField("b", 20)
			.objectField("c", 30));
	}

	private static MongoCollectionClient makeClient()
	{
		return new MongoCollectionClient(connector, COLLECTION_NAME);
	}

	@AfterClass
	public static void tearDownOnce() throws Exception
	{
		if (connector != null) {
			makeClient().executeCommand(new DropCollectionCommand());
		}
	}

	@Test
	public void testFindOneByIdNegative() throws Exception
	{
		if (connector != null) {
			MongoCollectionClient client = makeClient();
			assertNull(client.executeCommand(client.createDefaultFindOneCommand("lmnop")));
		}
	}

	@Test
	public void testFindOneByIdPositive() throws Exception
	{
		if (connector != null) {
			MongoCollectionClient client = makeClient();
			assertNotNull(client.executeCommand(client.createDefaultFindOneCommand("abc")));
		}
	}

	@Test
	public void testFindOneByFieldNegative() throws Exception
	{
		if (connector != null) {
			MongoCollectionClient client = makeClient();
			client.setKeyField("id");
			assertNull(client.executeCommand(client.createDefaultFindOneCommand("lmnop")));
		}
	}

	@Test
	public void testFindOneByFieldPositive() throws Exception
	{
		if (connector != null) {
			MongoCollectionClient client = makeClient();
			client.setKeyField("id");
			assertNotNull(client.executeCommand(client.createDefaultFindOneCommand("abc")));
		}
	}
}
