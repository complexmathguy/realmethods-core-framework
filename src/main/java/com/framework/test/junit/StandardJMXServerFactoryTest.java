/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.test.junit;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.jmx.StandardJMXServerFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * StandardJMXServerFactory Test class
 * <p>
 * This class tests the ability of the StandardJMXServerFactory to create non-null JMX MBeanServer.
 * <p>
 * <p>
 * @author    realMethods, Inc.
 */
public class StandardJMXServerFactoryTest extends TestCase 
{

// constructors
	
    /**
     * construtor
     *
     * @param    testCaseName		unique name applied to this TestCase
     */    
    public StandardJMXServerFactoryTest( String testCaseName )
    {
        super( testCaseName );

    }
    
    /**
	 * main test driver
     */
    public static void main(String[] args) 
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new StandardJMXServerFactoryTest( "fullTest" ) );

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
        suite.addTest(new StandardJMXServerFactoryTest("fullTest"));
        return suite; 
    }
    
// test methods

    /** 
     * full test of the component
     */
    public void fullTest()
    throws Throwable
    {
		new FrameworkBaseObject().logMessage( "*****\nStandardJMXServerFactoryTest:fullTest() - starting...\n*****" );			
    	
		testCreateMBeanServer();
		
		new FrameworkBaseObject().logMessage( "*****\nStandardJMXServerFactoryTest:fullTest() - passed...\n*****" );			
    }    
    
    /**
     * Tests createMBeanServer() on StandardJMXServerFactory
     * <p>
     * @throws Throwable
     */
    public void testCreateMBeanServer()
    throws Throwable 
    {
    	try
    	{
    		assertNotNull( new StandardJMXServerFactory().createMBeanServer() );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "StandardJMXServerFactory.testGetObject() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().logMessage( "StandardJMXServerFactory.testGetObject() - passed..." );      		
    }
    
}
    
/*
 * Change Log:
 * $Log: StandardJMXServerFactoryTest.java,v $
 *
 */    
