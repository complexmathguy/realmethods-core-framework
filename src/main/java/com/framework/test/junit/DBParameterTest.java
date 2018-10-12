/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.test.junit;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Calendar;
import java.util.Date;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.parameter.ArrayParameter;
import com.framework.common.parameter.BooleanParameter;
import com.framework.common.parameter.ByteArrayParameter;
import com.framework.common.parameter.CharacterParameter;
import com.framework.common.parameter.DateParameter;
import com.framework.common.parameter.DoubleParameter;
import com.framework.common.parameter.FloatParameter;
import com.framework.common.parameter.IntParameter;
import com.framework.common.parameter.LongParameter;
import com.framework.common.parameter.NullParameter;
import com.framework.common.parameter.ShortParameter;
import com.framework.common.parameter.StringParameter;
import com.framework.common.parameter.TimeParameter;
import com.framework.common.parameter.TimestampParameter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * DBParameter Test class
 * <p>
 * This class tests all com.framework.common.misc.Parameter sub-classes by :
 * <br>
 * <ul>
 * <il>Verifying assignment of the correct java.sql.Type
 * <il>Verifying assignment as an input database parameter
 * <il>Verifying assigned wrapped value is non-null
 * </ul>
 * <p>
 * Sub-classes tested include:
 * <br>
 * <ul>
 * <li>com.framework.common.misc.ArrayParameter</li>
 * <li>com.framework.common.misc.BooleanParameter</li>
 * <li>com.framework.common.misc.ByteArrayParameter</li>
 * <li>com.framework.common.misc.CharacterParameter</li>
 * <li>com.framework.common.misc.DateParameter</li>
 * <li>com.framework.common.misc.DoubleParameter</li>
 * <li>com.framework.common.misc.FloatParameter</li>
 * <li>com.framework.common.misc.IntParameter</li>
 * <li>com.framework.common.misc.LongParameter</li>
 * <li>com.framework.common.misc.NullParameter</li>
 * <li>com.framework.common.misc.ShortParameter</li>
 * <li>com.framework.common.misc.StringParameter</li>
 * <li>com.framework.common.misc.TimeParameter</li>
 * <li>com.framework.common.misc.TimestampParameter</li>
 * </ul>
 * 
 * <p>
 * @author    realMethods, Inc.
 */
public class DBParameterTest extends TestCase 
{

// constructors
	
    /**
     * construtor
     *
     * @param    testCaseName		unique name applied to this TestCase
     */    
    public DBParameterTest( String testCaseName )
    {
        super( testCaseName );

    }
    
    /**
	 * main test driver
     */
    public static void main(String[] args) 
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new DBParameterTest( "fullTest" ) );

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
        suite.addTest(new DBParameterTest("fullTest"));
        return suite; 
    }
    
// test methods

    /** 
     * full test of the component
     */
    public void fullTest()
    throws Throwable
    {
		new FrameworkBaseObject().logMessage( "\n*****DBParameterTest:fullTest() - starting...\n*****" );
		    	
		testArrayParameter();
		testBooleanParameter();
		testByteArrayParameter();
		testCharacterParameter();
		testDateParameter();
		testDoubleParameter();
		testFloatParameter();
		testIntParameter();
		testLongParameter();
		testNullParameter();
		testShortParameter();
		testStringParameter();
		testTimeParameter();
		testTimestampParameter();	
		
		new FrameworkBaseObject().logMessage( "\n*****DBParameterTest:fullTest() - passed...\n*****" );
    }    
    
    /**
     * Tests com.framework.common.misc.ArrayParameter
     * <p>
     * @throws Throwable
     */
    public void testArrayParameter()
    throws Throwable 
    {
    	try
    	{
    		Collection coll				= new ArrayList();
    		
    		coll.add( "test" );
    		
    		ArrayParameter param 	= new ArrayParameter( coll, true );
    		    		
	    	try
	    	{    		    		
	    		assertEquals( param.isInParameter(), true );
	    	}
	       	catch( Throwable exc )
	       	{
				new FrameworkBaseObject().logMessage( "DBParameterTest.testArrayParameter() isInParameter() comparison failed - " + exc.toString() );
	            throw exc;    		
	    	}
	    	
	    	try
	    	{      		
	    		assertEquals( param.getType(), java.sql.Types.VARBINARY );    
	    	}
	       	catch( Throwable exc )
	       	{
				new FrameworkBaseObject().logMessage( "DBParameterTest.testArrayParameter() getType() comparison failed - " + exc.toString() );
	            throw exc;    		
	    	}
	    	  
	    	try
	    	{    		
            	ByteArrayInputStream bais = new ByteArrayInputStream((byte[])param.getParameter());
	            ObjectInputStream ois = new ObjectInputStream (bais);
            	    		
	    		assertEquals( ois.readObject().toString(), coll.toString() );		
	    	}
	       	catch( Throwable exc )
	       	{
				new FrameworkBaseObject().logMessage( "DBParameterTest.testArrayParameter() getParameter() comparison failed - " + exc.toString() );
	            throw exc;    		
	    	}     		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testArrayParameter() failed - " + exc.toString() );
            throw exc;    		
    	}    	
    	
    	new FrameworkBaseObject().logMessage( "DBParameterTest:testArrayParameter() - passed..." );    	
    }
    
    /**
     * Tests com.framework.common.misc.BooleanParameter
     * <p>
     * @throws Throwable
     */
    public void testBooleanParameter()
    throws Throwable 
    {
   		BooleanParameter param 	= new BooleanParameter( true, true );
    	
    	try
    	{    		    		
    		assertEquals( param.isInParameter(), true );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testBooleanParameter() isInParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	
    	try
    	{      		
    		assertEquals( param.getType(), java.sql.Types.BIT );    
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testBooleanParameter() getType() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	  
    	try
    	{    		
    		assertEquals( ((Boolean)param.getParameter()).booleanValue(), true );		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testBooleanParameter() getParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}      		
  	
    	new FrameworkBaseObject().logMessage( "DBParameterTest:testBooleanParameter() - passed..." );    	
    }    
    
    /**
     * Tests com.framework.common.misc.ByteArrayParameter
     * <p>
     * @throws Throwable
     */
    public void testByteArrayParameter()
    throws Throwable 
    {
 
		byte [] b					= {0,1};    		
		ByteArrayParameter param 	= new ByteArrayParameter( b, true );
		    		
    	try
    	{    		    		
    		assertEquals( param.isInParameter(), true );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testByteArrayParameter() isInParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	
    	try
    	{      		
    		assertEquals( param.getType(), java.sql.Types.LONGVARBINARY );    
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testByteArrayParameter() getType() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	  
    	try
    	{    		
    		assertEquals( param.getParameter(), b );		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testByteArrayParameter() getParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}  
    	
    	new FrameworkBaseObject().logMessage( "DBParameterTest:testByteArrayParameter() - passed..." );      	   		
  	
    }    
    
    /**
     * Tests com.framework.common.misc.CharacterParameter
     * <p>
     * @throws Throwable
     */
    public void testCharacterParameter()
    throws Throwable 
    {
 
		Character c					= new Character( 'c' );    		
		CharacterParameter param 	= new CharacterParameter( c, true );
		    		
    	try
    	{    		    		
    		assertEquals( param.isInParameter(), true );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testCharacterParameter() isInParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	
    	try
    	{      		
    		assertEquals( param.getType(), java.sql.Types.CHAR );    
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testCharacterParameter() getType() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	  
    	try
    	{    		
    		assertEquals( ((String)param.getParameter()), c.toString() );		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testCharacterParameter() getParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}     		

    	new FrameworkBaseObject().logMessage( "DBParameterTest:testCharacterParameter() - passed..." );  
    	  	
    }    
    
    /**
     * Tests com.framework.common.misc.DateParameter
     * <p>
     * @throws Throwable
     */
    public void testDateParameter()
    throws Throwable 
    {
 
		Date date				= java.util.Calendar.getInstance().getTime(); 		
		DateParameter param		= new DateParameter( date, true );
		    		
    	try
    	{    		    		
    		assertEquals( param.isInParameter(), true );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testDateParameter() isInParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	
    	try
    	{      		
    		assertEquals( param.getType(), java.sql.Types.DATE );    
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testDateParameter() getType() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	  
    	try
    	{    		
    		assertEquals( param.getParameter(), date );		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testDateParameter() getParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}     		

    	new FrameworkBaseObject().logMessage( "DBParameterTest:testDateParameter() - passed..." );  
    	  	
    }    
    
    /**
     * Tests com.framework.common.misc.DoubleParameter
     * <p>
     * @throws Throwable
     */
    public void testDoubleParameter()
    throws Throwable 
    {
 
		Double dbl				= new Double( 1000 );    		
		DoubleParameter param 	= new DoubleParameter( dbl, true );
		    		
    	try
    	{    		    		
    		assertEquals( param.isInParameter(), true );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testDoubleParameter() isInParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	
    	try
    	{      		
    		assertEquals( param.getType(), java.sql.Types.DOUBLE );    
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testDoubleParameter() getType() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	  
    	try
    	{    		
    		assertEquals( param.getParameter(), dbl );		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testDoubleParameter() getParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}     		

    	new FrameworkBaseObject().logMessage( "DBParameterTest:testDoubleParameter() - passed..." );  
    	  	
    }    
    
    /**
     * Tests com.framework.common.misc.FloatParameter
     * <p>
     * @throws Throwable
     */
    public void testFloatParameter()
    throws Throwable 
    {
 
		Float flt				= new Float( 1.1 );    		
		FloatParameter param	= new FloatParameter( flt, true );
		    		
    	try
    	{    		    		
    		assertEquals( param.isInParameter(), true );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testFloatParameter() isInParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	
    	try
    	{      		
    		assertEquals( param.getType(), java.sql.Types.REAL );    
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testFloatParameter() getType() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	  
    	try
    	{    		
    		assertEquals( param.getParameter(), flt );		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testFloatParameter() getParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}     		

    	new FrameworkBaseObject().logMessage( "DBParameterTest:testFloatParameter() - passed..." );  
    	  	
    }    
    
    /**
     * Tests com.framework.common.misc.IntParameter
     * <p>
     * @throws Throwable
     */
    public void testIntParameter()
    throws Throwable 
    {
 
		Integer integer 	= new Integer( 1 );	 		
		IntParameter param 	= new IntParameter( integer, true );
		    		
    	try
    	{    		    		
    		assertEquals( param.isInParameter(), true );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testIntParameter() isInParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	
    	try
    	{      		
    		assertEquals( param.getType(), java.sql.Types.INTEGER );    
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testIntParameter() getType() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	  
    	try
    	{    		
    		assertEquals( param.getParameter(), integer );		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testIntParameter() getParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}     		

    	new FrameworkBaseObject().logMessage( "DBParameterTest:testIntParameter() - passed..." );  
    	  	
    }    
    
    /**
     * Tests com.framework.common.misc.LongParameter
     * <p>
     * @throws Throwable
     */
    public void testLongParameter()
    throws Throwable 
    {
 
		Long lng  				= new Long( 100000 );	
		LongParameter param 	= new LongParameter( lng, true );
		    		
    	try
    	{    		    		
    		assertEquals( param.isInParameter(), true );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testLongParameter() isInParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	
    	try
    	{      		
    		assertEquals( param.getType(), java.sql.Types.BIGINT );    
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testLongParameter() getType() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	  
    	try
    	{    		
    		assertEquals( param.getParameter(), lng );		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testLongParameter() getParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}     		

    	new FrameworkBaseObject().logMessage( "DBParameterTest:testLongParameter() - passed..." );  
    	  	
    }    
    
    /**
     * Tests com.framework.common.misc.NullParameter
     * <p>
     * @throws Throwable
     */
    public void testNullParameter()
    throws Throwable 
    {		
		NullParameter param 	= new NullParameter( true );
		    		
    	try
    	{    		    		
    		assertEquals( param.isInParameter(), true );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testNullParameter() isInParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	
    	try
    	{      		
    		assertEquals( param.getType(), java.sql.Types.NULL );    
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testNullParameter() getType() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	  
    	try
    	{    		
    		assertNull( param.getParameter() );		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testNullParameter() getParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}     		
    	
    	new FrameworkBaseObject().logMessage( "DBParameterTest:testNullParameter() - passed..." );    	
    }    
    
    /**
     * Tests com.framework.common.misc.ShortParameter
     * <p>
     * @throws Throwable
     */
    public void testShortParameter()
    throws Throwable 
    {
 
		Short shrt				= new Short( "0" );    		
		ShortParameter param 	= new ShortParameter( shrt, true );
		    		
    	try
    	{    		    		
    		assertEquals( param.isInParameter(), true );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testShortParameter() isInParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	
    	try
    	{      		
    		assertEquals( param.getType(), java.sql.Types.SMALLINT );    
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testShortParameter() getType() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	  
    	try
    	{    		
    		assertEquals( param.getParameter(), shrt );		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testShortParameter() getParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}     		

    	new FrameworkBaseObject().logMessage( "DBParameterTest:testShortParameter() - passed..." );  
    	  	
    }    
    
    /**
     * Tests com.framework.common.misc.StringParameter
     * <p>
     * @throws Throwable
     */
    public void testStringParameter()
    throws Throwable 
    {
 
		String str		  		= new String( "test" );		
		StringParameter param 	= new StringParameter( str, true );
		    		
    	try
    	{    		    		
    		assertEquals( param.isInParameter(), true );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testStringParameter() isInParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	
    	try
    	{      		
    		assertEquals( param.getType(), java.sql.Types.LONGVARCHAR );    
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testStringParameter() getType() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	  
    	try
    	{    		
    		assertEquals( param.getParameter(), str );		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testStringParameter() getParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}     		

    	new FrameworkBaseObject().logMessage( "DBParameterTest:testStringParameter() - passed..." );  
    	  	
    }    
    
    /**
     * Tests com.framework.common.misc.TimeParameter
     * <p>
     * @throws Throwable
     */
    public void testTimeParameter()
    throws Throwable 
    {
 
		Calendar cal			= Calendar.getInstance();  		
		TimeParameter param 	= new TimeParameter( cal, true );
		    		
    	try
    	{    		    		
    		assertEquals( param.isInParameter(), true );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testTimeParameter() isInParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	
    	try
    	{      		
    		assertEquals( param.getType(), java.sql.Types.TIME );    
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testTimeParameter() getType() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	  
    	try
    	{    		
    		assertEquals( param.getParameter().toString(), new java.sql.Time( cal.getTime().getTime() ).toString() );		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testTimeParameter() getParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}     		

    	new FrameworkBaseObject().logMessage( "DBParameterTest:testTimeParameter() - passed..." );    	
    }    
    
    /**
     * Tests com.framework.common.misc.TimestampParameter
     * <p>
     * @throws Throwable
     */
    public void testTimestampParameter()
    throws Throwable 
    {
 
        Timestamp timestamp 		= new java.sql.Timestamp( 1000 );		
		TimestampParameter param 	= new TimestampParameter( timestamp, true );
		    		
    	try
    	{    		    		
    		assertEquals( param.isInParameter(), true );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testTimestampParameter() isInParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	
    	try
    	{      		
    		assertEquals( param.getType(), java.sql.Types.TIMESTAMP );    
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testTimestampParameter() getType() comparison failed - " + exc.toString() );
            throw exc;    		
    	}
    	  
    	try
    	{    		
    		assertEquals( param.getParameter(), timestamp );		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "DBParameterTest.testTimestampParameter() getParameter() comparison failed - " + exc.toString() );
            throw exc;    		
    	}     		

    	new FrameworkBaseObject().logMessage( "DBParameterTest:testTimestampParameter() - passed..." );    	
    }                                    
}
    
/*
 * Change Log:
 * $Log: DBParameterTest.java,v $
 * Revision 1.1.1.1  2003/09/15 01:16:04  tylertravis
 * initial check-in
 *
 */    
