package com.swoop.data.mongo;

import com.mongodb.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Wrapper for Mongo DB object.  Handles connection timeout.
 */
public class MongoWrapper
{
	private final static int KEEP_ALIVE_MILLIS = 10 * 1000;

	private MongoURI uri;
	private int useCount;
	private Mongo mongo;
	private DB db;
	private Runnable releaseTask;

	public MongoWrapper(MongoURI uri)
	{
		this.uri = uri;
	}

	public synchronized DB use() 
		throws IOException
	{
		try {
			if (mongo == null) {
				mongo = new Mongo(uri);
				mongo.setReadPreference(ReadPreference.SECONDARY);
			}
			if (db == null) {
				String database = uri.getDatabase();
				if (database == null) {
					throw new IOException("no DB specified");
				}
				db = mongo.getDB(database);
				authenticate();
			}
			++useCount;
			releaseTask = null;
			return db;
		}
		catch (UnknownHostException e) {
			throw new IOException(e);
		}
		catch (MongoException e) {
			throw new IOException(e);
		}
	}

	public synchronized void release()
	{
		switch (useCount) {
		case 0:
			throw new IllegalStateException();
		case 1:
			startReleaseTask();
		default:
			--useCount;
		}
	}

	//
	// Note a flaw in this system: while we separately cache Mongo DB connections per
	// DatabaseSpec, which includes user name / password, the Mongo driver internally 
	// caches Mongo DB connections per network address.  So here we may receive a DB
	// object that has already been authenticated.  It's quite possible to configure 
	// several different DB connections with varying authentication info, which can lead
	// to mysterious failures - mysterious because they would depend on the order in which
	// the server makes the connections.
	//
	private void authenticate()
	{
		String userName = uri.getUsername();
		if (userName != null && userName.length() > 0 && !db.isAuthenticated()) {
			db.authenticate(userName, uri.getPassword());
		}
	}

	private void startReleaseTask()
	{
		new Thread(releaseTask = new Releaser()).start();
	}

	private class Releaser implements Runnable
	{
		public void run()
		{
			try {
				Thread.sleep(KEEP_ALIVE_MILLIS);

				if (releaseTask == this) {
					close();
				}
			}
			catch (InterruptedException e) {
			}
		}
	}

	private synchronized void close()
	{
		if (mongo != null) {
			mongo.close();
			mongo = null;
		}
		db = null;
	}

	private static MongoOptions getMongoOptions()
	{
		MongoOptions options = new MongoOptions();
		options.reset();
		// Set default Mongo options:
		options.setSocketTimeout(10000);
		options.setMaxWaitTime(2000);
		options.setConnectTimeout(5000);
		// TODO: enable configuration of options, as needed.
		return options;
	}
}
