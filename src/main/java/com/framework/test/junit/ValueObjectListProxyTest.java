/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.test.junit;

import java.util.ArrayList;

import com.framework.business.vo.list.ValueObjectListProxy;

import com.framework.common.FrameworkBaseObject;

import com.framework.test.junit.etc.TestPrimaryKey;
import com.framework.test.junit.etc.TestValueObject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This class test com.framework.common.ValueObjectListProxy for
 * <ul>
 * <li>construction and population via an ArrayList of com.framework.test.junit.etc.TestValueObject types</li>
 * <li>forward and backward iteration, verifying that the wrapped Collection is not empty</li>
 * <li>contains value object test</li>
 * <li>reset</li>
 * </ul>
 * <p>
 * 
 * <p>
 * @author    realMethods, Inc.
 */
public class ValueObjectListProxyTest extends TestCase 
{

// constructors
	
    /**
     * construtor
     *
     * @param     testCaseName		name of the  test case
     */    
    public ValueObjectListProxyTest( String testCaseName )
    {
        super( testCaseName );

    }
    
    /**
	 * main test driver
     */
    public static void main(String[] args) 
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new ValueObjectListProxyTest( "fullTest" ) );

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
        suite.addTest(new ValueObjectListProxyTest("fullTest"));
        return suite; 
    }
    
// test methods

    /** 
     * full test of the component
     */
    public void fullTest()
    throws Throwable
    {
		new FrameworkBaseObject().logMessage( "*****\nValueObjectListProxyTest:fullTest() - starting...\n*****" );			
    	
		testConstruction();
		testIterator();
		testContainsValueObject();
		testReset();
		
		new FrameworkBaseObject().logMessage( "*****\nValueObjectListProxyTest:fullTest() - passed...\n*****" );			
    }    
    
    /**
     * Tests construction on ValueObjectListProxy
     * <p>
     * @throws Throwable
     */
    public void testConstruction()
    throws Throwable 
    {
    	try
    	{
  			getVOListProxy();
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ValueObjectListProxy.testConstruction() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().logMessage( "ValueObjectListProxy.testConstruction() - passed..." );      		
    }
    
        
    /**
     * Tests iteration on ValueObjectListProxy
     * <p>
     * @throws Throwable
     */
    public void testIterator()
    throws Throwable 
    {
		listproxy = getVOListProxy();    	
		
    	try
    	{  			  			
    		assertTrue( listproxy.hasNext() );
    		
  			while( listproxy.hasNext() )
  				assertNotNull( listproxy.next() );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ValueObjectListProxy.testIterator() foward iteration failed - " + exc.toString() );
            throw exc;    		
    	}    

    	try
    	{
    		assertTrue( listproxy.hasPrevious() );
    		
			while( listproxy.hasPrevious() )
				assertNotNull( listproxy.previous() );		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ValueObjectListProxy.testIterator() reverse iteration failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().logMessage( "ValueObjectListProxy.testIterator() - forward and reverse iteration passed..." );      		
    }
    
    /**
     * Tests containsValueObject() on ValueObjectListProxy
     * <p>
     * @throws Throwable
     */
    public void testContainsValueObject()
    throws Throwable 
    {
    	try
    	{
  			assertTrue( getVOListProxy().containsValueObject( primarykey1 ) );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ValueObjectListProxy.testContainsValueObject() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().logMessage( "ValueObjectListProxy.testContainsValueObject() - passed..." );      		
    }
    
 
    /**
     * Tests reset() on ValueObjectListProxy
     * <p>
     * @throws Throwable
     */
    public void testReset()
    throws Throwable 
    {
    	try
    	{
  			getVOListProxy().reset();
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ValueObjectListProxy.testReset() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().logMessage( "ValueObjectListProxy.testReset() - passed..." );      		
    }
    

	/**
	 * Creates a ValueObjectListProxy once, and returns it.
	 * <p>
	 * @return	the created ValueObjectListProxy
	 * @throws Throwable
	 */        
    protected ValueObjectListProxy getVOListProxy()
    throws Throwable
    {
    	if ( listproxy == null )
    	{
    		ArrayList data =  new ArrayList();
    		data.add( new TestValueObject( primarykey1 ) );
    		data.add( new TestValueObject( primarykey2 ) );
    		
    		listproxy = new ValueObjectListProxy( data );    		
    	}
    	
    	return( listproxy );
    }
    
// attributes

	/**
	 * assistant in the testing
	 */
	private ValueObjectListProxy listproxy = null;
	
	/**
	 * key for a FrameworkValueObject that is created during the test
	 */    
	private TestPrimaryKey primarykey1		= new TestPrimaryKey( "999999999" );
	
	/**
	 * key for a Collection created during the test
	 */
	private TestPrimaryKey primarykey2		= new TestPrimaryKey( "listkey" );    
}
    
/*
 * Change Log:
 * $Log: ValueObjectListProxyTest.java,v $
 */    
