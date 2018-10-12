/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.test.junit.etc;

// **********************
// Imports
// **********************
import com.framework.business.pk.FrameworkPrimaryKey;

import com.framework.business.vo.FrameworkValueObject;

/**
 * TestValueObject helper class
 * <p>
 * @author    realMethods, Inc.
 */
public class TestValueObject extends FrameworkValueObject
{
//************************************************************************
// Constructors
//************************************************************************

    /** 
     * Default Constructor 
     */
    public TestValueObject() 
    {    
		applyVersioning( false );         
    }   

    /** 
     * Constructor with a TestPrimaryKey
     * <p>
     * @param    	key     
     * @exception 	IllegalArgumentException
     */
    public TestValueObject( TestPrimaryKey key ) 
    throws IllegalArgumentException
    {
        super( key );
               
		applyVersioning( false );        
    }   

//************************************************************************
// Accessor Methods
//************************************************************************

    /** 
     * Returns the TestPrimaryKey
     * @return TestPrimaryKey   
     */
    public TestPrimaryKey getTestPrimaryKey() 
    {    
        return( (TestPrimaryKey)getFrameworkPrimaryKey() );
    } 

// AIB Generated Section - Do Not Modify Within
    /**
    * Returns the testID
    * @return String
    */
	public String getTestID()
    {
        return this.testID;
    }

    /**
    * Assigns the testID
    * @param ID		an arbitrary identifier
    */
	public void setTestID( String ID )
    {
        testID = ID;
    }

    /**
    * Returns the arg1
    * @return String
    */
	public String getArg1()
    {
        return this.arg1;
    }

    /**
    * Assigns the arg1
    * @param arg1	an arbitrary argument
    */
	public void setArg1( String arg1 )
    {
        this.arg1 = arg1;
    }

    /**
    * Returns the arg2
    * @return String
    */
	public String getArg2()
    {
        return this.arg2;
    }

    /**
    * Assigns the arg2
    * @param 	arg2	an arbitrary argumet
    */
	public void setArge2( String arg2 )
    {
        this.arg2 = arg2;
    }

 
//************************************************************************
// FrameworkValueObject Overloads
//************************************************************************

	/**
	 * Returns the associated FrameworkPrimaryKey
	 *
	 * @return          FrameworkPrimaryKey
	 */
	public FrameworkPrimaryKey getFrameworkPrimaryKey()
	{
		return new TestPrimaryKey( testID );
	}

	/**
	 * Assigns the provided FrameworkPrimaryKey
	 *
	 * @param	key		
	 */
	public void setFrameworkPrimaryKey( FrameworkPrimaryKey key )
	{
		if ( key != null )
		{
			setTestID( key.values()[0].toString() );
		}
		else
		{
			setTestID( null );
		}
	}    
    
    /**
     * Performs a deep copy.
     * @param objectIn 		source to copy from
     * @exception IllegalArgumentException Thrown if the passed in objectIn is null. It is also
     * thrown if the passed in valueObject is not of the correct type.
     */
    public void copy( TestValueObject objectIn ) 
    throws IllegalArgumentException
    {
        if ( objectIn == null )
        {
            throw new IllegalArgumentException("Inside TestValueObject:copy(..) - objectIn cannot be null.");           
        }

        // Call base class copy
        super.copy(objectIn);
        
        // Set member attributes
        this.testID = objectIn.getTestID();
        this.arg1 = objectIn.getArg1();
        this.arg2 = objectIn.getArg2();

    }

    /**
     * Returns a string representation of the object.
     * @return String
     */
    public String toString()
    {
        StringBuffer returnString = new StringBuffer();

        returnString.append( super.toString() + " " );     
      
        returnString.append( "arg1 = " + this.arg1 );
        returnString.append( "," );
        returnString.append( "arg2 = " + this.arg2 );

        return returnString.toString();
    }

//************************************************************************
// Protected / Private Methods
//************************************************************************

    
//************************************************************************
// Attributes
//************************************************************************

// AIB Generated Section - Do Not Modify Within

	/**
	 * object id
	 */
	protected String testID = "999999999";

	/**
	 * arbitrary member variable
	 */
	protected String arg1;

	/**
	 * arbitrary member variable
	 */
	protected String arg2;



// ~AIB Generated

}


/*
 * Change Log:
 * $Log: TestValueObject.java,v $
 * Revision 1.1.1.1  2003/09/15 01:16:10  tylertravis
 * initial check-in
 *
 */

