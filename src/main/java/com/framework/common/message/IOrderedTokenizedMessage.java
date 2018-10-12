/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.message;

//************************************
// Imports
//************************************
import java.util.Collection;

/**
 * Provides the notion of a tokenized message with ordered values, for parsing purposes
 *
 * <p>
 * @author    realMethods, Inc.
 */
public interface IOrderedTokenizedMessage extends ITokenizedMessage
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * returns the index within a Collection of Strings
     * 
     * @param       zeroBasedindex
     * @return      String
     */
    public String getValue( int zeroBasedindex )
    throws IndexOutOfBoundsException;

    /**
     * returns a Collection of the parsed values
     *
     * @return       Collection
     */
    public Collection getValues();
}

/*
 * Change Log:
 * $Log: IOrderedTokenizedMessage.java,v $
 */
