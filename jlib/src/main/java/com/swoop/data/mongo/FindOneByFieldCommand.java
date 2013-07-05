package com.swoop.data.mongo;

/**
 * A specialized MongoDB findOne command that queries by (fieldName, fieldValue) tuple.
 * @deprecated
 */
public class FindOneByFieldCommand
	extends DefaultFindOneCommand
{
	public FindOneByFieldCommand(String field, String value)
	{
		queryField(field, value);
	}
}
