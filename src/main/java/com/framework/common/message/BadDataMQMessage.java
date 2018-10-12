/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.message;

//************************************
// Imports
//************************************

/**
 * Message class for bad MQ related data, used more for identity than an
 * overriding implementation atop FrameworkMQMessage.
 * <p>
 * @author    realMethods, Inc.
 */
public class BadDataMQMessage extends FrameworkMQMessage
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor - deter creation
     */
    protected BadDataMQMessage()
    {
    }

    /**
     * @param       msg
     */
    public BadDataMQMessage( String msg )
    {
        super( msg );
    }

    /**
     * Returns the associated message in String form
     * 
     * @return      String
     */
    public String getMessageAsString()
    {
        return( super.getMessageAsString() );
    }   
}

/*
 * Change Log:
 * $Log: BadDataMQMessage.java,v $
 */




