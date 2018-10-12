/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.properties;

//***************************
//Imports
//***************************

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Provides property access to the contents of the log related config file.
 * <p>
 * @author		realMethods, Inc.
 */
public class LogXMLPropertiesHandler 
	extends XMLPropertiesHandler
	implements ILogPropertiesHandler
{

//************************************************************************    
// Constructors
//************************************************************************


	public LogXMLPropertiesHandler()
	{
		super();
	}
	
//************************************************************************    
// Public Methods
//************************************************************************
  	
    /**
     * Returns a Collection of Strings, representing the names of the log handlers.
     * @return		log handler names
     */
    public Collection getLogHandlerNames()
    {
    	return( getFrameworkXMLParser().getAttributesForEachOccurance( "logHandler", "name" ) );
    }
	
    /**
     * Returns the key/value pairings as parameters for the provided log handler
     * @param 		logHandlerName	
     * @return		key/value pairings of log handler  parameters.
     */
    public Map getLogHandlerParams( String logHandlerName )
    {
    	Map params = getFrameworkXMLParser().getAttributesForEachOccuranceBy( "logHandler",
    																	"name",
    																	logHandlerName );    	
    	return( params );
    }
  
	/**
	 * Returns a Map where the key is the name of a log handler, and the value is a Map
	 * of its values.
	 * <p>
	 * @return	Map
	 */
	public Map getLogHandlers()
	{	
		Map logHandlers = (Map)cache( "logHandlers" );		
		
		if ( logHandlers == null )
		{
			logHandlers = new HashMap();
			Collection names 	= getLogHandlerNames();
			Iterator iter		= names.iterator();
			String name			= null;
				
			while( iter.hasNext() )
			{
				name = (String)iter.next();
				logHandlers.put( name, getLogHandlerParams( name ) );
			}
			
			cache( "logHandlers", logHandlers );
		}
		
		return( logHandlers );
	}
	
//*************************************************************************    
// Protected / Protected Methods
//************************************************************************

//************************************************************************    
// Attributes
//************************************************************************

}

/*
 * Change Log:
 * $Log$
 */
