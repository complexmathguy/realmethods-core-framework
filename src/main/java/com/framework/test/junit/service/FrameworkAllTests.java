/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/

package com.framework.test.junit.service;

import com.framework.test.junit.DBParameterTest;
import com.framework.test.junit.EJBServiceLocatorTest;
import com.framework.test.junit.FrameworkCheckedExceptionTest;
import com.framework.test.junit.FrameworkManagerTest;
import com.framework.test.junit.FrameworkPropertyLoaderTest;
import com.framework.test.junit.FrameworkUIDGeneratorTest;
import com.framework.test.junit.MessageSuiteTest;
import com.framework.test.junit.StandardJMXServerFactoryTest;
import com.framework.test.junit.UserSessionObjectManagerTest;
import com.framework.test.junit.ValueObjectListProxyTest;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * TestSuite that runs all Framework JUnit Test Cases.
 * <p>
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkAllTests 
{
	public static void main(String[] args) 
	{
		junit.textui.TestRunner.run(suite());
	}
	
	/**
	 * Method to invoke by the JUnit TestRunner.
	 * @return	a  JUnit TestSuite
	 */
	public static Test suite() 
	{ 
		TestSuite suite= new TestSuite("Framework All Test");
		
		suite.addTestSuite(FrameworkManagerTest.class);
		suite.addTestSuite(LogServiceTest.class);
		suite.addTestSuite(SecurityManagerServiceTest.class);		
		suite.addTestSuite(HookManagerServiceTest.class);
		suite.addTestSuite(ValueObjectNotificationServiceTest.class);
		suite.addTestSuite(CommandTaskServiceTest.class);		
		suite.addTestSuite(DBParameterTest.class);
		suite.addTestSuite(FrameworkCheckedExceptionTest.class);
		suite.addTestSuite(FrameworkPropertyLoaderTest.class);
		suite.addTestSuite(FrameworkUIDGeneratorTest.class);
		suite.addTestSuite(MessageSuiteTest.class);
		suite.addTestSuite(EJBServiceLocatorTest.class);
		suite.addTestSuite(StandardJMXServerFactoryTest.class);
		suite.addTestSuite(UserSessionObjectManagerTest.class);
		suite.addTestSuite(ValueObjectListProxyTest.class);
		
		//this test must be last since it will empty ConnectionPools required
		// of some of the other tests above...
		suite.addTestSuite(ConnectionPoolServiceTest.class);
				
		return suite;
	}
}

/*
 * Change Log:
 * $Log: FrameworkAllTests.java,v $
 */
 
