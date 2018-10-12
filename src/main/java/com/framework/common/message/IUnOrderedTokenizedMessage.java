/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.message;

//************************************
// Imports
//************************************
import java.util.Map;

/**
 * Provides the notion of a tokenized message with unordered values, for parsing purposes.
 * A key/value pair should be maintained within the implementation
 *
 * <p>
 * @author    realMethods, Inc.
 */
public interface IUnOrderedTokenizedMessage
    extends ITokenizedMessage
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * returns the value corresponding to the provided key
     * 
     * @param       key
     * @return      String
     */
    public String getValue( String key );

    /**
     * returns a Map of the parsed values as a Map
     *
     * @return       values as map
     */
    public Map getValues();

    /**
     * returns the token delimitter used for the key/value pairings
     *
     * @return      String
     */
    public String getKeyValueDelim();

    /**
     * Set the message and associate the key_value_delim
     *
     * @param 		msg   			target to tokenize
     * @param       keyValueDelim     what separates each value of the message     
     */
    public void setMessage( String msg, String keyValueDelim );
}

/*
 * Change Log:
 * $Log: IUnOrderedTokenizedMessage.java,v $
 */
