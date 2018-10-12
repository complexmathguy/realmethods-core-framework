/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.exception;

//************************************
// Imports
//************************************

/**
 * Exception class for xml file parsing related errors. 
 *
 * <p>
 * @author    realMethods, Inc.
 * @see       com.framework.presentation.parser.FrontControllerXMLParser
 */
public class XMLFileParsingException extends FrameworkCheckedException
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public XMLFileParsingException()
    {
    }

    /**
     * constructor
     *
     * @param	message		
     */
    public XMLFileParsingException( String message )
    {
        super( message );
    }
    
    /**
     * Constructor with a Throwable for chained exception and a message.
     * @param message
     * @param exception
     */
    public XMLFileParsingException( String message, Throwable exception )
    {
        super( message, exception ); 
    }
    
}

/*
 * Change Log:
 * $Log: XMLFileParsingException.java,v $
 */




