/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.security;

import javax.servlet.http.HttpSession;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.AuthenticationException;
import com.framework.common.exception.ObjectCreationException;

import com.framework.common.misc.Utility;

import com.framework.common.namespace.FrameworkNameSpace;
import com.framework.integration.esb.ESBManagerFactory;
import com.framework.integration.esb.IESBManager;

/**
 * Delegates to the FrameworkSecurityHelper, but uses the ESB to communicate
 * for more loose coupling
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkSecurityDelegateToHelper extends FrameworkBaseObject
{
//************************************************************************    
// Public Methods
//************************************************************************
    
    /**
     * default constructor
     */
    public FrameworkSecurityDelegateToHelper()
	{
    	if ( securityHelper == null )
    		securityHelper = new FrameworkSecurityHelper();
    }

//***************************************************
// public methods
//***************************************************

    /**
     * Re-uses the proof of concept security adapater to handle 
     * user authentication.
     *
     * @param		userid		user identifier
     * @param		password	user's password
     * @return 		boolean		true if authennticated
     * @exception	AuthenticationException		
     */
    public boolean authenticateUser( String userid, String password )
    throws AuthenticationException
    {
    	boolean is = false;
    	String service = FrameworkNameSpace.framework_security_service;
    	String methodName = "authenticateUser";
    	
    	Object[] args = {userid, password};
    	try
    	{
	    	IESBManager esbManager = ESBManagerFactory.getInstance().getESBManager( FrameworkNameSpace.DefaultESBManager );
	    	Object payload = esbManager.send( service, methodName, args, null);
	    	
	    	if ( payload != null )
	    		is = ((Boolean)payload).booleanValue();
    	}
    	catch( Throwable exc )
    	{
    		logErrorMessage( "FrameworkSecurityDelegateToHelper.authenticateUser(...) - " + exc );
    	}
        return( is );
    }

    
    /**
     * Checks with the FrameworkSecurityManager of the bound HttpSession to see
     * if the user is authorized for the provided role
     * <p> 
     * @param	userid		user identifier
     * @param 	roleName	user's password
     * @return 	boolean		true if authorized
     */
    public boolean isUserAuthorized( String userid, String roleName ) 
	{
    	boolean is = false;
    	String service = FrameworkNameSpace.framework_security_service;
    	String methodName = "isUserAuthorized";    	
    	Object[] args = {userid, roleName};
    	
    	try
    	{
	    	IESBManager esbManager = ESBManagerFactory.getInstance().getESBManager( FrameworkNameSpace.DefaultESBManager );
	    	Object payload = esbManager.send( service, methodName, args, null);
	    	
	    	if ( payload != null )
	    		is = ((Boolean)payload).booleanValue();
		}
		catch( Throwable exc )
		{
			logErrorMessage( "FrameworkSecurityDelegateToHelper.authenticateUser(...) - " + exc );
		}
    	
        return( is );
	}
    

//***************************************************
// protected methods
//***************************************************
    
    
//***************************************************
// attributes
//***************************************************
	
	/**
	 * Framework Security Manager
	 */
    static protected FrameworkSecurityHelper securityHelper = null;
    
}

/*
 * Change Log:
 * $Log: FrameworkServletSecurityHelper.java,v $
 */
