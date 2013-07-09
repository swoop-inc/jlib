package com.swoop.data.mongo;

import com.mongodb.DBObject;
import com.mongodb.MongoException;

public interface Postprocessor<T>
{
	/**
	 * Postprocess one query result object into whatever result type the subclass
	 * requires.  This method may assume that the connection to the server is
	 * maintained. 
	 *
	 * @param dbo  a query result object, non-null
	 * @return the postprocessed result
	 */
	public T postprocess(DBObject dbo)
		throws MongoException, java.io.IOException;
}
