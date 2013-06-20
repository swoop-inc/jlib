package com.swoop.data.mongo;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.MongoURI;
import com.mongodb.ReadPreference;

import java.io.IOException;

/**
 * Manager of MongoCollection objects.
 */
public class DefaultMongoConnector
	implements MongoConnector
{
	private final static int MAX_IDLE_MILLIS = 10 * 1000;

	private MongoURI uri;
	private int useCount;
	private Thread idleTimer;
	private Mongo mongo;
	private DB db;

	public DefaultMongoConnector()
	{
		this.uri = new MongoURI("mongodb://localhost");
	}

	public DefaultMongoConnector(String uriString)
	{
		this.uri = new MongoURI(uriString);
	}

	/**
	 * @inheritDoc
	 */
	public <T> T executeDbCommand(MongoDbCommand<T> command)
		throws IOException
	{
		try {
			try {
				return command.execute(useDb());
			}
			finally {
				release();
			}
		}
		catch (MongoException e) {
			throw new IOException(e);
		}
	}

	/**
	 * @inheritDoc
	 */
	public <T> T executeCollectionCommand(final String collectionName, final MongoCollectionCommand<T> command)
		throws IOException
	{
		return executeDbCommand(new MongoDbCommand<T>() {
			@Override
			public T execute(DB db)
				throws MongoException, java.io.IOException
			{
				return command.execute(db.getCollection(collectionName));
			}
		});
	}

	public String toString()
	{
		// DO NOT LEAK PASSWORD HERE!
		return "[MongoConnector:" + uri.getHosts() + "," + uri.getDatabase() + "]";
	}

	private synchronized DB useDb()
		throws IOException, MongoException
	{
		cancelIdleTimeout();
		++useCount;
		if (!isOpen()) {
			open();
		}
		return db;
	}

	private synchronized void release()
	{
		switch (useCount) {
		case 0:
			throw new IllegalStateException("useCount");
		case 1:
			startIdleTimeout();
		default:
			--useCount;
		}
	}

	private void startIdleTimeout()
	{
		if (idleTimer != null) {
			throw new IllegalStateException("idleTimer");
		}
		(idleTimer = new IdleTimerThread()).start();
	}

	private void cancelIdleTimeout()
	{
		if (idleTimer != null) {
			idleTimer.interrupt();
			idleTimer = null;
		}
	}

	private synchronized boolean isOpen()
	{
		return db != null;
	}

	private void open()
		throws IOException, MongoException
	{
		try {
			this.mongo = uri.connect();
			this.mongo.setReadPreference(ReadPreference.SECONDARY);
			String database = uri.getDatabase();
			if (database == null) {
				throw new IOException("no DB specified");
			}
			this.db = this.mongo.getDB(database);
			authenticate(uri);
		}
		catch (java.net.UnknownHostException e) {  // why this type doesn't extend IOException beats me.
			throw new IOException(e);
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
	private void authenticate(MongoURI uri)
	{
		String userName = uri.getUsername();
		if (userName != null && userName.length() > 0 && !db.isAuthenticated()) {
			db.authenticate(userName, uri.getPassword());
		}
	}

	private synchronized void close()
	{
		// Double-check state, in case this current thread was context-switched-out poised ready to close. 
		if (useCount == 0 && isOpen()) {
			mongo.close();
			mongo = null;
			db = null;
		}
	}

	private class IdleTimerThread extends Thread
	{
		@Override
		public void run()
		{
			try {
				Thread.sleep(MAX_IDLE_MILLIS);
				close();
			}
			catch (InterruptedException e) {
			}
		}
	}
}
