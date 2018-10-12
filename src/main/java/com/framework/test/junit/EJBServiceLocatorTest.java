/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.test.junit;

import com.framework.common.FrameworkBaseObject;

import com.framework.integration.locator.EJBServiceLocator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * EJBServiceLocator Test class
 * <p>
 * This class tests the com.framework.common.misc.EJBServiceLocator as follows:
 * <ul>
 * <li>Acquire a singleton instance from the EJBServiceLocator itself</li>
 * <li>Acquire a non-null InitialContext from the EJBServiceLocator</li>
 * <li>Using a known key value, <i>bind</i> an object to JNDI through the EJBServiceLocator</li>
 * <li>Using the known key, <i>lookup</i> the previous bound object in JNDI through the EJBServiceLocator</li>
 * </ul>
 * <p>
 * This class requires the presence of a JNDI provider running in the same JVM as the test class itself.
 * <p>
 * <p>
 * @author    realMethods, Inc.
 */
public class EJBServiceLocatorTest extends TestCase 
{

// constructors
	
    /**
     * construtor
     *
     * @param    testCaseName		unique name applied to this TestCase
     */    
    public EJBServiceLocatorTest( String testCaseName )
    {
        super( testCaseName );

    }
    
    /**
	 * main test driver
     */
    public static void main(String[] args) 
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new EJBServiceLocatorTest( "fullTest" ) );

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
        suite.addTest(new EJBServiceLocatorTest("fullTest"));
        return suite; 
    }
    
// test methods

    /** 
     * full test of the component
     */
    public void fullTest()
    throws Throwable
    {
		new FrameworkBaseObject().log( "*****\nEJBServiceLocatorTest:fullTest() - starting...\n*****" );			
    	
		testGetInstance();
		testGetInitialContext();
		testBind();
		testLookup();
		
		new FrameworkBaseObject().log( "*****\nEJBServiceLocatorTest:fullTest() - passed...\n*****" );			
		
    }    
    
    /**
     * Tests getInstance() on EJBServiceLocator
     * <p>
     * @throws Throwable
     */
    public void testGetInstance()
    throws Throwable 
    {
    	try
    	{
    		assertNotNull( EJBServiceLocator.getInstance() );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "EJBServiceLocatorTest.testGetInstance() failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	new FrameworkBaseObject().log( "EJBServiceLocatorTest:testGetInstance() - passed..." );
    	
    }
    
    /**
     * Tests getInitialContext() on EJBServiceLocator
     * <p>
     * @throws Throwable
     */
    public void testGetInitialContext()
    throws Throwable 
    {
    	try
    	{
    		EJBServiceLocator locator = EJBServiceLocator.getEJBServiceLocator();
    		assertNotNull( locator.getInitialContext() );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "EJBServiceLocatorTest.testGetInitialContext() failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	new FrameworkBaseObject().log( "EJBServiceLocatorTest:testGetInitialContext() - passed..." );    	
    }
    
    /**
     * Tests bind() on EJBServiceLocator
     * <p>
     * @throws Throwable
     */
    public void testBind()
    throws Throwable 
    {
    	try
    	{
    		EJBServiceLocator locator = EJBServiceLocator.getEJBServiceLocator();
    		locator.bind( JNDIKEY, JNDIVALUE );    		
    	}
       	catch( Throwable exc )
		{	
			new FrameworkBaseObject().log( "EJBServiceLocatorTest.testBind() failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	new FrameworkBaseObject().log( "EJBServiceLocatorTest:testBind() - passed..." );
     	
    }
    
    /**
     * Tests lookup() on EJBServiceLocator
     * <p>
     * @throws Throwable
     */
    public void testLookup()
    throws Throwable 
    {
    	try
    	{
    		EJBServiceLocator locator = EJBServiceLocator.getEJBServiceLocator();
    		assertEquals( JNDIVALUE, (String)locator.lookup( JNDIKEY ) );   		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "EJBServiceLocatorTest.testLookup() failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	new FrameworkBaseObject().log( "EJBServiceLocatorTest:testLookup() - passed..." );
     	
    }        

// attributes

	/**
	 * key value applied to the test object when bound to JNDI
	 */
	private final String JNDIKEY 	= "framework-service-locator-test-key";
	
	/**
	 * test object boud to JNDI using the key value for JNDIKEY
	 */
	private final String JNDIVALUE 	= "framework-service-locator-test-value";
}
    
/*
 * Change Log:
 * $Log: EJBServiceLocatorTest.java,v $
 * Revision 1.1.1.1  2003/09/15 01:16:05  tylertravis
 * initial check-in
 *
 */    
