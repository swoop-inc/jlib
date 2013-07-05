package com.swoop.data.mongo;


/**
 * A specialized MongoDB findOne command that queries by document ID.
 * @deprecated
 */
public class FindOneByIdCommand
	extends FindOneByFieldCommand
{
	public FindOneByIdCommand(String documentId)
	{
		super("_id", documentId);
	}
}
