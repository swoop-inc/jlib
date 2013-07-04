package com.swoop.data.mongo;

import com.mongodb.*;
import java.io.IOException;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MongoConnectorITCase
{
	MongoCollectionConnector connector;

	@Before
	public void setUp() throws Exception
	{
		MongoConnector dbConnector = new DefaultMongoConnector("mongodb://localhost:27018/test");
		connector = new DefaultMongoCollectionConnector(dbConnector, "stuff");
		connector.executeCommand(new InsertCommand() {
			@Override
			protected DBObject getObject() {
				return new BasicDBObject("_id", "abc");
			}
		});
	}

	@After
	public void tearDown() throws Exception
	{
		if (connector != null) {
			connector.executeCommand(new DropCollectionCommand());
		}
	}

	@Test
	public void testFindOneById() throws Exception
	{
		if (connector != null) {
			Object result = connector.executeCommand(new FindOneByIdCommand("abc"));
			assertNotNull(result);
		}
	}
}
