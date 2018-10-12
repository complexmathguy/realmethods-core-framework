/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.ejb;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.framework.common.FrameworkBaseObject;

/** 
 * Common base class for both FrameworkSessionBean and FrameworkEntityBean.
 *
 * 
 * <p> 
 * @author    realMethods, Inc.
 */
abstract public class FrameworkEJB extends FrameworkBaseObject
{
    /** 
     * This helper method is used by the sub-classes to acquire the name
     * of the connection pool to use for persistent activities.
     * <p>
     * @return       pool name to use
     */
    protected String determineConnectionPoolName()
    {
        if ( connectionPoolName == null )                
        {
            connectionPoolName = (String)getEJBProperty( "ConnectionPoolName" );
                        
            // if not there, get it from the global Utility object, which will get it from
            // the framework.xml property DefaultDatabaseConnectionName                        
            if ( connectionPoolName == null )
            {
                connectionPoolName = EJBUtility.getMainApplicationConnectionID();
	        	logDebugMessage( "FrameworkEJB:determineConnectionPoolName() - using " + connectionPoolName + " discovered in framework.xml" );                
            }
            else
            {
	        	logDebugMessage( "FrameworkEJB:determineConnectionPoolName() - using " + connectionPoolName + " loaded as an env-entry" );            	
            }
        }
        
        return( connectionPoolName );
        
    }

    /**
     * Helper method used to acquire a named EJB property for this
     * EJB object, as indicated within the deployment descriptor.
     * Returns null if nothing is found.
     *
     * @param       name		property name
     * @return      Object
     */
    protected Object getEJBProperty( String name )
    {
        Object object = null;
        
        // first, check this objects InitialContext java:comp/env
        try
        {
            Context initial = new InitialContext();
            Context env     = (Context)initial.lookup( "java:comp/env" );
                
            object = (Object)env.lookup( name );
        }
        catch( NamingException exc )
        {
        	logDebugMessage( "FrameworkEJB:getEJBProperty() - unable to locate " + name + " as an env-entry." );
        }
        
        return( object );
    }
    
// attributes

    /**
     * The name of the connection to use during persistence activities
     */
    protected String connectionPoolName = null;

}

/*
 * Change Log:
 * $Log: FrameworkEJB.java,v $
 */
