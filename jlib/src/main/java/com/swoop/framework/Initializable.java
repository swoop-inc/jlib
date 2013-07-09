package com.swoop.framework;


/**
 * Interface to mark resource objects that must be initialized before usage.
 */
public interface Initializable
{
	/**
	 * Initialize this object.  If initialization fails, the contract does not require this 
	 * object to be disposed of; therefore, in the case of failure the object is required to
	 * leave itself in a stable state.  (Its methods may still fail, but are required to 
	 * at least throw intelligible exceptions).
	 *  
	 * @throws InitializationException if initialization fails - for informational purposes only
	 */
	public void init()
		throws InitializationException;
}
