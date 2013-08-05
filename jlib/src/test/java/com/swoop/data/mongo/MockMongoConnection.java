package com.swoop.data.mongo;

import com.mongodb.DB;
import com.swoop.data.util.Connection;

public class MockMongoConnection implements Connection<DB>
{
	int openCount, openLevel;

	@Override
	public boolean isOpen()
	{
		return openLevel > 0;
	}

	@Override
	public DB open()
	{
		++openCount;
		++openLevel;
		return null;
	}

	@Override
	public void close()
	{
		--openLevel;
	}
}
