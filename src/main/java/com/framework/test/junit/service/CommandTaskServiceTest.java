/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.test.junit.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.message.TaskJMSMessage;

import com.framework.common.mgr.FrameworkManagerFactory;

import com.framework.integration.task.IFrameworkTask;
import com.framework.integration.task.ITaskJMSExecutionRegistry;
import com.framework.integration.task.TaskJMSExecutionHandler;
import com.framework.integration.task.TaskJMSExecutionRegistryFactory;

import com.framework.test.junit.FrameworkTestCase;

import com.framework.test.junit.etc.TestCommand;
import com.framework.test.junit.etc.TestTask;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This class tests all of the major functionality of the different components that comprise the Framework
 * Command/Task service. This includes : 
 * <ul>
 * <li>Tests getObject() on the TaskJMSExecutionRegistryFactory, to create a single instance
 * of a ITaskJMSExecutionRegistry.  It then registers a task for possible execution.
 * <li>Tests the construction and state of TaskJMSMessage.
 * <li>Tests getTaskJMSExecutionHandlers() on ITaskJMSExecutionRegistry and iterates through
 * the returned HashMap and calls getFrameworkTask() and tests for non-null.  Finally, each
 * TaskJMSExecutionHandler's onMessage() is called, via the FrameworkManager.executeTask() method,
 * causing a JMS notification.  This last test relies on the successful creation of the JMS 
 * connection <i>FrameworkTaskExecutionJMS</i>.
 * <li>Tests all registered Tasks by attempting to execute each.  Since a particular task implementation
 * may require a specific implementation of IFrameworkMessage be provided as an argument, this
 * test may not work completely.
 * <li>Tests the FrameworkTask base class by creating a TestTask, binding a Collection of TestCommands, and
 * then verifying the Collection.  Next, bind a FrameworkMessage and then execute the task.
 * </ul>
 * <p>
 * For this test to run successfully, make sure the JMS connection for the FrameworkTaskExecutionJMS is configured
 * correctly both within the .\properties\connectionpool.properties file as well as the JMS settings
 * for this Topic within your app. server.
 * <p>
 * <p>
 * @author    realMethods, Inc.
 */
public class CommandTaskServiceTest extends FrameworkTestCase 
{

// constructors
	
    /**
     * construtor
     *
     * @param    testCaseName
     */    
    public CommandTaskServiceTest( String testCaseName )
    {
        super( testCaseName );

    }
    
    /**
	 * main test driver
     */
    public static void main(String[] args) 
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new CommandTaskServiceTest( "fullTest" ) );

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
        suite.addTest(new CommandTaskServiceTest("fullTest"));
        return suite; 
    }
    
// test methods

    /** 
     * full test of the component
     */
    public void fullTest()
    throws Throwable
    {    		
		new FrameworkBaseObject().logMessage( "*****\nCommandTaskServiceTest:fullTest() - starting...\n*****" ); 
		
		testTaskJMSExecutionRegistry();
		testTaskJMSMessage();
		testTaskJMSExecutionHandlers();
		testRegisteredTasks();
		testTask();
						
		new FrameworkBaseObject().logMessage( "*****\nCommandTaskServiceTest:fullTest() - passed...*****" );			
    }    
    
    /**
     * Tests getObject() on the TaskJMSExecutionRegistryFactory, to create a single instance
     * of a ITaskJMSExecutionRegistry.  It then registers a task for possible execution.
     * <p>
     * @throws Throwable
     */
    public void testTaskJMSExecutionRegistry()
    throws Throwable 
    {
    	ITaskJMSExecutionRegistry registry 	= null;
    	
    	try
    	{
    		registry = TaskJMSExecutionRegistryFactory.getInstance().getObject();    		
    		assertNotNull( registry );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "CommandTaskServiceTest.testTaskJMSExecutionRegistry() failed to create an ITaskJMSExecutionRegistry from the TaskJMSExecutionRegistryFactory - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	try
    	{
			registry.registerTask( "Task=com.framework.test.junit.etc.TestTask", testExecutionMsg );			
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "CommandTaskServiceTest.testTaskJMSExecutionRegistry() failed register a Task with the TaskJMSExecutionRegistry - " + exc.toString() );
            throw exc;    		
    	}     	
    	
    	new FrameworkBaseObject().logMessage( "CommandTaskServiceTest:testTaskJMSExecutionRegistry() - passed..." );
    	
    }    
    
    /**
     * Tests the construction and state of TaskJMSMessage.
     * <p>
     * @throws Throwable
     */
    public void testTaskJMSMessage()
    throws Throwable 
    {
    	TaskJMSMessage taskMsg 			= null;
    	String msg						= "test-message";
    	String arg						= "test-argument";
    	boolean handleAsTransaction	= true;
    	
    	// construction
    	try
    	{
    		taskMsg = new TaskJMSMessage( msg, arg, handleAsTransaction ); 		
    		assertNotNull( taskMsg );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "CommandTaskServiceTest.testTaskJMSMessage() failed to create an ITaskJMSExecutionRegistry from the TaskJMSExecutionRegistryFactory - " + exc.toString() );
            throw exc;    		
    	}    	    	   	
    	
    	// test state of attributes
    	String varName = null;
    	
    	try
    	{
    		varName = "message";
    		assertEquals( arg, taskMsg.getMessage() );
    		
    		varName = "jmsString";
    		assertEquals( msg, taskMsg.getJMSString() );
    		
    		varName = "handleAsTransaction";
    		assertTrue( taskMsg.handleAsTransaction() );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "CommandTaskServiceTest.testTaskJMSMessage() failed to validate state for member variable " + varName + " - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	new FrameworkBaseObject().logMessage( "CommandTaskServiceTest:testTaskJMSMessage() - passed..." );
    	
    } 
        
    /**
     * Tests getTaskJMSExecutionHandlers() on ITaskJMSExecutionRegistry and iterates through
     * the returned HashMap and calls getFrameworkTask() and tests for non-null.  Finally, each
     * TaskJMSExecutionHandler's onMessage() is called, via the FrameworkManager.executeTask() method,
     * causing a JMS notification.  This last test relies on the successful creation of the JMS 
     * connection <i>FrameworkTaskExecutionJMS</i>.
*  <p>
     * @throws Throwable
     */
    public void testTaskJMSExecutionHandlers()
    throws Throwable 
    {
    	ITaskJMSExecutionRegistry registry 	= null;
    	HashMap handlers					= null;
    	
    	try
    	{
    		registry = TaskJMSExecutionRegistryFactory.getInstance().getObject();
    		handlers = registry.getTaskJMSExecutionHandlers();
    		assertNotNull( handlers ); 		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "CommandTaskServiceTest.testTaskJMSExecutionHandlers() failed with a null HashMap of TaskJMSExeutionHandlers - " + exc.toString() );
            throw exc;    		
    	}    	

		// iterate through the HashMap of TaskJMSExecutionHandlers and test for non-null to call 
		// getFrameworkTask()
		
		Iterator keys 					= handlers.keySet().iterator();
		TaskJMSExecutionHandler handler = null;
		IFrameworkTask task				= null;
		
		while( keys.hasNext() )
		{
			handler = (TaskJMSExecutionHandler)handlers.get( keys.next() );
			task	= handler.getFrameworkTask();
			
			try
			{
				assertNotNull( task );
			}
			catch( Throwable exc )
			{
				new FrameworkBaseObject().logMessage( "CommandTaskServiceTest.testTaskJMSExecutionHandlers() failed nonNull test on TaskJMSExecutionHandler.getFrameworkTask() - " + exc.toString() );
    	        throw exc; 				
			}
			
			// go through the backdoor via the FrameworkManager to more thoroughly test this
			try
			{
				FrameworkManagerFactory.getObject().executeTask( handler.getExecutionMsg(), null );
			}
			catch( Throwable exc )
			{
				new FrameworkBaseObject().logMessage( "CommandTaskServiceTest.testTaskJMSExecutionHandlers() failed to call FrameworkManager.executeTask(...) for task " + handler.getExecutionMsg() + "- " + exc.toString() );
    	        throw exc; 				
			}			
		}
		    	
    	new FrameworkBaseObject().logMessage( "CommandTaskServiceTest:testTaskJMSExecutionHandlers() - passed..." );    	
    }     
    
    /**
     * Tests all registered Tasks by attempting to execute each.  Since a particular task implementation
     * may require a specific implementation of IFrameworkMessage be provided as an argument, this
     * test may not work completely.
     * <p>
     * @throws Throwable
     */
    public void testRegisteredTasks()
    throws Throwable 
    {
    	ITaskJMSExecutionRegistry registry 	= null;
    	HashMap handlers					= null;
    	
    	try
    	{
    		registry = TaskJMSExecutionRegistryFactory.getInstance().getObject();
    		handlers = registry.getTaskJMSExecutionHandlers();
    		assertNotNull( handlers ); 		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "CommandTaskServiceTest.testRegisteredTasks() failed with a null HashMap of TaskJMSExeutionHandlers - " + exc.toString() );
            throw exc;    		
    	}    	

		// iterate through the HashMap of TaskJMSExecutionHandlers and test for non-null to call 
		// getFrameworkTask()
		
		Iterator keys 					= handlers.keySet().iterator();
		TaskJMSExecutionHandler handler = null;
		IFrameworkTask task				= null;
		
		while( keys.hasNext() )
		{
			handler = (TaskJMSExecutionHandler)handlers.get( keys.next() );
			task	= handler.getFrameworkTask();
			
			try
			{
				assertNotNull( task );
			}
			catch( Throwable exc )
			{
				new FrameworkBaseObject().logMessage( "CommandTaskServiceTest.testRegisteredTasks() failed nonNull test on TaskJMSExecutionHandler.getFrameworkTask() - " + exc.toString() );
    	        throw exc; 				
			}
			
			try
			{
				task.execute();
				new FrameworkBaseObject().logMessage( "CommandTaskServiceTest.testRegisteredTasks() successfully executed registerd task " + handler.getExecutionMsg() );
			}
			catch( Throwable exc )
			{
				new FrameworkBaseObject().logMessage( "CommandTaskServiceTest.testRegisteredTasks() failed to execute task - " + exc.toString() );
    	        throw exc; 				
			}			
		}
		    	
    	new FrameworkBaseObject().logMessage( "CommandTaskServiceTest:testRegisteredTasks() - passed..." );    	
    }    
    
    /**
     * Tests the FrameworkTask base class by creating a TestTask, binding a Collection of TestCommands, and
     * then verifying the Collection.  Next, bind a TaskJMSMessage and then execute the task.
*  <p>
     * @throws Throwable
     */
    public void testTask()
    throws Throwable     
    {
    	TestTask task 	= new TestTask();
    	Collection commands = new ArrayList();
    	
    	commands.add( new TestCommand() );
    	commands.add( new TestCommand() );
    	
		try
		{
			task.bindCommands( commands );
			assertEquals( commands, task.getCommands() );
			
			task.bindMessage( new TaskJMSMessage( "test-message" ) );
			
			task.execute();
		}
		catch( Throwable exc )
		{
			new FrameworkBaseObject().logMessage( "CommandTaskServiceTest.testRegisteredTasks() failed to execute task - " + exc.toString() );
	        throw exc; 				
		}   
		
    	new FrameworkBaseObject().logMessage( "CommandTaskServiceTest:testTask() - passed..." ); 		 	
    }
    
    
// attributes

	/**
	 * test msg  text
	 */
	protected String testExecutionMsg = "testTaskMsg";    
}

/*
 * Change Log:
 * $Log: CommandTaskServiceTest.java,v $
 */ 
 
