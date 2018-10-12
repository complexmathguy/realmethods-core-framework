/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.command;

//************************************
// Imports
//************************************
import com.framework.integration.objectpool.mq.IMQConnectionImpl;

/**
 * Base class for all Command objects associated with an IMQConnectionImpl. 
 * <p>
 * @author    realMethods, Inc.
 */
public abstract class MQCommand extends ResourceConnectionCommand
    implements IMQCommand
{
//****************************************************
// Public Methods
//****************************************************

// Command overload

// IMQCommand implementations

    /**
     * returns the IMQConnectionImpl in use
     *
     * @return     IMQConnectionImpl
     */
    public IMQConnectionImpl getMQConnection()
    {
        return( (IMQConnectionImpl)super.getConnectionInUse() );
    }
    

}

/*
 * Change Log:
 * $Log: MQCommand.java,v $
 */
