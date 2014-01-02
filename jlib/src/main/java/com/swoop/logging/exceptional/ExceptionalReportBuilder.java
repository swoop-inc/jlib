package com.swoop.logging.exceptional;

import java.util.ArrayList;
import java.util.List;

/**
 * Create an item that can serve as the payload of a report to the Exceptional.io server.
 */
public class ExceptionalReportBuilder
{
	private final static int IGNORED_SYNTHETIC_STACK_FRAMES = 1;

	private String threadName;
	private Throwable throwable;
	private boolean synthetic;
	private String logLevel;

	/**
	 * Construct a report having the given message, instead of an associated exception.
	 * @param message   an error message
	 */
	public ExceptionalReportBuilder(String message)
	{
		init();
		this.throwable = new Throwable(message);
		this.synthetic = true;
		throwable.fillInStackTrace();  // the current stack frame is ignored.
	}

	/**
	 * Construct a report from the given exception
	 * @param throwable   an exception
	 */
	public ExceptionalReportBuilder(Throwable throwable)
	{
		init();
		this.throwable = throwable;
	}

	public ExceptionalReportBuilder setLogLevel(String logLevel)
	{
		this.logLevel = logLevel;
		return this;
	}

	public ExceptionalReport build()
	{
		return new ExceptionalReport() {
			@Override
			public String getLogLevel()
			{
				return logLevel;
			}

			@Override
			public String getThreadName()
			{
				return threadName;
			}

			@Override
			public ExceptionalReport.Error[] getErrors()
			{
				List<ExceptionalReport.Error> errorList = new ArrayList<ExceptionalReport.Error>();
				for (Throwable thr = throwable; thr != null; thr = thr.getCause()) {
					errorList.add(buildError(thr, synthetic && thr == throwable));
				}
				return errorList.toArray(new ExceptionalReport.Error[errorList.size()]);
			}
		};
	}

	private ExceptionalReport.Error buildError(final Throwable throwable, final boolean synthetic)
	{
		final StackTraceElement[] stackTrace = throwable.getStackTrace();

		return new ExceptionalReport.Error()
		{
			@Override
			public String getMessage()
			{
				return throwable.getMessage();
			}

			@Override
			public String getMethodName()
			{
				return getStackTraceElement(0).getMethodName();
			}

			@Override
			public String getLineNumber()
			{
				return String.valueOf(getStackTraceElement(0).getLineNumber());
			}

			@Override
			public String getExceptionClass()
			{
				return synthetic ? null : throwable.getClass().getName();
			}

			@Override
			public String[] getBacktrace()
			{
				String[] backtrace = new String[getStackTraceLength()];
				for (int i = 0; i < backtrace.length; ++i) {
					backtrace[i] = getStackTraceElement(i).toString().trim();
				}
				return backtrace;
			}

			private StackTraceElement getStackTraceElement(int index)
			{
				return stackTrace[index + (synthetic ? IGNORED_SYNTHETIC_STACK_FRAMES : 0)];
			}

			private int getStackTraceLength()
			{
				return stackTrace.length - (synthetic ? IGNORED_SYNTHETIC_STACK_FRAMES : 0);
			}
		};
	}

	private void init()
	{
		this.threadName = Thread.currentThread().getName();
	}
}
