/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.common.properties;

import com.framework.common.xml.FrameworkXMLParser;

/**
 * The implementation of this interface is for parsing an xml file represented by an Input Stream
 * <p>
 * @author    realMethods, Inc.
 */
public interface IXMLPropertiesHandler extends IPropertiesHandler
{

	/**
	 * Returns the framework xml parser in use.
	 * @return	framework xml parser.
	 */
	public FrameworkXMLParser getFrameworkXMLParser();
	
}

/*
 * Change Log:
 * $Log: IFrameworkPropertiesHandler.java,v $
 */
    
