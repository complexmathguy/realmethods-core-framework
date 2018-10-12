/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.common.mgr;

import java.util.Properties;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.ConnectionAcquisitionException;
import com.framework.common.exception.ObjectCreationException;
import com.framework.common.exception.TaskExecutionException;

import com.framework.common.misc.Utility;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.integration.objectpool.ConnectionPoolManagerFactory;

import com.framework.integration.objectpool.jms.JMSImpl;

import com.framework.integration.objectpool.ldap.LDAPConnectionImpl;

/**
 * The purpose of this interface has been diminished as of v3.0.  Simply provides indiret
 * access to JMS releted connections and the default LDAP connection, all created by the 
 * ConnectionPoolManager.  Also simplifies the execution of a Framework Task.
 * <p> 
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.objectpool.ConnectionPoolManager
 * @see		  com.framework.common.namespace.FrameworkNameSpace
 * @see		  com.framework.common.misc.Utility
 */
public class FrameworkManager 
    extends FrameworkBaseObject 
    implements IFrameworkManager
{
    /*
     * default constructor - 
     * <p>
     * In order to ensure a singleton pattern with respect to this object type,
     * use the FrameworkManagerFactory.getObject() method
     * instead of creating one directly.
     */
    public FrameworkManager()
    {    	
    }
        
    
    /**
     * returns the global Framework common task execution JMSImpl
     *
     * @return      JMSImpl
     * @exception   ConnectionAcquisitionException
     */
    public JMSImpl getTaskExecutionJMSImpl()
    throws ConnectionAcquisitionException
    {
    	JMSImpl jmsimpl = null;
    	
    	try
    	{
        	jmsimpl = (JMSImpl)ConnectionPoolManagerFactory.getObject().getConnection( FrameworkNameSpace.TASK_EXECUTION_QUEUE );
    	}
    	catch( ObjectCreationException exc )
    	{
    		throw new ConnectionAcquisitionException( "FrameworkManager.getTaskExecutionJMSImpl() - failed to get the ConnectionPoolManager from its factory - " + exc, exc );
    	}
    	catch( Throwable exc1 )
    	{
    		throw new ConnectionAcquisitionException( "FrameworkManager.getTaskExecutionJMSImpl() - " + exc1, exc1 );
    	}
    	
    	return( jmsimpl );
    }
    
    /**
     * returns the global Framework common event related JMS Connection.  
     * Returns null if frameworkEventsEnabled is false
     *
     * @return      JMSImpl
     * @exception   ConnectionAcquisitionException
     */
    public JMSImpl getFrameworkEventJMSImpl()
    throws ConnectionAcquisitionException
    {
    	JMSImpl frameworkEventJMSImpl = null;
    	
    	try
    	{
    		if ( frameworkEventsEnabled == null )
    		{
	    		String prop = (String)Utility.getFrameworkProperties().get( FrameworkNameSpace.FRAMEWORK_EVENTS_ENABLED );
    	
		    	if ( prop != null && prop.length() > 0 )
	    			frameworkEventsEnabled = new Boolean( prop );
				else
					frameworkEventsEnabled = new Boolean( false );						    					
    		}
    			    	    		
    		if ( frameworkEventsEnabled.booleanValue() == true )
	    		frameworkEventJMSImpl = (JMSImpl)ConnectionPoolManagerFactory.getObject().getConnection( FrameworkNameSpace.EVENT_QUEUE_NAME );
    	}
    	catch( ObjectCreationException exc )
    	{
    		throw new ConnectionAcquisitionException( "FrameworkManager.getFrameworkEventJMSImpl() - failed to get the ConnectionPoolManager from its factory - " + exc, exc );
    	}
    	catch( Throwable exc1 )
    	{
    		throw new ConnectionAcquisitionException( "FrameworkManager.getFrameworkEventJMSImpl() - " + exc1, exc1 );
    	}    		
    	
        return( frameworkEventJMSImpl );    
    }

    /**
     * returns the global Framework common LDAP Connection
     *
     * @return      LDAPConnectionImpl
     * @exception   ConnectionAcquisitionException
     */
    public LDAPConnectionImpl getDefaultLDAPConnectionImpl()
    throws ConnectionAcquisitionException
    {
    	LDAPConnectionImpl ldap = null;
    	
    	try
    	{
    		ldap = (LDAPConnectionImpl)ConnectionPoolManagerFactory.getObject().getConnection( getProperties().getProperty( FrameworkNameSpace.DEFAULT_LDAP_CONNECTION_NAME ) );
    	}
    	catch( ObjectCreationException exc )
    	{
    		throw new ConnectionAcquisitionException( "FrameworkManager.getDefaultLDAPConnectionImpl() - failed to get the ConnectionPoolManager from its factory - " + exc, exc );
    	}
    	catch( Throwable exc1 )
    	{
    		throw new ConnectionAcquisitionException( "FrameworkManager.getDefaultLDAPConnectionImpl() - " + exc1, exc1 );
    	}    		
    	
        return( ldap );         
    }

    /**
     * returns the Framework related Properties...should directly use 
     * com.framework.common.misc.Utililty and call method getFramewoorkProperties()
     *
     * @return      Properties
     */
    public Properties getProperties()
    {        
        return( Utility.getFrameworkProperties() );
    }
    
    /**
     * Helper method used to call a Task to execute.  Right now, the current
     * implementation sends a TaskJMSMessage on the Framework TaskExecutionQueue.
     * By default, the TaskJMSMessage indicates to the Task to handle execution
     * as though it were a single transaction.  If this is not the desired behavior
     * simple call getTaskExecutionJMSImpl() and then call sendObject(), creating your
     * TaskJMSMessage with the required argument settings on construction.
     *
     * @param       taskName
     * @param       arg		conditional argument to be applied durig  the Task execution
     * @exception   TaskExecutionException
     */
    public void executeTask( String taskName, Object arg )
    throws TaskExecutionException
    {
    	try
    	{
	        JMSImpl taskJMSImpl = getTaskExecutionJMSImpl();
        
    	    if ( taskJMSImpl != null )
        	{
            	taskJMSImpl.sendObject( new com.framework.common.message.TaskJMSMessage( taskName, arg ), taskName );
		        ConnectionPoolManagerFactory.getObject().releaseConnection( taskJMSImpl );            
    	    }            
    	}
    	catch( Throwable exc )
    	{
    		throw new TaskExecutionException( "FrameworkManager.executeTask(String,Object) - " + exc, exc );
    	}         
    }
    
        
    /**
     * indicator as to whether the framework event notification is enabled in framework.xml
     */
    static private Boolean frameworkEventsEnabled = null;
             
}

/*
 * Change Log:
 * $Log: FrameworkManager.java,v $
 */
