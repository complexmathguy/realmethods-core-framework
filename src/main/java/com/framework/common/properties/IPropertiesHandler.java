/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.common.properties;

import java.io.InputStream;

import java.util.Map;

/**
 * Base interface of all property hanndlers.
 * <p>
 * @author    realMethods, Inc.
 */
public interface IPropertiesHandler
{
   /** 
   	* Parses the stream's content.
   	* <p>
   	* @param 	stream
   	*/  
	public void parse( InputStream stream );

	/**
	 * Client notification of being through with the handler
	 */
	public void doneNotification();
	
	/**
	 * Returns the key/value pairings as parameters for the provided owners name
	 * @param 		ownersName	
	 * @return		key/value pairings of parameters.
	 */
	public Map getParams( String ownersName );
}

/*
 * Change Log:
 * $Log: IFrameworkPropertiesHandler.java,v $
 */
    
