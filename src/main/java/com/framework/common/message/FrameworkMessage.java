/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.message;

//************************************
// Imports
//************************************
import com.framework.common.FrameworkBaseObject;

/**    
 * Base class of all Framework related messages
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkMessage extends FrameworkBaseObject
    implements IFrameworkMessage
{
//****************************************************
// Public Methods
//****************************************************
    /**
     * default constructor
     */
    public FrameworkMessage()
    {
    }

    /**
     * constructor
     * 
     * @param       object
     */
    public FrameworkMessage( Object object )
    {
        setMessage( object );
    }

// IFrameworkMessage implementations

    /**
     * Returns the associated message 
     * 
     * @return      String
     */
    public Object getMessage()
    {
        return( message );        
    }

    /**
     * Set the message
     *
     * @param	msg
     */
    public void setMessage( Object msg )
    {
        message = msg;
    }

    /**
     * Returns the associated message in String form
     * 
     * @return      String
     */
    public String getMessageAsString()
    {
        String msg = null;

        if ( message != null )
        {
            msg = message.toString();
        }

        return( msg );
    }

// attributes    

    /**
     * the message
     */
    protected Object message         = null;
}

/*
 * Change Log:
 * $Log: FrameworkMessage.java,v $
 */
