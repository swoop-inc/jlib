package com.swoop.data.mongo;

/**
 * A specialized MongoDB findOne command that queries by document ID.
 */
public class FindOneByIdCommand
	extends DefaultFindOneCommand
{
	public FindOneByIdCommand(String documentId)
	{
		queryId(documentId);
	}
}
