package com.swoop.data.mongo;

import com.mongodb.DBObject;

/**
 * A trivial postprocessor.
 */
public class DefaultPostprocessor
	implements Postprocessor<DBObject>
{
	@Override
	public DBObject postprocess(DBObject dbo)
	{
		return dbo;
	}
}
