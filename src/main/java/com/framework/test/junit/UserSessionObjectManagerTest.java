/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.test.junit;

import java.util.ArrayList;
import java.util.Collection;

import com.framework.business.vo.list.ValueObjectListProxy;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.FrameworkCheckedException;

import com.framework.integration.cache.IUserSessionObjectManager;
import com.framework.integration.cache.UserSessionObjectManager;
import com.framework.integration.cache.USOMFactory;

import com.framework.test.junit.etc.TestPrimaryKey;
import com.framework.test.junit.etc.TestValueObject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * UserSessionObjectManager Test class
 * <p>
 * This class tests a com.framework.common.UserSessionObjectManager and com.framework.common.USOMFactory as follows:
 * <ul>
 * <li>USOM construction via the USOMFactory</li>
 * <li>TestValueObject and ValueObjectListProxy assignment using unique TestPrimaryKeys for each</li>
 * <li>Request assigned TestValueObject using the previously provided TestPrimaryKey</li>
 * <li>Query the USOM for containment of the assigned TestValueObject using the previously provided TestPrimaryKey</li>
 * <li>Get all keys cached</li>
 * <li>Request the global USOM cache</li>
 * <li>Clear the USOM of all ValueObjectListProxy types</li>
 * <li>Remove the TestValueObject from the USOM</li>
 * </ul>
 * <p>
 * 
 * <p>
 * @author    realMethods, Inc.
 */
public class UserSessionObjectManagerTest extends TestCase 
{

// constructors
	
    /**
     * construtor
     *
     * @param    testCaseName
     */    
    public UserSessionObjectManagerTest( String testCaseName )
    {
        super( testCaseName );

    }
    
    /**
	 * Main test driver.
	 * @param		args
     */
    public static void main(String[] args) 
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new UserSessionObjectManagerTest( "fullTest" ) );

        junit.textui.TestRunner.run( suite );
    }

// TestCase overloads

    /**
     * Overload from JUnit TestCase, called once before a test is executed.
     * <p>
     * @return    junit.framework.Test
     */
	protected void setUp() 
    throws java.lang.Exception
    {
		primarykey 		= new TestPrimaryKey( "999999999" );
		valueobject		= new TestValueObject();
		listprimarykey	= new TestPrimaryKey( "testlistprimarykey" );    	
    }
    
    /**
     * Overload from JUnit TestCase, providing the method name to invoke for testing.  
     * The other methods can be invoked on their own, but fullTest covers all the bases.
     *
     * @return    junit.framework.Test
     */
    public static Test suite() 
    { 
        TestSuite suite= new TestSuite(); 
        suite.addTest(new UserSessionObjectManagerTest("fullTest"));
        return suite; 
    }
    
// test methods

    /** 
     * full test of the component
     */
    public void fullTest()
    throws Throwable
    {
		new FrameworkBaseObject().logMessage( "*****\nUserSessionObjectManagerTest:fullTest() - starting...\n*****" );			
    	
		testConstructor();
		testAssignValueObject();
		testGetValueObject();
		testContainsKey();
		testGetAllKeysCached();
		testGetGlobalCache();
		testEmptyCacheOfListProxies();
		testRemoveValueObject();
		
		new FrameworkBaseObject().logMessage( "*****\nUserSessionObjectManagerTest:fullTest() - passed...\n*****" );			
    }    
    
    /**
     * Tests the construction on UserSessioObjectManager
     * <p>
     * @throws Throwable
     */
    public void testConstructor()
    throws Throwable 
    {
    	try
    	{
    		assertNotNull( getUSOM() );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "UserSessioObjectManager constructor() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().logMessage( "UserSessioObjectManager - passed..." );      		
    }

    /**
     * Tests the assignValueObject() on UserSessioObjectManager
     * <p>
     * @throws Throwable
     */
    public void testAssignValueObject()
    throws Throwable 
    {
    	try
    	{
    		getUSOM().assignValueObject( primarykey, valueobject );
    		
    		Collection coll = new ArrayList();
    		coll.add( valueobject );
    		
    		ValueObjectListProxy listproxy = new ValueObjectListProxy( coll );
    		   
    		getUSOM().assignValueObject( listprimarykey, listproxy );  		    		 		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "UserSessioObjectManager.testAssignValueObject() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().logMessage( "UserSessioObjectManager.testAssignValueObject() - passed..." );      		
    }
    
    /**
     * Tests the getValueObject() on UserSessioObjectManager
     * <p>
     * @throws Throwable
     */
    public void testGetValueObject()
    throws Throwable 
    {
    	try
    	{
    		assertNotNull( getUSOM().getValueObject( primarykey ) );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "UserSessioObjectManager.testGetValueObject() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().logMessage( "UserSessioObjectManager constructor - passed..." );      		
    }    
    
    /**
     * Tests the containsKey() on UserSessioObjectManager
     * <p>
     * @throws Throwable
     */
    public void testContainsKey()
    throws Throwable 
    {
    	try
    	{
    		assertTrue( getUSOM().containsKey( primarykey ) );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "UserSessioObjectManager.testContainsKey() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().logMessage( "UserSessioObjectManager.testContainsKey() - passed..." );      		
    }       
    
    /**
     * Tests the getAllKeysCached() on UserSessioObjectManager
     * <p>
     * @throws Throwable
     */
    public void testGetAllKeysCached()
    throws Throwable 
    {
    	Collection coll = null;
    	
    	try
    	{
    		coll = getUSOM().getAllKeysCached();
    		
    		assertNotNull( coll );
    		
    		if ( coll.isEmpty() == true )
    			throw new FrameworkCheckedException( "UserSessioObjectManager.testGetAllKeysCached() - Collection is empty" );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "UserSessioObjectManager.testGetAllKeysCached() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().logMessage( "UserSessioObjectManager.testGetAllKeysCached() - passed..." );      		
    } 
        
    /**
     * Tests the getGlobalCache() on UserSessioObjectManager
     * <p>
     * @throws Throwable
     */
    public void testGetGlobalCache()
    throws Throwable 
    {
    	try
    	{
    		assertNotNull( UserSessionObjectManager.getGlobalCache() );    	
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "UserSessioObjectManager.testGetGlobalCache() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().logMessage( "UserSessioObjectManager.testGetGlobalCache() - passed..." );      		
    }    
    
    /**
     * Tests the emptyCacheOfListProxies() on UserSessioObjectManager
     * <p>
     * @throws Throwable
     */
    public void testEmptyCacheOfListProxies()
    throws Throwable 
    {
    	try
    	{
    		getUSOM().emptyCacheOfListProxies();   		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "UserSessioObjectManager.testEmptyCacheOfListProxies() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().logMessage( "UserSessioObjectManager.testEmptyCacheOfListProxies() - passed..." );      		
    } 
       
    /**
     * Tests the removeValueObject() on UserSessioObjectManager
     * <p>
     * @throws Throwable
     */
    public void testRemoveValueObject()
    throws Throwable 
    {
    	try
    	{
    		getUSOM().removeValueObject( primarykey );
    		assertFalse( getUSOM().containsKey( primarykey ) );     		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "UserSessioObjectManager.testRemoveValueObject() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().logMessage( "UserSessioObjectManager.testRemoveValueObject() - passed..." );      		
    } 
    
    /**
     * Helper to create a USOM
     * 
     * @return IUserSessionObjectManager
     * @throws Throwable
     */
    protected IUserSessionObjectManager getUSOM()
    throws Throwable
    {
    	if ( usom == null )
    		usom = new USOMFactory().createUSOM();
    		
    	return( usom );
    }
    
// attributes

	/**
	 * singelton USOM instance
	 */
	private static IUserSessionObjectManager usom 	= null;
	
	/**
	 * key used to cache a TestValueObject
	 */    
	private TestPrimaryKey primarykey 				= null;
	
	/**
	 * IFrameworkValueObject type to put in the USOM 
	 */
	private TestValueObject valueobject				= null;
	
	/**
	 * key used to cache a ValueObjectListProxy
	 */
	private TestPrimaryKey listprimarykey			= null;
                 
}
    
/*
 * Change Log:
 * $Log: UserSessionObjectManagerTest.java,v $
 *
 */    
