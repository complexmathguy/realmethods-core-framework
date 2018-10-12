/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.vo.list;

//************************************
// Imports
//************************************
import java.util.Collection;
import java.util.NoSuchElementException;

import com.framework.common.IFrameworkBaseObject;

/**
 * Defines an interface to iterate over a provided Collection, and take on the
 * identity of an IFrameworkValueObject.
 * <p> 
 * @author    realMethods, Inc.
 */
public interface IListProxy extends IFrameworkBaseObject
{
    /**
     * Returns the Data Values.
     * <p>
     * @return Collection of HashMap's that represents records of data.
     *          The keys into the data are the schema values.
     */
    public Collection getDataValues();

    /**
     * Apply a Collection of data values.
     * <p>
     * @param      	dataValues - collection that represents records of data.
     *              The keys into the data are the schema values.
     */    
    public void applyDataValues( Collection dataValues );
    
    /*
     * Moves the pointer to the next record if there is one.
     *
     * @return      Object
     * @exception   NoSuchElementException
     */
    public Object next()
    throws NoSuchElementException;

    /**
     * Moves the pointer to the previous record if there is one.
     * <p>
     * @return      Object
     * @exception   NoSuchElementException
     */
    public Object previous()
    throws NoSuchElementException;
    
    /**
     * Moves the pointer to the previous n records, or as many as possible,
     * and return as a Collection.
     * <p>
     * @param       numberToGet		# of Objects to return
     * @return      Collection		what was actually moved over
     */
    public Collection next( int numberToGet )
    throws NoSuchElementException;
    
    /**
     * Moves the pointer to the previous n records, or as many as possible,
     * and return as a Collection.
     * <p>
     * @param       numberToGet		# of Objects to return
     * @return      Collection		what was actually moved over
     */
    public Collection previous( int numberToGet )
    throws NoSuchElementException;    

    /**
     * Returns whether there are any more records in the iterator.
     * @return boolean
     */
    public boolean hasNext();

    /**
     * Returns whether there is a previous record in the iterator.
     * @return boolean
     */
    public boolean hasPrevious();

    /**
     * Returns whether there are any more then n records left in the iterator.
     * @param	numberToConsider	# to apply
     * @return 	boolean
     */
    public boolean hasNext( int numberToConsider );

    /**
     * Returns whether there are any more than n records before the beginning
     * of the iterator.
     * @param	numberToConsider	# to apply
     * @return 	boolean
     */
    public boolean hasPrevious( int numberToConsider );
    
    /**
     * reset - prepare to re-iterate
     */
    public void reset();
     
}

/*
 * Change Log:
 * $Log: IListProxy.java,v $
 */
