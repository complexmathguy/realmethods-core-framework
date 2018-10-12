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
import java.util.Map;

/**
 * Provides property access to the contents of the config.xml file.
 * <p>
 * @author		realMethods, Inc.
 */
public class ConfigXMLPropertiesHandler 
	extends XMLPropertiesHandler
	implements IConfigPropertiesHandler
{
	
//************************************************************************    
// Constructors
//************************************************************************


	public ConfigXMLPropertiesHandler()
	{
		super();
	}
	
//************************************************************************    
// Public Methods
//************************************************************************
  	
  	/**
  	 * Returns the log4j attributes from config.xml.
  	 * <p>
  	 * @return		key/value pairs of attribute names/values
  	 */
	public Map getLog4JParams()
	{
		Map props = (Map)cache( getFrameworkXMLParser().getAttributesForFirstOccurance( "framework-log4j-file" ) );
		
		if ( props == null )
		{		
			props = getFrameworkXMLParser().getAttributesForFirstOccurance( "framework-log4j-file" );
			cache("framework-log4j-file", props );
		}
		
		return ( props );
	}
	
	/**
	 * Returns a Collection of HashMaps, representing the values for the app-config-file element
	 * from config.xml.
	 * <p>
	 * @return		Collection of Hashaps
	 */
	public Collection getAppConfigFiles()
	{
		Collection props = (Collection)cache( "app-config-file" );
		
		if ( props == null )
		{
			props = getFrameworkXMLParser().getAttributesForEachOccurance( "app-config-file" );
			cache( props, "app-config-file" );
		}
		return( props );
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
