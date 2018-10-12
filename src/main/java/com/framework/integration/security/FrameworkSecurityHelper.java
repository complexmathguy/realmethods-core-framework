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

/**
 * Uses the default security manager to help authenticate/authorize a user
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkSecurityHelper extends FrameworkBaseObject
{
//************************************************************************    
// Public Methods
//************************************************************************
    
    /**
     * default constructor
     */
    public FrameworkSecurityHelper()
	{
    	if ( appID == null )
    		appID = Utility.getFrameworkProperties().getProperty(FrameworkNameSpace.APPLICATION_ID_TAG, ""); 
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
     * @return 		Boolean		true if authennticated, false otherwise
     * @exception	AuthenticationException		
     */			   
    public Boolean authenticateUser( String userid, String password )
    throws AuthenticationException
    {
    	String appID			= null;
        boolean bIsLoggedOn		= true;
        IFrameworkSecurityManager securityManager = null;

//        System.out.println( "Inside of here, userid: " + userid + ", pw: " + password );
        
        try
        {
        	securityManager = getSecurityManager();
        	
        	if ( securityManager != null )
        	{
        		if ( securityManager.retrieveSecurityUser( userid, password, appID ) != null )
        			bIsLoggedOn = true;        		
        		else
        			bIsLoggedOn = false;
        	}
        }
        catch( Throwable exc )
        {
            throw new AuthenticationException( "FrameworkServletSecurityHelper:logon() - failed to authenticate user " + userid + " - " + exc, exc );
        }
        
        return( new Boolean( bIsLoggedOn ) );
    }

    
    /**
     * Checks with the FrameworkSecurityManager of the bound HttpSession to see
     * if the user is authorized for the provided role
     * <p> 
     * @param	userid		user identifier
     * @param 	roleName	user's password
     * @return 	boolean		true if authorized
     */
    public Boolean isUserAuthorized( String userid, String roleName ) 
	{
		boolean isUserAuthorized 					= true;	// assume true
		IFrameworkSecurityManager securityManager 	= null;
		
		try
		{
			securityManager = getSecurityManager();
			
			if( securityManager != null )
				isUserAuthorized = securityManager.isUserAuthorized( new SecurityUser( userid ), new SecurityRole( roleName ) );
			else
				isUserAuthorized = true;	// if not framework authentication has been configured, assume authorized
		}
		catch( ObjectCreationException exc )
		{
			logErrorMessage( "FrameworkServletSecurityHelper.isUserAuthorized(String,String) - failed to authorized user " + userid + " - " + exc );
		}	
			
		return( new Boolean( isUserAuthorized ) );
		
	}
    

//***************************************************
// protected methods
//***************************************************
    
    /** 
     * Looks in the current HttpSession for the security manager. If not there, creates it and puts it in
     * <p>
     * @return 		IFrameworkSecurityManager
     * @exception	ObjectCreateException
     */
    protected IFrameworkSecurityManager getSecurityManager()
    throws ObjectCreationException
    {
		if ( securityManager == null )
		{
			securityManager  = FrameworkSecurityManagerFactory.getInstance().getDefaultSecurityManager();
		}
		
		return( securityManager );
    }
    
//***************************************************
// attributes
//***************************************************
	
	/**
	 * Framework Security Manager
	 */
    static protected IFrameworkSecurityManager securityManager = null;
    
    /**
     * The HttpSession in use
     */
	protected HttpSession session		= null;
	
	/**
	 * application id : static
	 */
	static protected String appID		= null;

    
}

/*
 * Change Log:
 * $Log: FrameworkServletSecurityHelper.java,v $
 */
