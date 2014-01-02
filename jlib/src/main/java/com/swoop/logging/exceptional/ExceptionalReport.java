package com.swoop.logging.exceptional;

/**
 * A potential message to the Exceptional.io server.
 */
public interface ExceptionalReport
{
	public String getLogLevel();
	public String getThreadName();
	public Error[] getErrors();

	public static interface Error
	{
		public String getMessage();
		public String getMethodName();
		public String getLineNumber();
		public String getExceptionClass();
		public String[] getBacktrace();
	}
}
