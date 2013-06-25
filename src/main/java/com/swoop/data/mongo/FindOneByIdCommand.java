package com.swoop.data.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.io.IOException;

/**
 * A specialized MongoDB findOne command that queries by document ID.
 */
public class FindOneByIdCommand
	extends FindOneCommand<DBObject>
{
	private String documentId;

	public FindOneByIdCommand(String documentId)
	{
		this.documentId = documentId;
	}

	protected DBObject getQuery()
	{
		return new BasicDBObject("_id", documentId);
	}

	protected DBObject postprocess(DBObject dbo)
	{
		return dbo;
	}
}
