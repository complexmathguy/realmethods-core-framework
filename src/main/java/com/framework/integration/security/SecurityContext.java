/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.security;

//*********************************
// Imports
//*********************************
import java.io.Serializable;

import com.framework.common.FrameworkBaseObject;

/**
 * Abstracts out the context associated with a security action.
 * <p>
 * @author    realMethods, Inc.
 */
public class SecurityContext extends FrameworkBaseObject 
    implements ISecurityContext, Serializable
{
//***************************************************   
// Public Methods
//***************************************************
	/**
	 * Constructor
	 * @param contextString 		String representation of the context.
	 */	 
	public SecurityContext( String contextString )
	{
	    super();
	    
		// Set the member attribute
		this.contextString = contextString;
	}

    /**
     * Retrieves the Context String
     * @return String
     */
    public String getContextString()
    {
        return contextString;
    }
    
    /**
     * Returns a string describing this Permission.
     * @return String
     */
	public String toString()
	{
		return contextString;
	}
//***************************************************   
// Protected/Private Methods
//***************************************************

//***************************************************   
// Attributes
//***************************************************
    /**
     * Context String
     */
    private String contextString = null;
}

/*
 * Change Log:
 * $Log: SecurityContext.java,v $
 */
