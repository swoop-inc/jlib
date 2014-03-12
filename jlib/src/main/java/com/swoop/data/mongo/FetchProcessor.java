package com.swoop.data.mongo;

import java.util.Date;

import com.mongodb.DBCursor;

public interface FetchProcessor
{
	public Date process(DBCursor cursor);
}
