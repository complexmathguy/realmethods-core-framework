/************************************************************************ 
 * Copyright 2001-2007 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.presentation.struts;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.ActionSupport;

import com.framework.business.vo.IFrameworkValueObject;
import com.framework.business.bo.IFrameworkBusinessObject;

import com.framework.common.FrameworkBaseObject;
import com.framework.common.event.FrameworkLogEventType;
import com.framework.common.exception.AuthenticationException;
import com.framework.common.exception.ProcessingException;
import com.framework.common.exception.FrameworkLogException;
import com.framework.common.misc.Utility;
import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.integration.cache.*;
import com.framework.integration.log.FrameworkDefaultLogger;
import com.framework.integration.security.*;

import org.apache.struts2.interceptor.ServletRequestAware;

/**
 * Base class for Struts Action classes within the Framework.
 * <p>
 * @author    realMethods, Inc.
 */
abstract public class FrameworkBaseStrutsAction //v5.0 Struts 2 no longer extends Action
extends ActionSupport
implements Preparable, ServletRequestAware
{
	
//************************************************************************    
// Public Methods
//************************************************************************

// ServletRequestAware implementation
	
	public void setServletRequest( HttpServletRequest request )
	{
		servletRequest = request;
	}
	
// helper methods

	protected HttpServletRequest getServletRequest()
	{
		return servletRequest;
	}
	
    /**
     * Helper method used to cache an object in the session.
     * <p>
     * @param 	identity	key to object in cache
     * @param  	object		what to cache
     */
    protected void cache(	String identity, 
    						Object object )
    {
        getCache().assign( identity, object );
    }
    
    /**
     * Helper method used to cache a IFrameworkValueObject in the session.
     * <p>
     * @param  	identity	key into cache
     * @return 	match to identity in cache
     */
    protected Object cache( String identity )
    {
        return( getCache().get( identity ) );
    }
    
    /**
     * Helper method to return a parameter value from the HttpServletRequest.
     * <p>
     * @param	id				name of the parameter
     * @return	the parameter
     */
    protected String getServletRequestParameter( String id )
    {
        return( getServletRequest().getParameter( id ) );
    }

    /**
     * If not created, creates it and puts it into the Session variable
     * <p>
     * @param	parameters
     * @return 	the value object cache for the current HttpSession
     */
    protected IFrameworkCache getCache()
    {
        IFrameworkCache cache = (IFrameworkCache)getServletRequest().getSession().getAttribute( FrameworkNameSpace.APPLICATION_CACHE_NAME );
        
        if ( cache == null )
        {
        	try
        	{
	            cache = FrameworkCacheFactory.getInstance().createFrameworkCache();
            
            	// assign to the HttpSession
	            getServletRequest().getSession().setAttribute( FrameworkNameSpace.APPLICATION_CACHE_NAME, cache );
        	}
        	catch( Throwable exc )
        	{
        		FrameworkDefaultLogger.error( "FrameworkBaseStrutsAction:getCache() - " + exc );
        	}                        
        }
        
        return( cache );
        
    }
    
    /**
     * Returns a Collection of String objects that represent all the parameter
     * values based on the provided object id parameter prefix.  It continue
     * to query the parameter set for the first (1), then second (2), and 
     * so on until none in the sequence is located.
     * <p>
     * @param 	objectIDParamPrefix		prefix used to denote the key parameters in the HttpServletRequest
     * @return 	the key fields
     */
    protected Collection getKeysFieldsFromServletRequest( String objectIDParamPrefix )
    {
        Collection v    = new ArrayList();
        String objid  	= null;
        int index     	= 1;
        
        while( (objid = getServletRequestParameter( objectIDParamPrefix + String.valueOf( index ) )) != null )
        {
            v.add( objid );
            index++;
        }
        
		FrameworkDefaultLogger.info( "Key Fields for " + objectIDParamPrefix + " are: " + v.toString() );
        
        return( v );
    }
    
    /**
     * Provide a common interface for Struts action-based logging, 
     * using the FrameworkDefaulLogger.
     * <p>
     * @param 	logEventType	event type
     * @param 	msg				text to log
     */
    protected void logMessage(	FrameworkLogEventType logEventType, 
    							String msg )
    {
        try
        {
        	FrameworkDefaultLogger.sendDefaultLogMessage( logEventType, msg );
        }
        catch( FrameworkLogException exc )
        {
        	new FrameworkBaseObject().logErrorMessage( "Failed to log message to default logger. Will send it through the Framework Log category." );
        	new FrameworkBaseObject().logMessage( msg, logEventType );
        }

    }
    	
    
	/**
	 * Helper method used to validate a token found in the request, against one previously
	 * generated and placed as an attribute in the HttpSession using the key FrameworkNameSpace.TOKEN_KEY.
	 *
	 * @return	String
	 */
	protected String validateToken()
	{
		HttpSession session = getServletRequest().getSession();
		String savedToken	= (String)session.getAttribute( FrameworkNameSpace.TOKEN_KEY );
				
		// clear it out of the httpsession
		session.setAttribute( FrameworkNameSpace.TOKEN_KEY, null );		

		if ( savedToken == null )	// nothing to compare to, but asked to...
		{
			logMessage( FrameworkLogEventType.DEBUG_LOG_EVENT_TYPE, "FrameworkBaseStrutsAction.validateToken() - no token saved in the HttpSession. Be sure previous form included the frameworktag:generateToken tag." );			
			return( FrameworkNameSpace.TOKEN_VALIDATION_ERROR );
		}
		
		String paramToken = getServletRequestParameter( FrameworkNameSpace.TOKEN_KEY );
		
		if ( paramToken == null )
		{
			logMessage( FrameworkLogEventType.DEBUG_LOG_EVENT_TYPE, "FrameworkBaseStrutsAction.validateToken() - no token saved in the HttpSession. Be sure previous form included the frameworktag:generateToken tag." );			
			return( FrameworkNameSpace.TOKEN_VALIDATION_ERROR );			

		}
		
		if ( savedToken.equals( paramToken ) == false )	
		{		
			logMessage( FrameworkLogEventType.INFO_LOG_EVENT_TYPE, "FrameworkBaseStrutsAction.validateToken() - duplicate token discovered." );			
			return( FrameworkNameSpace.DUPLICATE_FORM_SUBMISSION_ERROR );			
		}
		
        logMessage( FrameworkLogEventType.INFO_LOG_EVENT_TYPE, "FrameworkBaseStrutsAction.validateToken() - token check passed..." );	
			
		return( SUCCESS );       
	}
	
// security related helpers
	
    /**
     * Re-uses the proof of concept security adapater to handle 
     * user authentication.
     *
     * @param		userid		user identifier
     * @param		password	user's password
     * @return 		boolean		true if authennticated
     * @exception	AuthenticationException		
     */
    protected boolean authenticateUser( String userid, 
										String password )
    throws AuthenticationException
    {
    	FrameworkSecurityHelper secHelper = null;
    	boolean authenticated = false;
    	
    	try
		{
    		secHelper = new FrameworkSecurityHelper();
    		authenticated = secHelper.authenticateUser( userid, password );
        }
        catch( Throwable exc )
        {
            throw new AuthenticationException( "FrameworkServletSecurityHelper:logon() - failed to authenticate user " + userid + " - " + exc, exc );
        }
        
        return( authenticated );
    }

    
    /**
     * Checks with the FrameworkSecurityManager of the bound HttpSession to see
     * if the user is authorized for the provided role
     * <p> 
     * @param	userid		user identifier
     * @param 	roleName	user's password
     * @return 	boolean		true if authorized
     */
    public boolean isUserAuthorized( 	String userid, 
										String roleName ) 
	{
    	FrameworkSecurityHelper secHelper = null;
    	boolean authorized = false;
    	
		secHelper = new FrameworkSecurityHelper();
		authorized = secHelper.isUserAuthorized( userid, roleName );
		
        return( authorized );
    	
	}
    
//************************************************************************    
// Attributes
//************************************************************************

		
    /**
     * Default object identifier prefix.
     */
    public final static String DEFAULT_OBJECT_ID_PREFIX = "ObjID";
	private final String REDIRECT_TO 					= "redirectTo";
	public final static String KEY_DELIM 				= "!";
	protected HttpServletRequest servletRequest			= null;
	
	private static final long serialVersionUID = -5485382508029951644L;
	
/*
 * Change Log:
 * $Log: FrameworkBaseStrutsAction.java,v $
 */
}
