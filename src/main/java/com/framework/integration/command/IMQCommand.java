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
 * Common interface for all MQ related Commands.
 * <p>
 * @author    realMethods, Inc.
 */
public interface IMQCommand extends IFrameworkCommand
{
    /**
     * returns the IMQConnectionImpl in use
     *
     * @return     IMQConnectionImpl
     */
    public IMQConnectionImpl getMQConnection();
}

/*
 * Change Log:
 * $Log: IMQCommand.java,v $
 */
