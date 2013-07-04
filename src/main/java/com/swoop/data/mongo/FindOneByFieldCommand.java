package com.swoop.data.mongo;

import java.io.IOException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * A specialized MongoDB findOne command that queries by (fieldName, fieldValue) tuple.
 */
public class FindOneByFieldCommand
	extends FindOneCommand<DBObject>
{
	protected String	value;
	protected String	field;

	public FindOneByFieldCommand(String field, String value)
	{
		this.field = field;
		this.value = value;
	}

	@Override
	protected DBObject getQuery()
	{
		return new BasicDBObject(field, value);
	}

	/**
	 * Return the raw MongoDB result object - do no postprocessing.
	 */
	@Override
	protected DBObject postprocess(DBObject dbo) throws IOException
	{
		return dbo;
	}

}
