/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.properties;

 

import java.io.InputStream;

import java.util.Map;

import com.framework.common.xml.FrameworkXMLParser;

/** 
 * Handles the loading of xml property files, providing the common methods and 
 * required of all other framework related property handlers.
 * <p>
 * @author    realMethods, Inc.  
 */
abstract public class XMLPropertiesHandler 
    extends PropertiesHandler implements IXMLPropertiesHandler
{

// attributes   

	public XMLPropertiesHandler()
	{
		xmlParser = new FrameworkXMLParser();
	}
	
// IBasePropertiesHandler implementations

   /** 
	* Loads the stream's contents.
	* <p>
	* @param 	stream
	*/  
	public void parse( InputStream stream )
	{
		xmlParser.parseDocument( stream );
	}

	/**
	 * Returns the framework xml parser in use.
	 * @return	framework xml parser.
	 */
	public FrameworkXMLParser getFrameworkXMLParser()
	{	
		return( xmlParser );
	}
	
	/**
	 * Client notification of being through with the handler. Closes the framework xml parser
	 */
	public void doneNotification()
    {
    	xmlParser.close();
    }
    
	/**
	 * Returns the key/value pairings as parameters for the provided owners name
	 * @param 		ownersName	
	 * @return		key/value pairings of parameters.
	 */
	public Map getParams( String ownersName )
	{    
		return( xmlParser.getAttributesForFirstOccurance( ownersName ) );	
	}
	
// attributes
    
    /**
     * properties cache
     */
    protected FrameworkXMLParser xmlParser = null;
    
}

/*
 * Change Log:
 * $Log: XMLPropertiesHandler.java,v $
 */
