package com.swoop.data.mongo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.swoop.util.TimedTask;

public class TimedFindOneCommand extends DefaultFindOneCommand
{
	// default is no timeout -- 0
	private int				timeOut	= 0;
	private final Logger	logger	= LoggerFactory.getLogger(this.getClass());

	public TimedFindOneCommand(int timeout)
	{
		super();
		this.timeOut = timeout;
	}

	public TimedFindOneCommand(String documentId, int timeout)
	{
		super(documentId);
		this.timeOut = timeout;
	}

	public TimedFindOneCommand(String field, String value, int timeout)
	{
		super(field, value);
		this.timeOut = timeout;
	}

	public void setTimeOut(int timeout)
	{
		this.timeOut = timeout;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public DBObject execute(final DBCollection dbCollection)
		throws MongoException, IOException
	{
		DBObject dbo = new TimedTask<DBObject>() {
			@Override
			public DBObject doTask() throws IOException
			{
				long time = System.currentTimeMillis();
				DBObject result = internalExecute(dbCollection);
				time = System.currentTimeMillis() - time;
				if (time > timeOut)
					logger.error("MongoDB {}: took {}ms", dbCollection.getName(), "" + time);
				return result;
			}
		}.execute(timeOut);
		return dbo;
	}

	private DBObject internalExecute(DBCollection dbCollection) throws MongoException, IOException
	{
		return super.execute(dbCollection);
	}
}
