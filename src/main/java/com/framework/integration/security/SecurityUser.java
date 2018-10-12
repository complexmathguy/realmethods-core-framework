/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.security;

//******************************
// Imports
//******************************
import java.io.Serializable;

import java.security.Principal;

import com.framework.common.FrameworkBaseObject;

/**
 * Security User implementation of the ISecurityUser
 * interface.  Used in the JSP/Servlet portion for web security.
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class SecurityUser extends FrameworkBaseObject
    implements ISecurityUser, Serializable
{
//***************************************************   
// Public Methods
//***************************************************
    /**
     * Constructor
     * 
     * @param 		userid 
     * @exception 	IllegalArgumentException 
     */
	public SecurityUser( String userid ) 
	throws IllegalArgumentException
	{
	    // Call base class
	    super();

		userID = userid;
    }

    /** 
     * Retrieves the UserID (is Principal Name).
     * @return String
     */
    public String getUserID()
    {
        return userID;   
    }
    
	/**
	 * Compares this principal to the specified object. Returns true if the 
	 * object passed in matches the principal represented by the 
	 * implementation of this interface.
	 * 
	 * @param obj
	 * @return true if the principal passed in is the same as that encapsulated by this principal, 
	 *          and false otherwise.
	 */
	public boolean equals( Object obj )
	{
	    boolean equals = false;
	    
	    if (obj instanceof Principal)
	    {
	        Principal principalIn = (Principal)obj;
	        equals = userID.equals(principalIn.toString());
	    }
		
		return equals;
	}

	/**
	 * Returns the name of this principal.
	 * @return String
	 */
	public String getName()
	{
		return userID;
	}

    /**
     * Returns the hashcode of the underlyng userid
     * @return int
     */
	public int hashCode()
	{
		// return a hashcode of the userID
		return userID.hashCode();
	}
    
    /**
     * Stringifies the Principal.
     * @return String
     */
	public String toString()
	{
		return userID;
	}
	
//***************************************************   
// Protected/Private Methods
//***************************************************
    /**
     * Default Constructor.
     */
    private SecurityUser()
    {
    }
    

//***************************************************   
// Attributes
//***************************************************
	/**
	 * UserID.
	 */
	private String userID = null;
}

/*
 * Change Log:
 * $Log: SecurityUser.java,v $
 */
