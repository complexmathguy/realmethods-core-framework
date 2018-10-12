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
 * Interface to the provider of property access to the contents of the connection pool related config file.
 * <p>
 * @author		realMethods, Inc.
 */
public interface IConnectionPoolPropertiesHandler extends IPropertiesHandler
{


//************************************************************************    
// Public Methods
//************************************************************************


   /**
	* Returns a Collection of Strings, representing the names of the log handlers.
	* @return		log handler names
	*/
	public Collection getConnectionPoolNames();
		
   /**
	* Returns the key/value pairings as parameters for the provided pool name.
	* @param 		poolName	
	* @return		key/value pairings of log handler  parameters.
    */
	public Map getConnectionPoolParams( String poolName );
	  
   /**
	* Returns a Map where the key is the name of a connection pool, and the value is a Map
	* of its values.
	* <p>
	* @return	Map
	*/
	public Map getConnectionPools();
  
//************************************************************************    
// Attributes
//************************************************************************

}

/*
 * Change Log:
 * $Log$
 */
