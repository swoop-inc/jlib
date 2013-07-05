package com.swoop.data.mongo;

import com.mongodb.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class FindListCommandTest
{
	MongoConnector connector = new MockMongoConnector();

	@Test
	public void testPostProcess() throws Exception
	{
		List<Object> list = connector.executeCollectionCommand("coll", new DefaultFindListCommand().usePostprocessor(new FieldPicker("v")));
		assertEquals(1, list.size());
		assertEquals("abc", list.get(0));
	}

	private static class MockMongoConnector extends DefaultMongoConnector
	{
		@Override
		public <T> T executeDbCommand(MongoDbCommand<T> command)
		{
			return null;
		}
	}

	private static class FieldPicker implements Postprocessor<Object>
	{
		String fieldName;

		public FieldPicker(String fieldName)
		{
			this.fieldName = fieldName;
		}

		@Override
		public Object postprocess(DBObject dbo)
		{
			return dbo.containsKey(fieldName) ?  dbo.get(fieldName) : null;
		}
	}
}
