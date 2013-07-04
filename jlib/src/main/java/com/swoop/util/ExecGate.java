package com.swoop.util;

/**
 * A synchronization utility.  "Ask me the questions, Bridgekeeper.  I am not afraid." 
 */
public class ExecGate 
{
	private int lockCount;

	/**
	 * Constructor.
	 */
	public ExecGate()
	{
		this(0);
	}

	/**
	 * Constructor.
	 */
	public ExecGate(int lockCount)
	{
		if (lockCount < 0) {
			throw new IllegalArgumentException();
		}
		this.lockCount = lockCount;
	}

	/**
	 * Try to cross the gate.  If it is closed, wait for it to open.
	 */
	public synchronized void cross()
		throws InterruptedException
	{
		while (lockCount > 0) {
			wait();
		}
	}

	/**
	 * Allow threads to cross.
	 */
	public synchronized void unlock()
	{
		if (lockCount > 0 && --lockCount == 0) {
			notifyAll();
		}
	}

	/**
	 * Prevent threads from crossing.
	 */
	public synchronized void lock()
	{
		++lockCount;
	}
}
