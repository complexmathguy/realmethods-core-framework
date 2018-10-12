/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.test.junit;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.mgr.FrameworkManagerFactory;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * FrameworkManager Test class
 * <p>
 * This class tests the FrameworkManager as follows:
 * <ul>
 * <li>Acquire a singleton instance from the FrameworkManagerFactory, causing all properties and resources to be loaded and created
 * from log.properties, connectionpool.properties, framework.xml, and task.properties.</li>
 * <li>Tests the state of the hasBeenRun flag</li>
 * <li>Tests for a non-null return value for getTaskExecutionJMSImpl().  Requires the successful configuration and
 * loading of the Task Execution JMS connection as specified in the connectionpool.properties.</li>
 * <li>Tests for a non-null return value for getFrameworkEventJMSImpl().  Requires the successful configuration and
 * loading of the Framework Event JMS connection as specified in the connectionpool.properties.</li>
 * <li>Tests for a non-null return value for getDefaultLDAPConnectionImpl().  Requires the successful configuration and
 * loading of the default LDAP connection as specified in the connectionpool.properties. <b>This is commented out</b></li>
 * <li>Tests for a non-null return value for getProperties().  Requires the successful locating and loading of
 * the framework.xml file.  See FrameworkPropertiesHandler.</li>
 * <li> Call executeTask() using com.framemwork.test.junit.etc.TestTask</li>
 * </ul>
 * <p>
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkManagerTest extends FrameworkTestCase 
{

// constructors
	
    /**
     * construtor
     *
     * @param    testCaseName		unique name applied to this TestCase
     */    
    public FrameworkManagerTest( String testCaseName )
    {
        super( testCaseName );

    }
    
    /**
	 * main test driver
     */
    public static void main(String[] args) 
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new FrameworkManagerTest( "fullTest" ) );

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
        suite.addTest(new FrameworkManagerTest("fullTest"));
        return suite; 
    }
    
// test methods

    /** 
     * full test of the component
     */
    public void fullTest()
    throws Throwable
    {
		new FrameworkBaseObject().log( "*****\nFrameworkManagerTest:fullTest() - starting...\n*****" );			
    	
		testConstruction();
		testGetTaskExecutionJMSImpl();
		testGetFrameworkEventJMSImpl();
//		testGetDefaultLDAPConnectionImpl();
		testGetProperties();
		testExecuteTask();
		
		new FrameworkBaseObject().log( "*****\nFrameworkManagerTest:fullTest() - passed...\n*****" );					
    }    
    
    /**
     * Tests getObject() on FrameworkManagerFactory
     * <p>
     * @throws Throwable
     */
    public void testConstruction()
    throws Throwable 
    {
    	try
    	{
    		assertNotNull( FrameworkManagerFactory.getObject() );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "FrameworkManagerTest.testGetInstance() failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	new FrameworkBaseObject().log( "FrameworkManagerTest:testGetInstance() - passed..." );
    	
    }
   
    
    /**
     * Tests getTaskExecutionJMSImpl() on FrameworkManager
     * <p>
     * @throws Throwable
     */
    public void testGetTaskExecutionJMSImpl()
    throws Throwable 
    {
    	try
    	{  
    		assertNotNull( FrameworkManagerFactory.getObject().getTaskExecutionJMSImpl() );  		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "FrameworkManagerTest.testGetTaskExecutionJMSImpl() failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	new FrameworkBaseObject().log( "FrameworkManagerTest:testGetTaskExecutionJMSImpl() - passed..." );    	
    }
    
    /**
     * Tests getFrameworkEventJMSImpl() on FrameworkManager
     * <p>
     * @throws Throwable
     */
    public void testGetFrameworkEventJMSImpl()
    throws Throwable 
    {
    	try
    	{   
			assertNotNull( FrameworkManagerFactory.getObject().getFrameworkEventJMSImpl() );     				
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "FrameworkManagerTest.testGetFrameworkEventJMSImpl() failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	new FrameworkBaseObject().log( "FrameworkManagerTest:testGetFrameworkEventJMSImpl() - passed..." );    	
    }
    
    /**
     * Tests getProperties() on FrameworkManager
     * <p>
     * @throws Throwable
     */
    public void testGetProperties()
    throws Throwable 
    {
    	try
    	{  
			assertNotNull( FrameworkManagerFactory.getObject().getProperties() );     		 		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "FrameworkManagerTest.testGetProperties() failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	new FrameworkBaseObject().log( "FrameworkManagerTest:testGetProperties() - passed..." );    	
    }
    
    /**
     * Tests getDefaultLDAPConnectionImpl() on FrameworkManager
     * <p>
     * @throws Throwable
     */
/*    
    public void testGetDefaultLDAPConnectionImpl()
    throws Throwable 
    {
    	try
    	{  
			assertNotNull( FrameworkManagerFactory.getObject().getDefaultLDAPConnectionImpl() );     		 		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "FrameworkManagerTest.testGetDefaultLDAPConnectionImpl() failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	new FrameworkBaseObject().log( "FrameworkManagerTest:testGetDefaultLDAPConnectionImpl() - passed..." );    	
    }
*/    
    /**
     * Tests executeTask() on FrameworkManager using a dummy task named TestTask located in
     * the provided task.properties.
     * <p>
     * @throws Throwable
     */
    public void testExecuteTask()
    throws Throwable 
    {
    	try
    	{
    		FrameworkManagerFactory.getObject().executeTask( "TestTask", null );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "FrameworkManagerTest.testExecuteTask() failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	new FrameworkBaseObject().log( "FrameworkManagerTest:testExecuteTask() - passed..." );    	
    }                 
    

}
    
/*
 * Change Log:
 * $Log: FrameworkManagerTest.java,v $
 *
 */    
