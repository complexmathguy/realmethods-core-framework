/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.test.junit;

import javax.swing.JOptionPane;

import com.framework.common.startup.StartupManager;

import junit.framework.TestCase;

/**
 * FrameworkTestClass class
 * <p>
 * Base class for all framework related junit test cases requiring common functionality,
 * such as Framework startup causing the standard property files to be loaded.
 * <p>
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkTestCase extends TestCase
{
	/**
	 * constructor 
	 * 
	 * @param	testCaseName	unique name applied to this test case
	 */
	public FrameworkTestCase( String testCaseName )
	{
		super( testCaseName );
	}
	
// overloads from TestCase	

    /**
     * Overload from JUnit TestCase, called once before a test is executed.
     *
     * @return    junit.framework.Test
     */
	protected void setUp() 
    throws java.lang.Exception
    {
    	if ( propFilePath == null )
    	{
	    	propFilePath 	=  java.lang.System.getProperty( "PROPERTIES_LOCATION" );
			String msg 		= null;
			 
			if ( propFilePath == null || propFilePath.length() == 0 )
			{
				msg = "setup() for " + getClass().getName() + " failed due to the\nJava system property PROPERTIES_LOCATION not being specified.\nPlease provide the location of the property files.";
	            propFilePath = JOptionPane.showInputDialog( msg );				
			}
		
			if ( propFilePath != null )
				propFilePath =  propFilePath.replace( '\\', java.io.File.separatorChar );
	
			// only start once for any series of tests			
	        StartupManager.getInstance().start( getClass().getName(), propFilePath );				
    	}    			
    }
    
// attributes

	/**
	 * user provided -D System property representing the path to the Framework required property files
	 */
	static protected String propFilePath = null;    
}

/*
 * Change Log:
 * $Log: FrameworkTestCase.java,v $
 */ 
 
