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
 * Interface to the provider of property access to the contents of the ESB related config file.
 * <p>
 * @author		realMethods, Inc.
 */
public interface IESBPropertiesHandler extends IPropertiesHandler
{

//************************************************************************    
// Public Methods
//************************************************************************
  	
  	/**
  	 * Returns a Collection of Strings, representing the names of the endpoints.
  	 * @return		log handler names
  	 */
	public Collection getESBComponentNames();
	
	/**
	 * Returns the key/value pairings as parameters for the provided endpoint
	 * @param 		logHandlerName	
	 * @return		key/value pairings of ESB component data
	 */
	public Map getESBComponentData( String endpointName );
	
	/**
	 * Returns a Map where the key is the name of a compoment, and the value is a Map
	 * of its values.
	 * <p>
	 * @return	Map
	 */
	public Map getESBComponents();
	
	/**
	 * Returns the key/value pairings 
	 * @return	key/value pairings of JMS connector configuration values
	 */
	public Map getJMSConnectorConfig();
	
  	/**
  	 * Returns a Collection of Strings, representing the names of the manager.
  	 * @return		log handler names
  	 */
	public Collection getESBManagerNames();
	
	/**
	 * Returns the key/value pairings as parameters for the provided esb manager name
	 * @param 		managerName	
	 * @return		key/value pairings of esb manager parameters.
	 */
	public Map getESBManagerData( String managerName );
	
	/**
	 * Returns a Map where the key is the name of a manager, and the value is a Map
	 * of its values.
	 * <p>
	 * @return	Map
	 */
	public Map getESBManagers();
	
//************************************************************************    
// Attributes
//************************************************************************

}

/*
 * Change Log:
 * $Log$
 */
