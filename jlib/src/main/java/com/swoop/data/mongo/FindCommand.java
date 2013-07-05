package com.swoop.data.mongo;

import java.util.List;
import com.mongodb.DBObject;

/**
 * Seed of a MongoDB find or findOne command.
 */
public class FindCommand<T>
	extends FindCommandTemplate<DBObject>
	implements MongoCollectionCommand<List<DBObject>>
{
	public FindCommand()
	{
		super(new Postprocessor<DBObject>() {
			@Override
			public DBObject postprocess(DBObject dbo) {
				return dbo;
			}
		});
	}
}
