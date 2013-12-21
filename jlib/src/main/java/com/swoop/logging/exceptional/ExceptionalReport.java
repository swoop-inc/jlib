package com.swoop.logging.exceptional;

/**
 * A potential message to the Exceptional.io server.
 */
public interface ExceptionalReport
{
	public String getMessage();
	public String getLogLevel();
	public String getMethodName();
	public String getLineNumber();
	public String getThreadName();
	public String getExceptionClass();
	public String[] getBacktrace();
}
