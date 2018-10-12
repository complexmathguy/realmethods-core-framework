/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.test.junit.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.FrameworkCheckedException;

import com.framework.integration.security.FrameworkSecurityManagerFactory;
import com.framework.integration.security.IFrameworkSecurityManager;
import com.framework.integration.security.ISecurityUser;

import com.framework.test.junit.FrameworkTestCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This class tests the key functionality of all components that comprise the Framework's Security Management service.
 * Testing includes :
 * <ul>
 * <li>Verifying the ability of the FrameworkSecurityManagerFactory to create and load the necessary IFrameworkSecurityManagers
 * from the entries defined in the security.properties file.</li>
 * <li>Verify the default SecurityManager is not null.</li>
 * <li>For each defined Security Manager, verify its attributes and it's ability to authenticate and authorize against
 * the default userid/password combo of 111111111/letmein2.
 * </ul>
 * <p>
 * @author    realMethods, Inc.
 */
public class SecurityManagerServiceTest extends FrameworkTestCase 
{

// constructors
	
    /**
     * construtor
     *
     * @param    testCaseName		unique name applied to this TestCase
     */    
    public SecurityManagerServiceTest( String testCaseName )
    {
        super( testCaseName );
    }
    
    /**
	 * main test driver
     */
    public static void main(String[] args) 
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new SecurityManagerServiceTest( "fullTest" ) );

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
        suite.addTest(new SecurityManagerServiceTest("fullTest"));
        return suite; 
    }
    
// test methods

    /** 
     * full test of the component
     */
    public void fullTest()
    throws Throwable
    {
		new FrameworkBaseObject().logMessage( "*****\nSecurityManagerServiceTest:fullTest() - starting...\n*****" );
		    	
		testFrameworkSecurityManagerFactory();
		testFrameworkDefaultSecurityManager();
		testFrameworkSecurityManagers();
		
		new FrameworkBaseObject().logMessage( "*****\nSecurityManagerServiceTest:fullTest() - passed...\n*****" );			
    }    
    
    /**
     * Tests testFrameworkSecurityManagerFactory
     * <p>
     * @throws Throwable
     */
    public void testFrameworkSecurityManagerFactory()
    throws Throwable 
    {
    	try
    	{
    		assertNotNull( FrameworkSecurityManagerFactory.getInstance() );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "SecurityManagerServiceTest.testFrameworkSecurityManagerFactory() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	new FrameworkBaseObject().logMessage( "SecurityManagerServiceTest.testFrameworkSecurityManagerFactory() - passed..." );      		
    }
    
    
    /**
     * Tests the default Security Manager
     * <p>
     * @throws Throwable
     */
    public void testFrameworkDefaultSecurityManager()
    throws Throwable 
    {
    	IFrameworkSecurityManager defaultSecMgr = null;
    	
    	try
    	{
	    	defaultSecMgr = FrameworkSecurityManagerFactory.getInstance().getDefaultSecurityManager();	
	    		 
			internalCheck( defaultSecMgr );  
		}
		catch( Throwable exc )
		{
			new FrameworkBaseObject().logMessage( "SecurityManagerServiceTest.testFrameworkDefaultSecurityManager() failed  - " + exc.toString() );
            throw exc;  
		}
		
    	new FrameworkBaseObject().logMessage( "SecurityManagerServiceTest.testFrameworkDefaultSecurityManager() - passed..." );      		
    }

    public void testFrameworkSecurityManagers()
    throws Throwable 
    {  	
    	Map managers = null;
    	
    	try
    	{
			managers = FrameworkSecurityManagerFactory.getInstance().getSecurityManagers();
			assertNotNull( managers );   		
    	}
    	catch( Throwable exc )
    	{
			new FrameworkBaseObject().logMessage( "SecurityManagerServiceTest.testFrameworkSecurityManagers() failed due to no FrameworkSecurityManagers contained by the FrameworkSecurityManagerFactory.  Check security.properties and its location. - " + exc.toString() );
            throw exc;     		
    	}
    	
    	// user may point to a different security.properties than the one shipped, so cannot
    	// assume it's size...
    	
    	// iterate through the Map, calling internalCheck
    	Iterator keys 					= managers.keySet().iterator();
    	IFrameworkSecurityManager mgr 	= null;
    	FrameworkCheckedException exception	= null;
    	ArrayList names					= new ArrayList( managers.size() );
    	
    	while( keys.hasNext() )
    	{
    		mgr = (IFrameworkSecurityManager)managers.get( keys.next() );
    		
    		try
    		{
    			internalCheck( mgr );
		    	new FrameworkBaseObject().logMessage( "SecurityManagerServiceTest.testFrameworkSecurityManagers() - passed for sec. mgr " + mgr.getName() + "..." );      		
    			
    		}
    		catch( Throwable exc )
    		{
    			// we want to check them all...and then throw an exception based on the first one
    			
    			if ( exception == null )
    				exception = new FrameworkCheckedException( "SecurityManagerServiceTest.testFrameworkSecurityManagers() on sec.mgr " + mgr.getName() + " failed - " + exc.toString(), exc );
    				
    			// always log the exception
				new FrameworkBaseObject().logMessage( "SecurityManagerServiceTest.testFrameworkSecurityManagers() - " + exc.toString() );    			
    		}
    		
    		if ( names.contains( mgr.getName() ) )
    		{
    			if ( exception == null )
    				exception = new FrameworkCheckedException( "SecurityManagerServiceTest.testFrameworkSecurityManagers() on sec.mgr " + mgr.getName() + " failed - duplicate security manager name " + mgr.getName() );    				
    		}
    		else
    			names.add( mgr.getName() );
    	}
    	
    	if ( exception != null )
    		throw exception;
    		
    	new FrameworkBaseObject().logMessage( "SecurityManagerServiceTest.testFrameworkSecurityManagers() - passed..." );      		
    }
    
    /**
     * Internal helper used to verify an IFrameworkSecurityMananger.
     * 
     * @param	secMgr		test target
     */
    protected void internalCheck( IFrameworkSecurityManager secMgr )
    throws Throwable
    {
    	String mgrName = secMgr.getName();
    	String errMsgPrefix = "SecurityManagerServiceTest.internalCheck() on " + mgrName + " failed";
    	
    	try
    	{
	    	assertNotNull( secMgr );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( errMsgPrefix + " due to a null IFrameworkSecurityManager  - " + exc.toString() );
            throw exc;    		
    	}
    	  
		// verify it's attributes
		String varName = null;
		
		try
		{	 
			varName = "authenticator";
			assertNotNull( secMgr.getAuthenticator() );	
			
			varName = "roleLoader";
			assertNotNull( secMgr.getRoleLoader() );
			
			varName = "userLoader";
			assertNotNull( secMgr.getUserLoader() );     
			
			varName = "name";
			assertNotNull( secMgr.getName() );				  
		}
		catch( Throwable exc )
		{
            throw new FrameworkCheckedException( errMsgPrefix + " due to invalid state for member variable " + varName + " - " + exc.toString() );  
		} 	
		
		// attempt to authorize and gather all the roles for the user
		ISecurityUser securityUser = null;
		
    	try
    	{
	    	securityUser = secMgr.retrieveSecurityUser( userid, password, appid );
	    	assertNotNull( securityUser );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( errMsgPrefix + " due to call to FrameworkSecurityManager.retrieveSecurityUser() which attempts to authenticate the user and load roles for future authorization queries - " + exc.toString() );
            throw exc;    		
    	}		
    	
    	// attempt to see if the user is authorized for the default role
    	try
    	{
    		assertTrue( secMgr.isUserAuthorized( securityUser.getUserID(), role ) );
    	}
    	catch( Throwable exc )
    	{
			new FrameworkBaseObject().logMessage( errMsgPrefix + " to authorize user " + userid + " - " + exc.toString() );
            throw exc;    		
    	}
    	    	    	
    	// attemmpt to unauthenticate the user
    	try
    	{
	    	assertTrue( secMgr.unAuthenticateUser( securityUser ) );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( errMsgPrefix + " unathenticate user  - " + exc.toString() );
            throw exc;    		
    	}    	
    }
    
// attributes

	/**
	 * test user idd
	 */
	private String userid	= "111111111";	
	/**
	 * test password
	 */
	private String password	= "letmein2";
	/**
	 * application identifier
	 */	
	private String appid	= "Framework JUnit Test";
	/**
	 * test role name
	 */
	private String role		= "Employee";
        
}
    
/*
 * Change Log:
 * $Log: SecurityManagerServiceTest.java,v $
 * Revision 1.1  2003/10/08 01:59:46  tylertravis
 * initial version
 *
 */    
