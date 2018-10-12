/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.properties;

//***************************
//Imports
//***************************

import java.util.Map;

/**
 * Interface to the provider of property access to the contents of the framework related config file.
 * <p>
 * @author		realMethods, Inc.
 */
public interface IFrameworkPropertiesHandler extends IPropertiesHandler
{

//************************************************************************    
// Public Methods
//************************************************************************
  	
   /**
   	* Returns the root parameters.
   	* @return	presentation and integration tier parameters
   	*/
  	public Map getParams();

   /**
   	* Returns the parameter value for the provided key.
   	* <p>
   	* @param 	key
   	* @return	presentation and integration tier parameters
   	*/
  	public String getParam( String key );
		
   /**
   	* Returns the parameter value for the provided key.  If the key doesn't exist, or no
   	* value has been applied, returns the defValue.
   	* <p>
   	* @param	key		key to parameter value
   	* @param 	defValue	default value
   	* @return	discovered parameter, or the defValue if not found
   	*/		
  	public String getParam( String key, String defValue );
  	
  	/**
  	 * Returns the mapping of jndi args.
  	 * @return	jndi args
  	 */
  	public Map getJNDIArgs();
  	
  	/**
  	 * Returns the mapping of startup declarations.
  	 * @return	startup definitions
  	 */
  	public Map getStartups();
  	
  	/**
  	 * Returns the mapping of hook declarations.
  	 * @return	hook definitions
  	 */
  	public Map getHooks();
  	
  	/**
  	 * Returs the mapping of factory declarations.
  	 * @return	factory declarations
  	 */
  	public Map getFactoryDecls();
  	
  	
//************************************************************************    
// Attributes
//************************************************************************

}

/*
 * Change Log:
 * $Log$
 */
