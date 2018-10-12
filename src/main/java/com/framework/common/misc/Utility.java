/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.common.misc;

//************************************
// Imports
//************************************


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.common.startup.StartupManager;

import com.framework.common.uid.FrameworkUIDGeneratorFactory;
import com.framework.common.uid.IFrameworkUIDGenerator;

import com.framework.common.properties.*;
import com.framework.integration.persistent.FrameworkHibernatorInterceptorFactory;

/**
 * Utility class used to contain static helper methods which belong to 
 * no one class but provide global functionality.
 * <p>
 * @author    realMethods, Inc.
 */
public class Utility extends FrameworkBaseObject
{

//****************************************************
// Attributes
//****************************************************
	/**
	 * the default connection pool name
	 */
    static String defaultDBConnectionName 			= null;
    
    /**
     * host machine name
     */
    static String hostName							= null;
    
    /**
     * framework properties - for global convenience
     */
    static public Properties frameworkProperties 	= null;

	/**
	 * default date format
	 */
    static public String defaultDateFormat 		= "MM/dd/yyyy";
    
    /**
     * default timestamp format
     */
	static public String defaultTimestampFormat	= "hh:mm:ss MM/dd/yyyy";
	
	/**
	 * default time format
	 */
	static public String defaultTimeFormat 		= "MM/dd/yyyy";

    
//****************************************************
// Public Methods
//****************************************************
    /** 
     * deter instantiation but provide for subclassing
     */
    protected Utility()
    {
    }

    /**
    * Forces static initialization of some of the Framework related properties
    */
    static 
    {
    	// just in case not yet provoked
    	StartupManager.getInstance().start( null /*get app. id from framework.xml*/, null /*use -D property for prop. file location*/ );
    	
        try
        {
			frameworkProperties = new Properties();
			frameworkProperties.putAll( PropertyFileLoader.getInstance().getFrameworkPropertiesHandler().getParams() );
        }
        catch ( Throwable exc )
        {
            new FrameworkBaseObject().printTheMessage("Error : Could not access framework property files because of : " + exc );
        }
        
        if ( frameworkProperties != null )
        {
	        defaultDBConnectionName = frameworkProperties.getProperty( FrameworkNameSpace.DEFAULT_CONNECTION_NAME, "TheDBConnection");                
		    defaultDateFormat 		= frameworkProperties.getProperty( FrameworkNameSpace.DEFAULT_DATE_FORMAT, "MM/dd/yyyy");    	
			defaultTimestampFormat	= frameworkProperties.getProperty( FrameworkNameSpace.DEFAULT_TIMESTAMP_FORMAT, "hh:mm:ss MM/dd/yyyy" );
			defaultTimeFormat		= frameworkProperties.getProperty( FrameworkNameSpace.DEFAULT_TIME_FORMAT, "MM/dd/yyyy" );		
        }        
        

        com.framework.integration.persistent.FrameworkPersistenceHelper.self().jumpStart();
        
    }
    
    /**
     * Global provision of the pool connection identifier to the 
     * default Application database  - exists for backward compatability.
     * <p>
     * @return		String
     */
    static public String getMainApplicationConnectionID()
    {
        return( defaultDBConnectionName ); 
    }


	/**
	 * Closes an open database statement.
	 * <p>
	 * <i>Use com.framework.integration.objectpool.db.DBUtility instead.</i>
	 * <p>
	 * @param		conn		the JDBC conection
	 */
	public static void CloseConnection( Connection conn )
	{
		com.framework.integration.objectpool.db.DBUtility.CloseConnection( conn );
	}

	/**
	 * Closes an open database statement
	 * <p>
	 * <i>Use com.framework.integration.objectpool.db.DBUtility instead.</i>
	 * <p>
	 * @param		stmt		the JDBC statement
	 */
	public static void CloseStatement( Statement stmt )
	{
		com.framework.integration.objectpool.db.DBUtility.CloseStatement( stmt );
	}

	/**
	 * Closes an open database result set
	 * <p>
	 * <i>Use com.framework.integration.objectpool.db.DBUtility instead.</i>
	 * <p>
	 * @param 		rs		the JDBC result set
	 */ 
	public static void CloseResultSet( ResultSet rs )
	{
		com.framework.integration.objectpool.db.DBUtility.CloseResultSet( rs );
	}
	
    /**
     * Delegates to EJBServiceLocator getInitialContext, which should be used in favor
     * of this method.
     * <p>
     * @return               	InitialContextContext
     * @exception           	NamingException
     */
    static public InitialContext getInitialContext() 
    throws NamingException 
    {
        return( com.framework.business.ejb.EJBUtility.getInitialContext() );
    }   
   

    /**
     * Delegates to EJBServiceLocator lookup(String)
     * <p>
     * @param		nameToLookup
     * @return     	Object
     * @exception	NamingException
	 * @deprecated	use com.framework.business.ejb.EJBUtility instead
     */
    static public Object lookupHomeInterface( String nameToLookup ) 
    throws NamingException 
    {       
        return( com.framework.business.ejb.EJBUtility.lookupHomeInterface( nameToLookup ) );
    }   

    /** 
     * Transforms a Collection of Framework Remote interfaces into
     * a Collection of IFrameworkBusinessObjects.  Returns null if the 
     * transformation fails.
     * <p>
     * <i>Use com.framework.ibusiness.ejb.EJBUtility instead.</i>
     * <p>
     * @param       remoteInterfaces	Collection of FrameworkRemotes
     * @return      Collection			FrameworkValueObjects obtained by calling getBusinessObject() on each FrameworkRemote
     */
    static public Collection transformRemotesToBOs( Collection remoteInterfaces )
    {        
        return( com.framework.business.ejb.EJBUtility.transformRemotesToBOs( remoteInterfaces ) );
    }
    

    /** 
     * Transforms a Collection of Framework local interfaces into
     * a Collection of IFrameworkBusinessObjects.  Returns null if the
     * transformation fails.
     * <p>
     * <i>Use com.framework.ibusiness.ejb.EJBUtility instead.</i>
     * <p>
     * @param       localInterfaces		Collection of FrameworkLocals
     * @return      Collection			IFrameworkValueObjects obtained by calling getBusinessObject() on each FrameworkLocal
     */
    static public Collection transformLocalsToBOs( Collection localInterfaces )
    {
        return( com.framework.business.ejb.EJBUtility.transformLocalsToBOs( localInterfaces ) );
    }

    /**
     * Returns the contents of the framework.xml file.
     * <p>
     * @return      Properties
     */
    static public Properties getFrameworkProperties()
    {
    	if ( frameworkProperties == null )
    	{
			try
			{
				frameworkProperties = new Properties();
				frameworkProperties.putAll( PropertyFileLoader.getInstance().getFrameworkPropertiesHandler().getParams() );
			}
			catch ( Throwable exc )
			{
				new FrameworkBaseObject().logErrorMessage("ERROR:: COULD NOT RETRIEVE PROPERTIES FROM FRAMEWORK PROPERTIES FILE BECAUSE OF : " + exc );
			}
    	}
    	
        return( frameworkProperties );
    }
    
    /**
     * Tokenizes the input String, using the provided token as String, 
     * into a Collection of String objects.
     * <p>
     * @param       input		parse target
     * @param       delim		character set used to delimeter data in the parse target
     * @return      Collection	tokenized Strings
     */
    static public Collection parseToCollection( String input, String delim )
    {        
        ArrayList list              = new ArrayList();
        StringTokenizer tokenizer   = new StringTokenizer( input, delim );
        String token              	= null;
        
        while( tokenizer.hasMoreTokens() )
        {
            token = tokenizer.nextToken();

            if ( token != null && token.length() > 0 )
                list.add( token.trim() );
        }
        
        return( list );
    }
    
           

    /**
     * Generate a Framework internal identifier, which may not guaranteed to
     * be unique.  This utility method delegates to the framework.xml
     * class of interface type com.framemwork.common.IFrameworkUIDGenerator
     * <p>
     * @param	key		possible input to assist in generating the identifer
     * @return			generated identifier
     */
    static public String generateUID( Object key )
	{
		IFrameworkUIDGenerator uidGenerator = null;    	    

		try
		{
			uidGenerator  = FrameworkUIDGeneratorFactory.getObject();				
		}
		catch( Throwable exc )
		{
		}
		
		return( uidGenerator.generateUID( key ) );			
	}
	
    /**
     * Generate a Framework internal identifier, which is not guaranteed to
     * be unique. This method delegates internally to the overloaded method
     * generateUID( Object key ), passing a null for the arg.
     * <p>
     * @return      String
     */
    static public String generateUID()
	{    
		return( generateUID( null ) );
	}
	
    /**
     * Helper method used to format a String into a Calendar
     * <p>
     * @param       date		focus of the formmat
     * @return      java.util.Calendar
     */
    public static java.util.Calendar formatToCalendar( String date )
    {
        java.util.Calendar cal = formatToCalendar( date, defaultDateFormat );
        
        if ( cal == null )
            cal = formatToCalendar( date, defaultDateFormat );
       
        return( cal );
    }
    
    /**
     * Helper method used to format a String into a Calendar based on the
     * the specified date format.
     * <p>
     * @param       value		focus of the format
     * @param       format		pattern to format to
     * @return      java.util.Calendar
     */    
    public static java.util.Calendar formatToCalendar( String value, String format )
    {
        java.util.Calendar cal = null;
        
        if ( value != null )
        {
            try
            {
                cal = java.util.Calendar.getInstance();
                cal.setTime( new java.text.SimpleDateFormat( format ).parse( value ) );
            }
            catch( Exception exc )
            {
            	new FrameworkBaseObject().printTheMessage( "Utility:formatToCalendar(String,String) - " + exc );
                cal = null;
            }
        }
        
        return( cal );
    }
    
    /**
     * Helper method used to format a String into a java.util.Date
     * <p>
     * @param       value		target of the format
     * @return      java.sql.Date
     */
    static public java.util.Date formatToDate( String value )
    {
        return( new java.sql.Date( formatToSQLDate( value ).getTime() ) );
    }
    
    /**
     * Helper method used to format a String into a java.sql.Date
     * <p>
     * @param       value		target of the format
     * @return      java.sql.Date
     */
    static public java.sql.Date formatToSQLDate( String value )
    {
        java.sql.Date date = formatToSQLDate( value, defaultDateFormat );
        
        if ( date == null )	// try a different format
            date = formatToSQLDate( value, "MM-dd-yyyy" );
       
        return( date );
    }
    
    /**
     * Helper method used to format a String into a java.sql.Date based on the
     * the specified date format. Returns null if the formatting fails.
     * <p>
     * @param       value		focus of the format
     * @param       format		pattern to format to
     * @return      java.sql.Date
     */    
    static public java.sql.Date formatToSQLDate( String value, String format )
    {
        java.sql.Date date = null;
        
        if ( value != null )
        {
            try
            {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime( new java.text.SimpleDateFormat( format ).parse( value ) );
                
                date = new java.sql.Date( cal.getTime().getTime() );
            }
            catch( Exception exc )
            {
            	new FrameworkBaseObject().logMessage( "Utility:formatToSQLDate(String,String) - " + exc );
                date = null;
            }
        }
        
        return( date );
    }
    
    /**
     * Helper method used to format a Date into a String based on the
     * the specified date format.  Returns null if the formatting fails.
     * <p>
     * @param       value		focus of the format
     * @param       format      pattern to format to
     * @return      String
     */    
    static public String formatDateToString( java.util.Date value, String format )
    {
    	String s = null;
    	
    	if ( value != null )
    	{    		
    		try
    		{
    			s = new java.text.SimpleDateFormat( format ).format( value );    		
    		}
            catch( Exception exc )
            {
            	new FrameworkBaseObject().printTheMessage( "Utility:formatDateToString(Date,String) - " + exc );
                s = null;
            }    		
    	}
    	
    	return( s );
    }  
    
    /**
     * Helper method used to format a java.util.Date into a String based on the
     * the default date format. Returns null if the formatting fails.
     * <p>
     * @param       value		focus of the format
     * @return      String
     */    
    static public String formatDateToString( java.util.Date value )
    {	
    	return( formatDateToString( value, defaultDateFormat ) );
    }  
        

    /**
     * Helper method used to format a Timestamp into a String based on the
     * the specified timestamp format.  Returns null if the formatting fails.
     * <p>
     * @param       timestamp		focus of the format
     * @param       format      	pattern to format to
     * @return      String
     */    
    static public String formatTimestampToString( java.sql.Timestamp timestamp, String format )
    {
    	String s = null;
    	
    	if ( timestamp != null )
    	{    		
    		try
    		{
    			s = new java.text.SimpleDateFormat( format ).format( timestamp );    		
    		}
            catch( Exception exc )
            {
                new FrameworkBaseObject().logMessage( "Utility:formatTimestampToString(formatTimestampToString,String) - " + exc );
                s = null;
            }    		
    	}
    	
    	return( s );
    }  
    
    /**
     * Helper method used to format a Timestamp into a String based on the
     * the default timestamp format.  Returns null if the formatting fails.
     * <p>
     * @param       timestamp		focus of the format
     * @return      String
     */    
    static public String formatTimestampToString( java.sql.Timestamp timestamp )
    {
    	String s = formatTimestampToString( timestamp, defaultTimestampFormat );
	
    	return( s );
    }  
	
   /**
     * Helper method used to format a String into a java.sql.Timestamp.  Returns
     * null if the formatting fails.
     * <p>
     * @param       value			focus of the format
     * @return      java.sql.Timestamp
     */
    static public java.sql.Timestamp formatToTimestamp( String value )
    {
        java.sql.Timestamp timestamp = formatToTimestamp( value, defaultTimestampFormat );
        
        return( timestamp );
    }
    
    /**
     * Helper method used to format a String into a java.sql.Timestamp based on the
     * the specified date format.  Returns null if the formatting fails...
     * <p>
     * @param       value		focus of the format
     * @param       format      pattern to format to
     * @return      java.sql.Timestamp
     */    
    static public java.sql.Timestamp formatToTimestamp( String value, String format )
    {
        java.sql.Timestamp timestamp = null;
        
        if ( value != null )
        {
            try
            {
                timestamp = new java.sql.Timestamp( new java.text.SimpleDateFormat( format ).parse( value ).getTime() );
            }
            catch( Exception exc )
            {
            	new FrameworkBaseObject().logMessage( "Utility:formatToTimestamp(String,String) - " + exc );
                timestamp = null;
            }
        }
        
        return( timestamp );
    }
    
	/**
	 * Helper method used to format a Time into a String based on the
	 * the specified timestamp format.  Returns null if the formatting fails.
	 * <p>
	 * @param       time			focus of the format
	 * @param       format      	pattern to format to
	 * @return      String
	 */    
	static public String formatTimeToString( java.sql.Time time, String format )
	{
		String s = null;
    	
		if ( time != null )
		{    		
			try
			{
				s = new java.text.SimpleDateFormat( format ).format( time );    		
			}
			catch( Exception exc )
			{
				new FrameworkBaseObject().logMessage( "Utility:formatTimeToString(java.sql.Time,String) - " + exc );
				s = null;
			}    		
		}
    	
		return( s );
	}  
    

	/**
	 * Helper method used to format a Time into a String based on the
	 * the default timestamp format.  Returns null if the formatting fails.
	 * <p>
	 * @param       time		focus of the format
	 * @return      String
	 */    
	static public String formatTimeToString( java.sql.Time time )
	{
		String s = formatTimeToString( time, defaultTimeFormat );
	
		return( s );
	}  
	
   /**
	 * Helper method used to format a String into a java.sql.Time.  Returns
	 * null if the formatting fails.
	 * <p>
	 * @param       value			focus of the format
	 * @return      java.sql.Time
	 */
	static public java.sql.Time formatToTime( String value )
	{
		java.sql.Time time = formatToTime( value, defaultTimeFormat );
        
		return( time );
	}
    
	/**
	 * Helper method used to format a String into a java.sql.Time based on the
	 * the specified date format.  Returns null if the formatting fails...
	 * <p>
	 * @param       value		focus of the format
	 * @param       format      pattern to format to
	 * @return      java.sql.Time
	 */    
	static public java.sql.Time formatToTime( String value, String format )
	{
		java.sql.Time time = null;
        
		if ( value != null )
		{
			try
			{
				time = new java.sql.Time( new java.text.SimpleDateFormat( format ).parse( value ).getTime() );
			}
			catch( Exception exc )
			{
				new FrameworkBaseObject().logMessage( "Utility:formatToTime(String,String) - using format " + format + " : "+ exc );
				time = null;
			}
		}
        
		return( time );
	}
    
    /**
     * Returns the server host name.
     * <p>
     * @return String
     */
    static public String getHostName()
    {
		if ( ( hostName == null ) || ( hostName.equals("Unknown") ) ) 
		{
			try 
			{
				hostName = InetAddress.getLocalHost().getHostName();
			}
			catch( UnknownHostException e ) 
			{
				hostName = "Unknown";
			}
		}
			
		return hostName;
	}    		
}   

/*
 * Change Log:
 */
