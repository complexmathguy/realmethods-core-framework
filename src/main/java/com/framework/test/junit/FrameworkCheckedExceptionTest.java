/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.test.junit;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.FrameworkCheckedException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * FrameworkCheckedException Test class
 * <p>
 * This class tests the based class FrameworkCheckedException for :
 * <ul>
 * <li>construction</li>
 * <li>Test getCheckedException() for non-null value</li>
 * <li>Test getExceptionID() for non-null value</li>
 * <li>Test the loggedFlag for the default value of false, assign to true and retest</li>
 * <li>Test toString() for non-null value</li>
 * </ul>
 * 
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkCheckedExceptionTest extends TestCase 
{

// constructors
	
    /**
     * construtor
     *
     * @param    testCaseName		unique name applied to this TestCase
     */    
    public FrameworkCheckedExceptionTest( String testCaseName )
    {
        super( testCaseName );

    }
    
    /**
	 * main test driver
     */
    public static void main(String[] args) 
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new FrameworkCheckedExceptionTest( "fullTest" ) );

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
        suite.addTest(new FrameworkCheckedExceptionTest("fullTest"));
        return suite; 
    }
    
// test methods

    /** 
     * full test of the component
     */
    public void fullTest()
    throws Throwable
    {
		new FrameworkBaseObject().log( "*****\nFrameworkCheckedExceptionTest:fullTest() - starting...\n*****" );			
    	
		testConstruction();
		testGetChainedException();
		testGetExceptionID();
		testLoggedFlag();
		testToString();
		
		new FrameworkBaseObject().log( "*****\nFrameworkCheckedExceptionTest:fullTest() - passed...\n*****" );			
    }    
    
    /**
     * Tests constructor(String, Throwable) on FrameworkCheckedException
     * <p>
     * @throws Throwable
     */
    public void testConstruction()
    throws Throwable 
    {
    	try
    	{
    		FrameworkCheckedException exception = new FrameworkCheckedException( "test frameworkcheckedexception", chainedExc );
    		exception.getMessage();
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "FrameworkCheckedException(String, Throwable) failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().log( "FrameworkCheckedException(String, Throwable) - passed..." );      		
    }

    /**
     * Tests getChainedException() on FrameworkCheckedException
     * <p>
     * @throws Throwable
     */
    public void testGetChainedException()
    throws Throwable 
    {
    	try
    	{
    		FrameworkCheckedException exception = new FrameworkCheckedException( "test frameworkcheckedexception", chainedExc );    		   		
    		assertEquals( exception.getChainedException(), chainedExc );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "FrameworkCheckedException.getChainedException() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().log( "FrameworkCheckedException.getChainedException() - passed..." );      		
    }
    
    /**
     * Tests getExceptionID() on FrameworkCheckedException
     * <p>
     * @throws Throwable
     */
    public void testGetExceptionID()
    throws Throwable 
    {
    	try
    	{
    		FrameworkCheckedException exception = new FrameworkCheckedException( "test frameworkcheckedexception", chainedExc );
    		assertNotNull( exception.getExceptionID() );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "FrameworkCheckedException.getExceptionID() notNull failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().log( "FrameworkCheckedException.getExceptionID() - passed..." );      		
    }
    
    /**
     * Tests getIsLogged() and setIsLogged on FrameworkCheckedException
     * <p>
     * @throws Throwable
     */
    public void testLoggedFlag()
    throws Throwable 
    {
   		FrameworkCheckedException exception = new FrameworkCheckedException( "test frameworkcheckedexception", chainedExc );    
    	
    	try
    	{
    		// shouldn't be set by default
    		assertFalse( exception.getIsLogged() );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "FrameworkCheckedException.getIsLogged() true comparison failed - " + exc.toString() );
            throw exc;    		
    	}    

    	try
    	{
    		// assign to true and recheck
    		exception.setisLogged( true );
    		assertTrue( exception.getIsLogged() );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "FrameworkCheckedException.setIsLogged() assignment failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	new FrameworkBaseObject().log( "FrameworkCheckedException getIsLogged() and setIsLogged() - passed..." );      		
    }            
    
    /**
     * Tests toString() on FrameworkCheckedException
     * <p>
     * @throws Throwable
     */
    public void testToString()
    throws Throwable 
    {
   		FrameworkCheckedException exception = new FrameworkCheckedException( "test frameworkcheckedexception", chainedExc );    
    	
    	try
    	{
    		assertNotNull( exception.toString() );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "FrameworkCheckedException.getIsLogged() true comparison failed - " + exc.toString() );
            throw exc;    		
    	}    

    	new FrameworkBaseObject().log( "FrameworkCheckedException.toString() - passed..." );      		
    }                
    
// attributes

	private final Exception chainedExc = new Exception( "test chain exception" );
    
}
    
/*
 * Change Log:
 * $Log: FrameworkCheckedExceptionTest.java,v $
 *
 */    
