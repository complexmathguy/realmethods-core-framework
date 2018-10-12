/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.objectpool.mq;

//************************************
// Imports
//************************************

import com.framework.common.FrameworkBaseObject;

/**
 * Encapsulates capabilites of an MQ Receive termination indicator
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkMQReceiveTerminator extends FrameworkBaseObject
{

// constructors

    /**
     * deter instantiation
     */
    protected FrameworkMQReceiveTerminator()
    {}
    
    /**
     * Use this constructor when receiving a single message
     *
     * @param 	terminationIndicator
     */     
    public FrameworkMQReceiveTerminator( String terminationIndicator )
    throws IllegalArgumentException
    {
        if ( terminationIndicator == null )
            throw new IllegalArgumentException( "MQReceiveTerminator() - receive termination provided is null." );
    
        if ( terminationIndicator.length() == 0 )                
	        throw new IllegalArgumentException( "MQReceiveTerminator() - receive termination provided is empty." );            
        
        this.terminationIndicator = terminationIndicator;
    }

    /**
     * returns the encapsulated termination indicator String
     *
     * @return      String
     */
    public String toString()
    {
        return( terminationIndicator );
    }
    
// attributes

    /**
     * The purpose of the encapsulation
     */
    protected String terminationIndicator = null;
}

/*
 * Change Log:
 * $Log: FrameworkMQReceiveTerminator.java,v $
 */
