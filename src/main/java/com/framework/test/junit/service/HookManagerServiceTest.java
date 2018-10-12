/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.test.junit.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.*;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.event.FrameworkEventType;

import com.framework.common.exception.FrameworkCheckedException;

import com.framework.common.message.FrameworkMessage;

import com.framework.integration.hook.FrameworkHookFactory;
import com.framework.integration.hook.FrameworkHookManager;
import com.framework.integration.hook.ICheckedExceptionThrownHook;
import com.framework.integration.hook.ICommandExecutionFailureHook;
import com.framework.integration.hook.IFrameworkHookManager;
import com.framework.integration.hook.IPostCommandExecuteHook;
import com.framework.integration.hook.IPostTaskExecuteHook;
import com.framework.integration.hook.IPreCommandExecuteHook;
import com.framework.integration.hook.IPreTaskExecuteHook;
import com.framework.integration.hook.ITaskExecutionFailureHook;
import com.framework.integration.hook.IUserSessionBoundHook;
import com.framework.integration.hook.IUserSessionUnBoundHook;

import com.framework.test.junit.FrameworkTestCase;

import com.framework.test.junit.etc.TestCommand;
import com.framework.test.junit.etc.TestTask;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This class will exercise the following:
 * <ul>
 * <li>FrameworkHookFactory creation
 * <li>FrameworkHookManager creation
 * <li>Task related hook interfaces - IPreTaskExecuteHook, IPostTaskExecuteHook, and ITaskExecutionFailureHook.
 * <li>Command related hook interfaces -  IPreCommandExecuteHook, IPostCommandExecuteHook, and ICommandExecutionFailureHook.
 * <li>UserSession related hook interfaces - IUserSessionBoundHook and IUserSessionUnBoundHook
 * </ul>
 * <p>
 * The following hook interfaces require the presence of an HttpServlceRequest, and are best tested within the context of running servlet based application:
 * <ul>
 * <li>IPreHttpServletRequestProcessorHook
 * <li>IPostHttpServletRequestProcessorHook
 * <li>IHttpServletRequestProcessorErrorHook
 * <li>IPrePageRequestProcessorHook
 * <li>IPostPageRequestProcessorHook
 * <li>IPageRequestProcessorErrorHook
 * </ul>
 * <p>
 * All hook implementations are loaded from the values for key <i>FrameworkHookManager</i> as specified in the framework.xml. 
 * Although hook test class names are already specified here, replace with your hook class names when testing.
 * <p>
 * <p>
 * @author    realMethods, Inc.
 */
public class HookManagerServiceTest extends FrameworkTestCase 
{

// constructors
	
    /**
     * construtor
     *
     * @param    testCaseName		unique name applied to this TestCase
     */    
    public HookManagerServiceTest( String testCaseName )
    {
        super( testCaseName );

    }
    
    /**
	 * main test driver
     */
    public static void main(String[] args) 
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new HookManagerServiceTest( "fullTest" ) );

        junit.textui.TestRunner.run( suite );
    }

// TestCase overloads

    /**
     * Overload from JUnit TestCase, providing the method name to invoke for testing.  
     * The other methods can be invoked on their own, but fullTest covers all the bases.
     *
     * @return    junit.framework.Test
     */
    public static Test suite() 
    { 
        TestSuite suite= new TestSuite(); 
        suite.addTest(new HookManagerServiceTest("fullTest"));
        return suite; 
    }
    
    
// test methods

    /** 
     * full test of the component
     */
    public void fullTest()
    throws Throwable
    {
		new FrameworkBaseObject().logMessage( "*****\nHookManagerServiceTest:fullTest() - starting...\n*****" );			
    	
		testFrameworkHookFactory();
		testFrameworkHookManager();
		testFrameworkHooks();
		
		new FrameworkBaseObject().logMessage( "*****\nHookManagerServiceTest:fullTest() - passed...\n*****" );			
		
    }    
    
    /**
     * Tests getInstance() on the FrameworkHookFactory
     * <p>
     * @throws Throwable
     */
    public void testFrameworkHookFactory()
    throws Throwable 
    {
    	try
    	{
    		assertNotNull( FrameworkHookFactory.getInstance() );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "HookManagerServiceTest.testFrameworkHookFactory() failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	new FrameworkBaseObject().logMessage( "HookManagerServiceTest:testFrameworkHookFactory() - passed..." );
    	
    }

    /**
     * Tests the FrameworkHookManager
     * <p>
     * @throws Throwable
     */
    public void testFrameworkHookManager()
    throws Throwable 
    {
    	IFrameworkHookManager manager = null;
    	
    	// attempt to acquire the singleton manager
    	try
    	{
    		manager = FrameworkHookManager.getHookManager();
    		assertNotNull( manager );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "HookManagerServiceTest.testFrameworkHookManager() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	// refresh the hooks, causing the hook class names to be reloaded from the 
    	// framework property handler
    	manager.refreshHooks();
    		    	
    	new FrameworkBaseObject().logMessage( "HookManagerServiceTest:testFrameworkHookManager() - passed..." );
    	
    }

    /**
     * Tests all the available hooks as discovered in the framework.xml file.
     * <p>
     * @throws Throwable
     */
    public void testFrameworkHooks()
    throws Throwable 
    {
		boolean bPassed  = true;   
		String hookType			= null;

		// task related hooks
    	try
    	{
			TestTask task 			= new TestTask();
			Collection commands     = new ArrayList();
			FrameworkMessage msg 	= new FrameworkMessage( "test message");

		
			commands.add( new TestCommand() );
			task.bindCommands( commands );    	
				
    		hookType = FrameworkEventType.PreTaskExecute.getEventType();    						
    		IPreTaskExecuteHook preTaskHook = (IPreTaskExecuteHook)FrameworkHookManager.getHookManager().getHook( hookType );

    		if ( preTaskHook != null )
    		{
    			if ( preTaskHook.canHandle( task, msg ) )
    			{
    				preTaskHook.preTaskExecute( msg, true );
    			} 
    		} 
    		
    		hookType = FrameworkEventType.PostTaskExecute.getEventType();
    		IPostTaskExecuteHook postTaskHook = (IPostTaskExecuteHook)FrameworkHookManager.getHookManager().getHook( hookType );
    		
    		if ( postTaskHook != null )
    		{
    			if ( postTaskHook.canHandle( task, msg ) )
    			{
    				postTaskHook.postTaskExecute( msg, true );
    			} 
    		}
    		
    		hookType = FrameworkEventType.TaskExecutionFailure.getEventType();
    		ITaskExecutionFailureHook taskFailHook = (ITaskExecutionFailureHook)FrameworkHookManager.getHookManager().getHook( hookType );
    		
    		if ( taskFailHook != null )
    		{
   				taskFailHook.taskExecutionFailure( msg, true );
    		}    		    		   		
    	}
       	catch( Throwable exc )
       	{
       		bPassed = false;
			new FrameworkBaseObject().logMessage( "HookManagerServiceTest.testFrameworkHooks() for " + hookType + " failed - " + exc.toString() );    		
    	}
    			
		// commmand related hooks 
    	try
    	{
    		TestCommand command		= new TestCommand();
			TestTask task 			= new TestTask();
			Collection commands 	= new ArrayList();
			FrameworkMessage msg 	= new FrameworkMessage( "test message");

		
			commands.add( command );
			task.bindCommands( commands );    	
				
    		hookType = FrameworkEventType.PreCommandExecute.getEventType();    						
    		IPreCommandExecuteHook preCommandHook = (IPreCommandExecuteHook)FrameworkHookManager.getHookManager().getHook( hookType );

    		if ( preCommandHook != null )
    		{
    			if ( preCommandHook.canHandle( command, msg ) )
    			{
    				preCommandHook.preCommandExecute( msg, task );
    			} 
    		} 
    		
    		hookType = FrameworkEventType.PostCommandExecute.getEventType();
    		IPostCommandExecuteHook postCommandHook = (IPostCommandExecuteHook)FrameworkHookManager.getHookManager().getHook( hookType );
    		
    		if ( postCommandHook != null )
    		{
    			if ( postCommandHook.canHandle( command, msg ) )
    			{
    				postCommandHook.postCommandExecute( msg, task );
    			} 
    		}
    		
    		hookType = FrameworkEventType.CommandExecutionFailure.getEventType();
    		ICommandExecutionFailureHook commandFailHook = (ICommandExecutionFailureHook)FrameworkHookManager.getHookManager().getHook( hookType );
    		
    		if ( commandFailHook != null )
    		{
   				commandFailHook.commandExecutionFailure( msg, task );
    		}    		    		   		
    	}
       	catch( Throwable exc )
       	{
       		bPassed = false;
			new FrameworkBaseObject().logMessage( "HookManagerServiceTest.testFrameworkHooks() for " + hookType + " failed - " + exc.toString() );    		
    	}    		
		
		// user session related hooks
    	try
    	{
			HttpSession userSession 				= null;
			HttpSessionBindingEvent bindingEvent	= null;
			
			// user session bound    		
    		hookType = FrameworkEventType.UserSessionBound.getEventType();
    		IUserSessionBoundHook userBoundHook = (IUserSessionBoundHook)FrameworkHookManager.getHookManager().getHook( hookType );
    		
    		if ( userBoundHook != null )
    		{
   				userBoundHook.process( userSession, bindingEvent );
    		}
    		
    		// user session unbound
    		hookType = FrameworkEventType.UserSessionUnBound.getEventType();
    		IUserSessionUnBoundHook userUnBoundHook = (IUserSessionUnBoundHook)FrameworkHookManager.getHookManager().getHook( hookType );
    		
    		if ( userUnBoundHook != null )
    		{
   				userUnBoundHook.process( userSession, bindingEvent );
    		}   		    		   		
    	}
       	catch( Throwable exc )
       	{
       		bPassed = false;
			new FrameworkBaseObject().logMessage( "HookManagerServiceTest.testFrameworkHooks() for " + hookType + " failed - " + exc.toString() );    		
    	}	

		// checked exeption related hooks
    	try
    	{   		
    		hookType = FrameworkEventType.CheckedExceptionThrown.getEventType();
    		ICheckedExceptionThrownHook checkedUnboundhook = (ICheckedExceptionThrownHook)FrameworkHookManager.getHookManager().getHook( hookType );
    		
    		if ( checkedUnboundhook != null )
    		{
   				checkedUnboundhook.process( new FrameworkCheckedException("test checked exception") );
    		}
    				    		   		
    	}
       	catch( Throwable exc )
       	{
       		bPassed = false;
			new FrameworkBaseObject().logMessage( "HookManagerServiceTest.testFrameworkHooks() for " + hookType + " failed - " + exc.toString() );    		
    	}
		// http related hooks are best tested through a deployable application such as the POC    	
		if ( bPassed == true )			    	
	    	new FrameworkBaseObject().logMessage( "HookManagerServiceTest:testFrameworkHooks() - passed..." );
    	
    }    
    
}
    
/*
 * Change Log:
 * $Log: HookManagerServiceTest.java,v $
 */    
