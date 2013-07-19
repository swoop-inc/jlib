package com.swoop.framework;

/**
 * Exception type that represents some error in initialization of a resource.
 * Initialization errors are not fatal.
 */
public class InitializationException
    extends Exception
{
    /**
	 * 
	 */
	private static final long	serialVersionUID	= -1416883271804079440L;

	/**
	 * Default constructor.
	 */
    public InitializationException()
    {
    }

    /**
     * Constructor.
     * @param cause    the root cause
     */
    public InitializationException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Constructor.
     * @param msg     a brief description of the error
     */
    public InitializationException(String msg)
    {
        super(msg);
    }

    /**
     * Constructor.
     * @param msg      a brief description of the error
     * @param cause    the root cause
     */
    public InitializationException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
