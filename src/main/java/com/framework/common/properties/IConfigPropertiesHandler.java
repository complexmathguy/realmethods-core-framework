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
 * Interface to the provider of property access to the contents of the configs file.
 * <p>
 * @author		realMethods, Inc.
 */
public interface IConfigPropertiesHandler extends IPropertiesHandler
{
	
	
//************************************************************************    
// Public Methods
//************************************************************************
  	
  	/**
  	 * Returns the log4j attributes from config.xml.
  	 * <p>
  	 * @return		key/value pairs of attribute names/values
  	 */
	public Map getLog4JParams();
	
	/**
	 * Returns a Collection of HashMaps, representing the values for the app-config-file element
	 * from config.xml.
	 * <p>
	 * @return		Collection of Hashaps
	 */
	public Collection getAppConfigFiles();
		
//************************************************************************    
// Attributes
//************************************************************************

}

/*
 * Change Log:
 * $Log$
 */
