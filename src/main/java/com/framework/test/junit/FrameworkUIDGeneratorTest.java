/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.test.junit;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.uid.FrameworkUIDGeneratorFactory;
import com.framework.common.uid.IFrameworkUIDGenerator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * FrameworkUIDGenerator Test class
 * <p>
 * This class tests the ability of the FrameworkUIDGeneratorFactory to generate the IFrameworkUIDGenerator
 * implementation as indicated in the framework.xml.  It also tests the implementations ability
 * to generate a UID by calling <i>generateUID( Object )</i> with both a null and non-null Object argument.
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkUIDGeneratorTest extends TestCase 
{

// constructors 
	
    /**
     * construtor
     *
     * @param	testCaseName		unique name applied to the TestCase
     */    
    public FrameworkUIDGeneratorTest( String testCaseName )
    {
        super( testCaseName );

    }
    
    /**
	 * main test driver
     */
    public static void main(String[] args) 
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new FrameworkUIDGeneratorTest( "fullTest" ) );

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
        suite.addTest(new FrameworkUIDGeneratorTest("fullTest"));
        return suite; 
    }
    
// test methods

    /** 
     * full test of the component
     */
    public void fullTest()
    throws Throwable
    {
		new FrameworkBaseObject().log( "*****\nFrameworkUIDGeneratorTest:fullTest() - starting...\n*****" );			
    	
    	testConstruction();
		testGenerateUID();
		
		new FrameworkBaseObject().log( "*****\nFrameworkUIDGeneratorTest:fullTest() - passed...\n*****" );			
    }    


    /**
     * Tests construction on FrameworkUIDGenerator
     * <p>
     * @throws Throwable
     */
    public void testConstruction()
    throws Throwable 
    {
    	try
    	{
			IFrameworkUIDGenerator generator = FrameworkUIDGeneratorFactory.getObject();
			
    		assertNotNull( generator );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "FrameworkUIDGenerator.testGetObject() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().log( "FrameworkUIDGenerator.testConstruction() - passed..." );     	
    }
    
        
    /**
     * Tests generateUID() on FrameworkUIDGenerator
     * <p>
     * @throws Throwable
     */
    public void testGenerateUID()
    throws Throwable 
    {
    	IFrameworkUIDGenerator generator = null;
    	
    	try
    	{
			generator = FrameworkUIDGeneratorFactory.getObject();    		
    		assertNotNull( generator.generateUID(  null ) );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "FrameworkUIDGenerator.testGenerateUID() will null arg failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	try
    	{			   		
    		assertNotNull( generator.generateUID( "1234" ) );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "FrameworkUIDGenerator.testGenerateUID() will non-null arg failed - " + exc.toString() );
            throw exc;    		
    	}  
    	    	
    	new FrameworkBaseObject().log( "FrameworkUIDGenerator.testGenerateUID() - passed..." );      		
    }
    
}
    
/*
 * Change Log:
 * $Log: FrameworkUIDGeneratorTest.java,v $
 */    
