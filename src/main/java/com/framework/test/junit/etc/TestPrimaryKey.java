/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.test.junit.etc;

// **********************
// Imports
// **********************
import java.util.ArrayList;
import java.util.Collection;

import com.framework.business.pk.FrameworkPrimaryKey;

/**
 * TestPrimaryKey helper class
 * <p>
 * <p>
 * @author    realMethods, Inc.
 */
public class TestPrimaryKey extends FrameworkPrimaryKey            
{

//************************************************************************
// Public Methods
//************************************************************************

    /** 
     * default constructor - should be normally used for dynamic instantiation
     */
    public TestPrimaryKey() 
    {
    }
    
    
    /** 
     * Constructor with all arguments relating to the primary key        
     * 
     * @param 		id		object unique identifier
     * @exception IllegalArgumentException
     */
    public TestPrimaryKey( Object id ) 
    throws IllegalArgumentException
    {
        super();
                
		this.testID = id.toString();

    }   


    /** 
     * Constructor that accepts a Collection of ordered key fields
     *
     * @param    args		Collection containing key values 
     */
    public TestPrimaryKey( Collection args ) 
    {
        ArrayList array = new ArrayList( args );     
		this.testID = array.get(0).toString();
    }
    
//************************************************************************
// Access Methods
//************************************************************************


	/**
	* Returns the testID
	* 
	* @return		String
	*/
	public String getTestID()
	{
		return( this.testID );
	}
   
 
//************************************************************************
// Protected / Private Methods
//************************************************************************

    /**
     * Retrieves the values as a Collection
     * 
     * @return Collection
     */
    public Collection valuesAsCollection()
    {
	// assign the attributes to the Collection back to the parent
        ArrayList collection = new ArrayList();

		collection.add( testID );

        return( collection );
    }

    
//************************************************************************
// Attributes
//************************************************************************

	/**
	 * object identifier
	 */
	public String testID; 

}


/*
 * Change Log:
 * $Log: TestPrimaryKey.java,v $
 */

