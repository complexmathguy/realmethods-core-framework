/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.presentation.translator;

import javax.servlet.ServletRequest;

import com.framework.business.pk.FrameworkPrimaryKey;

import com.framework.business.vo.IFrameworkValueObject;

import com.framework.business.vo.list.IListProxy;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.misc.Utility;

import com.framework.integration.cache.IUserSessionObjectManager;

/**
 * Generic Base Translator class.
 * <p>
 * For situations where specific list and attribute translators are either not
 * defined or required, this class will provide the basic capabilites of acquiring
 * from the UserSessionObjectManager bound during initialization.  The USOM itself
 * is during creationion of the com.framework.taglib.TranslatorTag.  This class gets
 * the USOM from either the UserSession (<i>rM Presentation Tier in use</i>) or the
 * HttpSession (<i>Struts Presentation Tier in use</i>)
 * <p>
 * <p>
 * @author    realMethods, Inc.
 */
public class BaseTranslator 
    extends FrameworkBaseObject implements IBaseTranslator
{

// constructors

    /** 
     * Base constructor.
     */
    public BaseTranslator()
    {
        // call base class
        super();            
    }

// initialization methods

    /** 
     * This initialization method replaces the init( IUserSessionObjectManager ), which
     * remains for backward capability.  The provided objects represent the locations
     * by which an object should be acquired, first looking in the servletRequest, then
     * the USOM.
     * <p
     * @param       servletRequest     
     * @param       usom			cache context 
     */
    public void init( ServletRequest servletRequest, IUserSessionObjectManager usom )
    {
        this.servletRequest    		= servletRequest; 
        baseUserSessionObjectManager    = usom;
    }
    
    /** 
     * This initialization method remains for backward capability.  Use an overloaded
     * version.
     * <p>
     * @param    		usom 	cache context
     * @deprecated	
     */    
    public void init( IUserSessionObjectManager usom )
    {
        baseUserSessionObjectManager	= usom;
        servletRequest    				= null; 
    }
    

// accessor methods

    /**
     * Retrieves an IFrameworkValueObject from a UserSessionObjectManager using the 
     * provided String as a key.
     * <p>
     * @param   key
     * @return  IFrameworkValueObject 
     */    
    public IFrameworkValueObject getValueObject( String key )
    {
        return( getValueObject( new FrameworkPrimaryKey( key ) ) );        
    }

    /**
     * Retrieves an IFrameworkValueObject from a UserSessionObjectManager using the 
     * provided FrameworkPrimaryKey as a key.
     * <p>
     * @param   objKey
     * @return  IFrameworkValueObject 
     */    
    public IFrameworkValueObject getValueObject( FrameworkPrimaryKey objKey )
    {
		return( (IFrameworkValueObject)get( objKey ) );
    }
        
    /**
     * Retrieves an IListProxy implementation containing IFrameworkValueObjects.
     * <p>
     * @param  	key
     * @return  IListProxy
     */
    public IListProxy getList( String key )
    {
        return((IListProxy)getValueObject( key ) );
    }

	/**
	 * Return the object from the cache associated with the provided key
	 * @param 	objKey	String
	 * @return	Object
	 */
	public Object getValue( String objKey )
	{
		return( get( objKey ) );
	}
	
    
    /**
     * Return the object from the cache associated with the provided key
     * @param 	objKey	Object
     * @return	Object
     */
    public Object get( Object objKey )
    {
		if ( objKey == null )
			throw new IllegalArgumentException( "BaseTranslator:get( Object ) - argument cannot be null" );
            
		Object obj = null;    

		// step #1:  attempt to locate the object in the servlet request 
		if ( getServletRequest() != null )  // one provided, so use it
		{
			// assume the first attribute in the FrameworkPrimaryKey is
			// the only one, and has a String representation
			obj = (Object)getServletRequest().getAttribute( objKey.toString() );
		}
        
		if ( obj == null )   // not yet located
		{
			// Retrieve the UserSessionObjectManager
			IUserSessionObjectManager objectManager = getBaseUserSessionObjectManager();
                
			// Ensure we got one before doing anything else
			if (objectManager != null)
			{
				obj = objectManager.get( objKey );        
			}            
			else
			{
				logErrorMessage( "***Null USOM discovered in BaseTranslator..." );
			}
		}
            
		return( obj );
    	
    }
    
// format related methods

    /** 
     * Returns a formatted String representation of the provided Calendar, using
     * the default date format.
     * <p>
     * @param       	date
     * @return      	String
     * @deprecated	v2.3.4 -  use Utility.formatDateToString(java.util.Calendar) instead
     */
    static public String formatDate( java.util.Calendar date )
    {
    	if ( date != null )
	    	return( Utility.formatDateToString( date.getTime() ) );
		else
			return( "" );	    	
    }

    /** 
     * Returns a formatted String representation of the provided Calendar, using
     * the provided date format.
     * <p>
     * @param       	date
     * @param			dateFormat	
     * @return      	String
     * @deprecated	v2.3.4 -  use Utility.formatDateToString(java.util.Calendar,String) instead
     */
    static public String formatDate( java.util.Calendar date, String dateFormat )
    {
    	if ( date != null )    	
	        return( Utility.formatDateToString( date.getTime(), dateFormat ) );
		else
			return( "" );	        
    }

    /** 
     * Returns a formatted String representation of the provided Date, using
     * the default date format.
     * <p>
     * @param       	date
     * @return      	String
     * @deprecated	v2.3.4 -  use Utility.formatDateToString(java.util.Date) instead
     */
    static public String formatDate( java.util.Date date )
    {
        return( Utility.formatDateToString( date ) );
    }

    /** 
     * Returns a formatted String representation of the provided Date, using
     * the provided date format.
     * <p>
     * @param       	date
     * @param			dateFormat	
     * @return      	String
     * @deprecated	v2.3.4 -  use Utility.formatDateToString(java.util.Date,String) instead
     */    
    static public String formatDate( java.util.Date date, String dateFormat )
    {
		return( Utility.formatDateToString( date, dateFormat )  );
    }


    /**
     * Returns the date format string to use for date related output.
     * <p>
     * @return      	String
     * @deprecated	v2.3.4 -  use Utility.getDefaultDateFormat() instead
     */
    static protected String getDateFormat()
    {
        return( Utility.defaultDateFormat );
    }
    
//************************************************************************    
// Private / Protected Methods
//************************************************************************

    
    /**
     * Retrieves the UserSessionObjectManager.
     * @return UserSessionObjectManager
     */
    protected IUserSessionObjectManager getBaseUserSessionObjectManager()
    {
        return baseUserSessionObjectManager;   
    }
    
    /**
     * Returns the ServletRequest.
     * @return	ServletRequest
     */
    protected ServletRequest getServletRequest()
    {
        return( servletRequest );
    }
    
    /**
     * Helper method to always return a non-null String
     * <p>
     * @param       o
     * @return      String
     */
    protected String assignSafeStringFromObject( Object o )
    {
        if ( o != null )
        {
            if ( o instanceof java.util.Date )
            {
                return( formatDate( (java.util.Date)o ) );
            }
            else if ( o instanceof java.util.Calendar )
            {
                return( formatDate( (java.util.Calendar)o ) );
            }
            else
                return( o.toString() );
        }
        else
            return( "" );
    }
    
    
//************************************************************************    
// Attributes
//************************************************************************
    /**
     * IUserSessionObjectManager.
     */
    protected IUserSessionObjectManager baseUserSessionObjectManager = null;
    
    /**
     * ServletRequest
     */
    protected ServletRequest servletRequest = null;     
}

/*
 * Change Log:
 * $Log: BaseTranslator.java,v $
 */
