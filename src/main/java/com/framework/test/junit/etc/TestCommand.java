/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.test.junit.etc;

import com.framework.common.exception.CommandExecutionException;

import com.framework.common.message.IFrameworkMessage;

import com.framework.integration.command.FrameworkCommand;

import com.framework.integration.task.IFrameworkTask;

/**
 * TestCommand helper class
 * <p>
 * <p>
 * @author    realMethods, Inc.
 */
public class TestCommand extends FrameworkCommand
{
    /**
     * place where the command is notified to do it's work
     * with respect to the input msg.
     *
     * @param       theMessage
     * @param       task
     * @exception   CommandExecutionException
     * @exception   IllegalArgumentException	if theMessage is null
     *
     */
    public void onExecute( IFrameworkMessage theMessage, IFrameworkTask task )
    throws CommandExecutionException, IllegalArgumentException
    {
 		if  ( theMessage == null )
            throw new IllegalArgumentException( "TestCommand:onExecute(...) - null IFrameworkMessage arg" );
            
		logMessage( "TestCommand.onExecute() successful with message arg " + theMessage.toString() );            

    }
}

/*
 * Change Log:
 * $Log: TestCommand.java,v $
 */
 
