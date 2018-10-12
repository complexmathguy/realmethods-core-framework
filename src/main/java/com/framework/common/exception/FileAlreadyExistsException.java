/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.exception;

//***********************************
// Imports
//***********************************

/**
 * Base Exception class for database related actions
 * 
 * <p>
 * @author    realMethods, Inc.
 */
public class FileAlreadyExistsException extends FrameworkCheckedException
{

//************************************************************************    
// Public Methods
//************************************************************************

    /** 
     * Base constructor.
     */
    public FileAlreadyExistsException()
    {
        super();
    }

    /** 
     * Constructor with message.
     *
     * @param message   text of the exception
     */
    public FileAlreadyExistsException( String message )
    {
        super( message );    
    }

    /**
     * Constructor with a Throwable for chained exception and a message.
     * 
     * @param message
     * @param exception
     */
    public FileAlreadyExistsException( String message, Throwable exception )
    {
        super( message, exception ); 
    }

	/**
	 * Returns the file name
	 * 
	 * @return String
	 */
    final public String getFileName()
    {
        return( fileName );
    }

	/**
	 * Assign the file name
	 * 
	 * @param fileName
	 */
    final public void setFileName( String fileName )
    {
        this.fileName = fileName; 
    }

	/**
	 * Returns the file path
	 * 
	 * @return String
	 */
    final public String getFilePath()
    {
        return( filePath );
    }

	/**
	 * Assigns the file path
	 * 
	 * @param filePath
	 */
    final public void setFilePath( String filePath)
    {
        this.filePath = filePath; 
    }


//************************************************************************    
// Private / Protected Methods
//************************************************************************

//************************************************************************    
// Attributes
//************************************************************************
    private String fileName = null;
    private String filePath = null;
}

/*
 * Change Log:
 * $Log: FileAlreadyExistsException.java,v $
 */



