/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.test.junit;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.message.FrameworkMessage;
import com.framework.common.message.OrderedTokenizedMessage;
import com.framework.common.message.UnOrderedTokenizedMessage;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * MessageSuite Test class
 * <p>
 * This class tests the attribute assignments and message parsing capabilities of the following classes:
 * <ul>
 * <li>com.framework.common.message.FrameworkMessage</li>
 * <li>com.framework.common.message.OrderedTokenizedMessage</li>
 * <li>com.framework.common.message.UnOrderedTokenizedMessage</li>
 * </ul>
 * <p>
 * <p>
 * @author    realMethods, Inc.
 */
public class MessageSuiteTest extends TestCase 
{

// constructors
	
    /**
     * construtor
     *
     * @param    testCaseName		unique name applied to this TestCase
     */    
    public MessageSuiteTest( String testCaseName )
    {
        super( testCaseName );

    }
    
    /**
	 * main test driver
     */
    public static void main(String[] args) 
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new MessageSuiteTest( "fullTest" ) );

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
        suite.addTest(new MessageSuiteTest("fullTest"));
        return suite; 
    }
    
// test methods

    /** 
     * full test of the component
     */
    public void fullTest()
    throws Throwable
    {
		new FrameworkBaseObject().log( "*****\nMessageSuiteTest:fullTest() - passed...\n*****" );
    	
		testFrameworkMessage();
		testOrderedTokenizedMessage();
		testUnOrderedTokenizedMessage();
		
		new FrameworkBaseObject().log( "*****\nMessageSuiteTest:fullTest() - passed...\n*****" );
    }    
    
    /**
     * Tests a FrameworkMessage
     * <p>
     * @throws Throwable
     */
    public void testFrameworkMessage()
    throws Throwable 
    {
    	String msg				= "test message";
    	StringBuffer buf		= new StringBuffer( msg );
    	FrameworkMessage fwkMsg	= new FrameworkMessage( buf );    	
    		
    	try
    	{    		
    		assertEquals( fwkMsg.getMessage(), buf );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "MessageSuite.testFrameworkMessage() getObject() comparison failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	try
    	{    		
    		assertEquals( fwkMsg.getMessageAsString(), msg );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "MessageSuite.testFrameworkMessage() getMessageAsString() comparison failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	new FrameworkBaseObject().log( "MessageSuiteTest:testFrameworkMessage() - passed..." );
    }
    
    

    /**
     * Tests a OrderedTokenizedMessage
     * <p>
     * @throws Throwable
     */
    public void testOrderedTokenizedMessage()
    throws Throwable 
    {
    	String msg				= "test,message";
    	String delim			= ",";
    	OrderedTokenizedMessage tknMsg = new OrderedTokenizedMessage( msg, delim );    	
    		
    	try
    	{    		
    		assertEquals( tknMsg.getMessage(), msg );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "MessageSuite.testOrderedTokenizedMessage() getObject() comparison failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	try
    	{    		
    		assertEquals( tknMsg.getMessageAsString(), msg );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "MessageSuite.testOrderedTokenizedMessage() getMessageAsString() comparison failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	try
    	{    		
    		assertEquals( tknMsg.getTokenDelims(), delim );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "MessageSuite.testOrderedTokenizedMessage() getTokenDelims() comparison failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	
    	try
    	{   
			assertNotNull( tknMsg.getValues() );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "MessageSuite.testOrderedTokenizedMessage() getValues() not null test failed - " + exc.toString() );
            throw exc;    		
    	}  
    	
    	try
		{   
    		assertEquals( tknMsg.getValue(0), "test" );
    		assertEquals( tknMsg.getValue(1), "message" );			
    	}
	   	catch( Throwable exc )
   		{
			new FrameworkBaseObject().log( "MessageSuite.testOrderedTokenizedMessage() getValue(int) comparison failed - " + exc.toString() );
            throw exc;    		
		}     		
    	    	
    	new FrameworkBaseObject().log( "MessageSuiteTest:testOrderedTokenizedMessage() - passed..." );
    	
    }   

    /**
     * Tests a FrameworkUnOrderedTokenizedMessage
     * <p>
     * @throws Throwable
     */
    public void testUnOrderedTokenizedMessage()
    throws Throwable 
    {
    	String msg				= "firstName=Joe,lastName=Smith,age=34";
    	String delim			= ",";
    	String keydelim			= "=";
    	UnOrderedTokenizedMessage tknMsg = new UnOrderedTokenizedMessage( msg, delim, keydelim );    	
    		
    	try
    	{    		
    		assertEquals( tknMsg.getMessage(), msg );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "MessageSuite.testUnOrderedTokenizedMessage() getObject() comparison failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	try
    	{    		
    		assertEquals( tknMsg.getMessageAsString(), msg );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "MessageSuite.testUnOrderedTokenizedMessage() getMessageAsString() comparison failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	try
    	{    		
    		assertEquals( tknMsg.getTokenDelims(), delim );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "MessageSuite.testUnOrderedTokenizedMessage() getTokenDelims() comparison failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	try
    	{    		
    		assertEquals( tknMsg.getKeyValueDelim(), keydelim );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "MessageSuite.testUnOrderedTokenizedMessage() getKeyValueDelim() comparison failed - " + exc.toString() );
            throw exc;    		
    	}  
    	    	
    	try
    	{   
			assertNotNull( tknMsg.getValues() );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().log( "MessageSuite.testUnOrderedTokenizedMessage() getValues() notNull test failed - " + exc.toString() );
            throw exc;    		
    	}  
    	
    	try
		{   
    		assertEquals( tknMsg.getValue("firstName"), "Joe" );
    		assertEquals( tknMsg.getValue("lastName"), "Smith" );
			assertEquals( tknMsg.getValue("age"), "34" );
    	}
	   	catch( Throwable exc )
   		{
			new FrameworkBaseObject().log( "MessageSuite.testUnOrderedTokenizedMessage() getValue(String) comparison failed - " + exc.toString() );
            throw exc;    		
		}     		  	
    	
    	new FrameworkBaseObject().log( "MessageSuiteTest:testUnOrderedTokenizedMessage() - passed..." );
    	
    }              
}
    
/*
 * Change Log:
 * $Log: MessageSuiteTest.java,v $
 *
 */    
