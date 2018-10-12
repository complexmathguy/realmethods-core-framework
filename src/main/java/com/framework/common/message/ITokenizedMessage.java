/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.message;

//************************************
// Imports
//************************************
import com.framework.common.exception.TokenizeException;

/**
 * Common interface of all tokenized type messages
 *
 * <p>
 * @author    realMethods, Inc.
 */
public interface ITokenizedMessage
    extends IFrameworkMessage
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * Returns the associated message in String form
     * 
     * @return      String
     */
    public String getTokenDelims();

	/**
	 * Set the delimitters used to tokenize the string
	 *
	 * @param	tknDelims		what separates each value of the message
	 */
    public void setTokenDelims( String tknDelims )
    throws IllegalArgumentException;

    /**
     * tokenizes the contained message
     */
    public void tokenize()
    throws TokenizeException;

}

/*
 * Change Log:
 * $Log: ITokenizedMessage.java,v $
 */
