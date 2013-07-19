package com.swoop.data.mongo;

import com.mongodb.DBObject;

/**
 * A FindOneCommand that does no postprocessing, initially.
 */
public class DefaultFindOneCommand
	extends FindOneCommand<DBObject>
	implements MongoCollectionCommand<DBObject>
{
	private final static Postprocessor<DBObject> DEFAULT_POSTPROCESSOR = new DefaultPostprocessor();

	public DefaultFindOneCommand()
	{
		super(DEFAULT_POSTPROCESSOR);
	}

	public DefaultFindOneCommand(String documentId)
	{
		super(DEFAULT_POSTPROCESSOR);
		queryId(documentId);
	}

	public DefaultFindOneCommand(String field, String value)
	{
		super(DEFAULT_POSTPROCESSOR);
		queryField(field, value);
	}
}
