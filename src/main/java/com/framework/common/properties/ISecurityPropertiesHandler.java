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
 * Interface to the provider of property access to the contents of the security related config file.
 * <p>
 * @author		realMethods, Inc.
 */
public interface ISecurityPropertiesHandler extends IPropertiesHandler
{

//************************************************************************    
// Public Methods
//************************************************************************

   /**
   	* Returns a Collection of Strings, representing the names of the security managers.
   	* @return		security manager names
   	*/
  	public Collection getSecurityManagerNames();

	/**
	 * Returns the attribute names and values for the provided security manager name.s
	 * @param secMgrName
	 * @return	properties for the named security manager
	 */
	public Map getSecurityManagerParams( String secMgrName );
 
	/**
	 * Returns a Map where the key is the name of a sec. manager, and the value is a Map
	 * of its values.
	 * <p>
	 * @return	all security manager properties, where the key is a sec. mgr. name, and the
	 * 			related value is a Map of its properties 
	 */
	public Map getSecurityManagers();
	 
//************************************************************************    
// Attributes
//************************************************************************

}

/*
 * Change Log:
 * $Log$
 */
