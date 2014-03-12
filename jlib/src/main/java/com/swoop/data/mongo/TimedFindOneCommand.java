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
	private final static Logger	logger	= LoggerFactory.getLogger(TimedFindOneCommand.class);

	private boolean			isTimedOut	= false;

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
		TimedTask<DBObject> timedTask = new TimedTask<DBObject>() {
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
		};

		DBObject dbo = timedTask.execute(timeOut);
		isTimedOut = timedTask.isTimedOut();

		return dbo;
	}

	public boolean isTimedOut()
	{
		return isTimedOut;
	}

	private DBObject internalExecute(DBCollection dbCollection) throws MongoException, IOException
	{
		return super.execute(dbCollection);
	}
}
