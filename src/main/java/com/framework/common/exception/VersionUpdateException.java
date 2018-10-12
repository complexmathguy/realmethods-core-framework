/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.exception;

/**
 * Exception class for failure of making use of a FrameworkDAO object
 * 
 * <p>
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.dao.FrameworkDAO
 * @see		  com.framework.business.vo.FrameworkValueObject
 */
public class VersionUpdateException extends FrameworkCheckedException
{
    /** 
     * Base constructor.
     */
    public VersionUpdateException()
    {
        super(); 
    }

    /** 
     * Constructor with message.
     * 
     * @param message   text of the exception
     */
    public VersionUpdateException( String message )
    {
        super( message ); 
    }
    
}

/*
 * Change Log:
 * $Log: VersionUpdateException.java,v $
 */




