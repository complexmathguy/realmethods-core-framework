/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.notify;

import com.framework.common.FrameworkBaseObject;

/**
 * Wrapper for the different types of value object notification
 * <p> 
 * @author    realMethods, Inc.
 */
public class ValueObjectNotificationType extends FrameworkBaseObject
{
    /**
     * constructor - deter external instantiation
     * @param       type
     */
    protected ValueObjectNotificationType( int type )
    { this.type = type; }
    
    /**
     * query method on update flag
     * @return      boolean
     */
    public boolean isUpdate()
    { return( type == 0 ); }
    
    /**
     * query method on delete flag
     * @return      boolean
     */    
    public boolean isDelete()
    { return( type == 1 ); }

    /**
     * query method on create flag
     * @return      boolean
     */
    public boolean isCreate()
    { return( type == 2 ); }
    
    /**
     * Returns Create, Update, or  Delete
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
    	if ( isCreate() )
    		return( "Create" );
		else if ( isUpdate() )
			return( "Update" );
		else
			return( "Delete");
    }    
    
// attributes

    protected int type = -1;
    
    static final public ValueObjectNotificationType Update = new ValueObjectNotificationType( 0 );
    static final public ValueObjectNotificationType Delete = new ValueObjectNotificationType( 1 );
    static final public ValueObjectNotificationType Create = new ValueObjectNotificationType( 2 );
}

/*
 * Change Log:
 * $Log: ValueObjectNotificationType.java,v $
 */

