/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.test.junit;

import java.util.Iterator;
import java.util.Map;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.properties.PropertyFileLoader;
import com.framework.common.properties.IFrameworkPropertiesHandler;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * FrameworkPropertiesHandler Test class
 * <p>
 * This class tests the FrameworkPropertiesHandler as follows:
 * <ul>
 * <li>Acquire a singleton instance from the FrameworkPropertiesHandlerFactory</li>
 * <li>Test all loader property handlers for non-null condition.</li>
 * </ul>
 * <p>
 * In order to test loading properties from a file, specify the fully qualified directory
 * of the framework.xml file using the following System parameter:
 * <p>
 * <i>-DPROPERTIES_LOCATION=</i>
 * <p>
 * For example:
 * <br>
 * -DPROPERTIES_LOCATION=c:/realmethods/test/junit/properties
 * <p>
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkPropertyLoaderTest extends TestCase 
{

// constructors
	
    /**
     * construtor
     *
     * @param    testCaseName		unique name applied to this TestCase
     */    
    public FrameworkPropertyLoaderTest( String testCaseName )
    {
        super( testCaseName );

    }
    
    /**
	 * main test driver
     */
    public static void main(String[] args) 
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new FrameworkPropertyLoaderTest( "fullTest" ) );

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
        suite.addTest(new FrameworkPropertyLoaderTest("fullTest"));
        return suite; 
    }
    
// test methods

    /** 
     * full test of the component
     */
    public void fullTest()
    throws Throwable
    {
		new FrameworkBaseObject().log( "*****\nFrameworkPropertyLoaderTest:fullTest() - starting...\n*****" );			
    	
		testGetInstance();
		testPropertyHandlers();
		
		new FrameworkBaseObject().log( "*****\nFrameworkPropertyLoaderTest:fullTest() - passed...\n*****" );			
		
    }    
    
    /**
     * Tests getObject() on FrameworkPropertiesHandlerFactory
     * <p>
     * @throws Throwable
     */
    public void testGetInstance()
    throws Throwable 
    {
    	try
    	{
    		assertNotNull( PropertyFileLoader.getInstance() );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "FrameworkPropertyLoaderTest.testGetInstance() failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	new FrameworkBaseObject().log( "FrameworkPropertyLoaderTest:testGetInstance() - passed..." );
    	
    }
    
        
    /**
     * Tests for non-null on all loaded property handlers
     * <p>
     * @throws Throwable
     */
    public void testPropertyHandlers()
    throws Throwable 
    {    
		String name 						= null;
		IFrameworkPropertiesHandler handler = null;
			
    	try
    	{
    		Map handlers 				= PropertyFileLoader.getInstance().getPropertyHandlers();
    		Iterator iter				= handlers.keySet().iterator();
    		
    		while( iter.hasNext() )
    		{
    			name = (String)iter.next();
    			handler = (IFrameworkPropertiesHandler)handlers.get( name );
    			assertNotNull( handler );
    		}
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "FrameworkPropertyLoaderTest.testPropertyHandlers() failed in non-null test on " + name + " property handler." );
            throw exc;    		
    	}    	
    	
    	new FrameworkBaseObject().log( "FrameworkPropertyLoaderTest:testPropertyHandlers() - passed..." );
    	
    }
           

// attributes

	/**
	 * JNDI name used to bind and lookup an arbitrary test object
	 */
	static protected String contextName	= "JUnitTest";
}
    
/*
 * Change Log:
 * $Log: FrameworkPropertyLoaderTest.java,v $
 *
 */    
