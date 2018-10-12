/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.test.junit.service;

import com.framework.common.FrameworkBaseObject;

import com.framework.integration.notify.IValueObjectNotificationManager;
import com.framework.integration.notify.ValueObjectNotificationEvent;
import com.framework.integration.notify.ValueObjectNotificationManager;
import com.framework.integration.notify.ValueObjectNotificationType;

import com.framework.test.junit.FrameworkTestCase;

import com.framework.test.junit.etc.TestJMSMessageListener;
import com.framework.test.junit.etc.TestPrimaryKey;
import com.framework.test.junit.etc.TestValueObject;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * This class tests the following:
 * <ul>
 * <li>com.framework.common.ValueObjectNotificationType - contained static types and member data state
 * <li>com.framework.common.ValueObjectNotificationEvent -  creation and member data state
 * <li>com.framework.common.ValueObjectNotificationManager - creation, notification, and subscription
 * </ul>
 * <p>
 * To effectively test the ValueObjet Notification Manager, make sure the connectionpool.properties
 * for the JMS connection <i>FrameworkValueObjectNotificationJMS</i> is configure correctly.  This will
 * all the TestJMSMessageListener to be able to subscribe and be notified.
 * <p>
 * @author    realMethods, Inc.
 */
public class ValueObjectNotificationServiceTest extends FrameworkTestCase 
{

// constructors
	
    /**
     * construtor
     *
     * @param    testCaseName		unique name applied to this TestCas
     */    
    public ValueObjectNotificationServiceTest( String testCaseName )
    {
        super( testCaseName );

    }
    
    /**
	 * main test driver
     */
    public static void main(String[] args) 
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new ValueObjectNotificationServiceTest( "fullTest" ) );

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
        suite.addTest(new ValueObjectNotificationServiceTest("fullTest"));
        return suite; 
    }
    
// test methods

    /** 
     * full test of the component
     */
    public void fullTest()
    throws Throwable
    {
		new FrameworkBaseObject().logMessage( "*****\nFrameworkLogServiceTest:fullTest() - starting...\n*****" );
		    	
		testValueObjectNotificationType();
		testValueObjectNotificationEvent();
		testValueObjectNotificationManager();
		
		new FrameworkBaseObject().logMessage( "*****\nFrameworkLogServiceTest:fullTest() - passed...\n*****" );        	
    }
    
    
    /**
     * Tests the ValueObjectNotificationType's static final members Update, Create, and Delete
     * <p>
     * @throws Throwable
     */
    public void testValueObjectNotificationType()
    throws Throwable 
    {
    	try
    	{   	
    		assertTrue( ValueObjectNotificationType.Update.isUpdate() );	
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ValueObjectNotificationServiceTest.testValueObjectNotificationType() failed to test static type Update - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	try
    	{   	
    		assertTrue( ValueObjectNotificationType.Create.isCreate() );	
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ValueObjectNotificationServiceTest.testValueObjectNotificationType() failed to test static type Create - " + exc.toString() );
            throw exc;    		
    	}
    	    
    	try
    	{   	
    		assertTrue( ValueObjectNotificationType.Delete.isDelete() );	
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ValueObjectNotificationServiceTest.testValueObjectNotificationType() failed to test static type Create - " + exc.toString() );
            throw exc;    		
    	}
    	    	    	
    	new FrameworkBaseObject().logMessage( "ValueObjectNotificationServiceTest.testValueObjectNotificationType() - passed..." );      		
    }    
    
    /**
     * Tests the ValueObjectNotificationEvent
     * <p>
     * @throws Throwable
     */
    public void testValueObjectNotificationEvent()
    throws Throwable 
    {
    	ValueObjectNotificationEvent event 	= null;
    	ValueObjectNotificationType type 	= ValueObjectNotificationType.Create;
    	TestValueObject vo					= new TestValueObject( new TestPrimaryKey( "testkey" ) );

    	// construction
    	try
    	{   		
    		event = new ValueObjectNotificationEvent( vo, type );  
    		assertNotNull( event ); 		 	
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ValueObjectNotificationServiceTest.testValueObjectNotificationEvent() failed to construct a ValueObjectNotificationEvent - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	// verify variable assignment
    	String varName	= null;
    	
    	try
    	{   		
    		varName = "valueObject";
    		assertEquals( vo, event.getValueObject() );   		
    		
    		varName = "notificationType";
    		assertEquals( type.toString(), event.getNotificationType().toString() );
    		 	
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ValueObjectNotificationServiceTest.testValueObjectNotificationEvent() failed to construct a ValueObjectNotificationEvent " + " on var " + varName + " - " + exc.toString() );
            throw exc;    		
    	}
    	    	
    	new FrameworkBaseObject().logMessage( "ValueObjectNotificationServiceTest.testValueObjectNotificationEvent() - passed..." );      		
    }
    
    /**
     * Tests the ValueObjectNotificationManager for construction, subscription, and notification
     * <p>
     * @throws Throwable
     */
    public void testValueObjectNotificationManager()
    throws Throwable 
    {
    	IValueObjectNotificationManager mgr = null;
    	TestJMSMessageListener listener		= null;
    	TestValueObject vo					= new TestValueObject( new TestPrimaryKey( "testkey" ) );
    	    	
    	// construction
    	try
    	{   		
    		mgr = ValueObjectNotificationManager.getValueObjectNotificationManager();
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ValueObjectNotificationServiceTest.testValueObjectNotificationManager() failed on construction - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	// attempt to subscribe
    	String correlationID = vo.getFrameworkPrimaryKey().toString();
    	    	
    	try
    	{   		
    		listener = new TestJMSMessageListener();
    		mgr.subscribeForValueObjectNotification( listener, vo, correlationID );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ValueObjectNotificationServiceTest.testValueObjectNotificationManager() failed to subscribe for notification - " + exc.toString() );
            throw exc;    		
    	}     	    	    	
    	
    	// attempt to be notify listeners    	
    	ValueObjectNotificationEvent event = null;
    	try
    	{   		
    		// create event spoofed
			event = new ValueObjectNotificationEvent( vo, ValueObjectNotificationType.Create );
    		mgr.notifyValueObjectListeners( event );
    		
    		// update event spoofed
			event = new ValueObjectNotificationEvent( vo, ValueObjectNotificationType.Update );
    		mgr.notifyValueObjectListeners( event );    		
    		
    		// delete event spoofed
			event = new ValueObjectNotificationEvent( vo, ValueObjectNotificationType.Delete );
    		mgr.notifyValueObjectListeners( event );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ValueObjectNotificationServiceTest.testValueObjectNotificationManager() failed to notify listeners of " + event.toString() + " - " + exc.toString() );
            throw exc;    		
    	}    	
    	new FrameworkBaseObject().logMessage( "ValueObjectNotificationServiceTest.testValueObjectNotificationManager() - passed..." );      		
    }        
}

/*
 * Change Log:
 * $Log: ValueObjectNotificationServiceTest.java,v $
 * Revision 1.1.1.1  2003/09/15 01:16:08  tylertravis
 * initial check-in
 *
 */ 
 
