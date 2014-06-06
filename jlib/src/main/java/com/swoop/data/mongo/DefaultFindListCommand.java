package com.swoop.data.mongo;

import com.mongodb.DBObject;

/**
 * A FindListCommand that does no postprocessing, initially.
 */
public class DefaultFindListCommand extends FindListCommand<DBObject>
{
	private final static Postprocessor<DBObject> DEFAULT_POSTPROCESSOR = new DefaultPostprocessor();

	public DefaultFindListCommand()
	{
		super(DEFAULT_POSTPROCESSOR);
	}
}
