/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.hook;

import java.util.Map;

import com.framework.common.FrameworkBaseObject;
import com.framework.common.exception.FrameworkStartupException;
import com.framework.common.exception.HookAccessException;
import com.framework.common.exception.ObjectCreationException;

import com.framework.common.properties.PropertyFileLoader;

import com.framework.common.startup.FrameworkStartup;

/**
 * Contains a singleton instance which does the real work.  Only delegate
 * to it when necessary, otherwise, the non-singleton instance holds the
 * already created and accessed hook interface.  This way, we can decrease 
 * any bottle neck to the singleton.
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkHookManager 
    extends FrameworkStartup
    implements IFrameworkHookManager
{

// constructors

    /**
     * default constructor
     */
    public FrameworkHookManager()
    {    	
    	singleton = this;
    }
    

// FrameworkStartup overloads

	public void start()
	throws FrameworkStartupException	
	{
		refreshHooks();
	}

	public void stop()
	{
		hookKeysAndClassNames.clear();
	}
	
// action methods
    
    /**
     * refreshes the Map of hooks by loading the classnames from the
     * framework property handler
     */
    public void refreshHooks()
    {
        // clear out the FrameworkHookFactory cache
        try
        {
            FrameworkHookFactory.getInstance().clearCache();
        }
        catch( ObjectCreationException exc )
        {
        	logErrorMessage( "FrameworkHookManager.refreshHooks() - failed to get an instance of a FrameworkHookFactory - " + exc );
        }
        
        // lazy initialization
        if ( hookKeysAndClassNames != null )
        {
            // clear it out...
            hookKeysAndClassNames.clear();
        }
            
        // use the FrameworkPropertiesHandler to load the hooks
        try
        {
            hookKeysAndClassNames = PropertyFileLoader.getInstance().getFrameworkPropertiesHandler().getHooks();
        }
        catch( Throwable exc2 )
        {
        	logErrorMessage( "FrameworkHookManager.refreshHooks() - failed to acquire the hook related framework properties  - " + exc2 );
        }
                            
    }
    
    /**
     * static method used to control the creation of a HookManager
     *
     * @return      IFrameworkHookManager
     */
	static synchronized public IFrameworkHookManager getHookManager()
    {
        if ( singleton == null )
        {       	
        	try
        	{
         		singleton = new FrameworkHookManager();        
            	singleton.start();
        	}
        	catch( FrameworkStartupException exc )
        	{
        		new FrameworkBaseObject().logErrorMessage( "FrameworkHookManager.getHookManager() - failed to start the HookManager - " + exc );   
        	}
        }
        
        return( singleton );
    }
    

    /**
     * returns the hook interface associated with the class name to create
     * <p>
     * @param       hookClassName
     * @return      IFrameworkHook
     * @exception   HookAccessException
     */
    public IFrameworkHook getHook( String hookClassName )
    throws HookAccessException
    {
    	if ( hookKeysAndClassNames != null )
    	{
	        String className = (String)hookKeysAndClassNames.get( hookClassName );
	                    
	        if ( className != null )   // found one
	        {
	            try
	            {
	                requestHook = FrameworkHookFactory.getInstance().getHookAsObject( className );
	            }
	            catch( Exception exc )
	            {
	                throw new HookAccessException( "FrameworkHookManager:getHook() - failed to access hook " + hookClassName + " - " + exc, exc );
	            }
	        }
    	}
    	else
    		requestHook = null;
    	        
        return( requestHook );
    }
    
    
// attributes

    /**
     * the hook requested
     */
    protected IFrameworkHook requestHook = null;
    
    /**
     * attempted hook access indicator
     */
    protected boolean attemptedHookAccess = false;
    
    /**
     * singleton exists indicator
     */
    protected boolean singletonIndicator = false;
    
    /**
     * the singleton
     */
    private static FrameworkHookManager singleton = null;
    
    /**
     * the hooks by name, className
     */
    private Map hookKeysAndClassNames = null;
}


/*
 * Change Log:
 * $Log: FrameworkHookManager.java,v $
 * Revision 1.4  2003/10/07 21:35:46  tylertravis
 * Use logxxxMessage in place of logMessage.
 *
 * Revision 1.3  2003/09/09 17:51:06  tylertravis
 * Completed the comments for public void refreshHooks().
 *
 * Revision 1.2  2003/08/05 12:16:17  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:20  tylertravis
 * initial sourceforge cvs revision
 *
 */
