/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.test.junit.etc;

import javax.servlet.http.*;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.FrameworkCheckedException;
import com.framework.common.exception.HookProcessException;

import com.framework.common.message.IFrameworkMessage;

import com.framework.integration.command.IFrameworkCommand;

import com.framework.integration.hook.ICheckedExceptionThrownHook;
import com.framework.integration.hook.ICommandExecutionFailureHook;
import com.framework.integration.hook.IHttpServletRequestProcessorErrorHook;
import com.framework.integration.hook.IPageRequestProcessorErrorHook;
import com.framework.integration.hook.IPostCommandExecuteHook;
import com.framework.integration.hook.IPostHttpServletRequestProcessorHook;
import com.framework.integration.hook.IPostPageRequestProcessorHook;
import com.framework.integration.hook.IPostTaskExecuteHook;
import com.framework.integration.hook.IPreCommandExecuteHook;
import com.framework.integration.hook.IPreHttpServletRequestProcessorHook;
import com.framework.integration.hook.IPrePageRequestProcessorHook;
import com.framework.integration.hook.IPreTaskExecuteHook;
import com.framework.integration.hook.ITaskExecutionFailureHook;
import com.framework.integration.hook.IUserSessionBoundHook;
import com.framework.integration.hook.IUserSessionUnBoundHook;

import com.framework.integration.task.IFrameworkTask;

/**
 * Provides an implementation for all com.framework.integration.hook specific interfaces.
 * <p>
 * @author    realMethods, Inc.
 */
public class TestHook extends FrameworkBaseObject
implements IUserSessionBoundHook,IUserSessionUnBoundHook,
IPreTaskExecuteHook, IPostTaskExecuteHook, ITaskExecutionFailureHook,
IPreCommandExecuteHook, IPostCommandExecuteHook, ICommandExecutionFailureHook,
IPreHttpServletRequestProcessorHook, IPostHttpServletRequestProcessorHook, IHttpServletRequestProcessorErrorHook,
IPrePageRequestProcessorHook, IPostPageRequestProcessorHook, IPageRequestProcessorErrorHook,
ICheckedExceptionThrownHook
{
	
    /**
     * The purpose of the hook.  Provides the HttpSessionBindingEvent
     * passed along by the HttpSession just after unbinding.
     * 
     * @param       session
     * @param       event
     * @exception   HookProcessException
     */
    public void process( HttpSession session, HttpSessionBindingEvent event )
    throws HookProcessException
    {
    }	
    
    /**
     * the purpose of the hook
     * 
     * @param       msg
     * @param       handleCommit
     * @exception   HookProcessException     
     */
    public void taskExecutionFailure( IFrameworkMessage msg, boolean handleCommit )
    throws HookProcessException
    {
    }    
    
    /**
     * query method used to determine if the hook can handle hooking
     * based on the provided command and the message to work over
     *
     * @param       task
     * @param       msg
     */
    public boolean canHandle( IFrameworkTask task, IFrameworkMessage msg )
    {
    	return( true );
    }
    
    
    /**
     * the purpose of the hook
     * 
     * @param       msg
     * @param       bHandleCommit
     * @exception   HookProcessException     
     */
    public void preTaskExecute( IFrameworkMessage msg, boolean bHandleCommit )
    throws HookProcessException
    {
    }   
    
    /**
     * the purpose of the hook
     * 
     * @param       msg
     * @param       handleCommit
     * @exception   HookProcessException     
     */
    public void postTaskExecute( IFrameworkMessage msg, boolean handleCommit )
    throws HookProcessException
    {
    }
    
    /**
     * query method used to determine if the hook can handle hooking
     * based on the provided command and the message to work over
     *
     * @param       command
     * @param       msg
     */
    public boolean canHandle( IFrameworkCommand command, IFrameworkMessage msg )
    {
    	return( true );
    }
        
    /**
     * the purpose of the hook
     * 
     * @param       msg
     * @param       task
     * @exception   HookProcessException     
     */
    public void preCommandExecute( IFrameworkMessage msg, IFrameworkTask task )
    throws HookProcessException
    {
    }
    
    /**
     * the purpose of the hook
     * 
     * @param       msg
     * @param       task
     * @exception   HookProcessException
     */
    public void postCommandExecute( IFrameworkMessage msg, IFrameworkTask task )
    throws HookProcessException
    {    	
    }
    
    /**
     * the purpose of the hook
     * 
     * @param       msg
     * @param       task
     * @exception   HookProcessException     
     */
    public void commandExecutionFailure( IFrameworkMessage msg, IFrameworkTask task )
    throws HookProcessException
    {
    }           
    
    /**
     * the purpose of the hook
     * 
     * @param       request
     * @exception   HookProcessException
     */
    public void process( HttpServletRequest request )
    throws HookProcessException
    {
    }    
    
    /**
     * the purpose of the hook
     * 
     * @param       action
     * @param       request
     * @exception   HookProcessException
     */
    public void process( String action, HttpServletRequest request )
    throws HookProcessException
    {
    }
    
	/**
     * the purpose of the hook
     * 
     * @param       exception
     * @exception   HookProcessException     
     */
    public void process( FrameworkCheckedException exception )
    throws HookProcessException
    {
    }    
        
}

/*
 * Change Log:
 * $Log: TestHook.java,v $ial check-in
 *
 */
