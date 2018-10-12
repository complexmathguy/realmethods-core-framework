/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.security;

import java.util.Map;

import com.framework.common.FrameworkBaseObject;

/**
 * Encapsulated LDAP related user information
 * <p>
 * @author    realMethods, Inc.
 */
public class LDAPUserInfo extends FrameworkBaseObject
   implements ILDAPUserInfo
{
    /**
     * default constructor
     */
    public LDAPUserInfo()
    {
    }
    
    /**
     * @param		ldapData	key/value pairings of user information
     */
    public LDAPUserInfo( Map ldapData )
    {
        applyLDAPData( ldapData );
    }
    
// accessor methods

    /**
     * returns the first name
     * @return      String
     */
    public String getFirstName()
    {
        return (String)userData.get( FIRST_NAME_KEY );
    }

    /**
     * returns the last name
     * @return      String
     */    
    public String getLastName()
    {
        return (String)userData.get( LAST_NAME_KEY );
    }
    
    /**
     * returns the full common name
     * @return      String
     */    
    public String getFullCommonName()
    {
        return (String)userData.get( FULL_NAME_KEY );
    }
    
    
    /**
     * returns the userid 
     * @return      String
     */    
    public String getUserID()
    {
        return (String)userData.get( USER_ID_KEY );
    }
    
    /**
     * returns the password encrypted
     * @return      String
     */    
    public String getPasswordEncrypted()
    {
        return (String)userData.get( PASSWORD_ENCRYPTED_KEY );
    }

    /**
     * returns the password decrypted
     * @return      String
     */
    public String getPassword()
    {
        return (String)userData.get( PASSWORD_DECRYPTED_KEY );
    }
    
    /**
     * returns the email address
     * @return      String
     */    
    public String getEmailAddress()
    {
        return (String)userData.get( EMAIL_KEY );
    }

    /**
     * returns the phone number
     * @return      String
     */
    public String getPhoneNumber()
    {
        return (String)userData.get( PHONE_KEY );
    }
    
    /**
     * returns the fax number
     * @return      String
     */    
    public String getFaxNumber()
    {
        return (String)userData.get( FAX_KEY );
    }

    /**
     * returns the object class
     * @return      String
     */
    public String getObjectClass()
    {
        return (String)userData.get( OBJECT_CLASS_KEY );
    }
    
    /**
     * returns the associated value
     * @return      String
     */
    public String getValue( String sKey )
    {
        return( (String)userData.get( sKey ) );
    }
    
    /**
     * returns the bound Map of key/value pairings
     * @return      Map
     */    
    public Map getUserInfo()
    {
        return( userData );
    }

// helper methods

    /**
     * assigns the Map of data internally
     * @param	ldapData 
     */
    public void applyLDAPData( Map ldapData )
    {
        userData = ldapData;
    }

    /**
     * applies a value by key to the internal m_UserData
     * @param   key
     * @param   value
     */
    public void applyValue( String key, String value )
    {
        userData.put( key, value );
    }
    
// attributes

	/**
	 * local cache for user info data
	 */
    protected Map  userData                          = null;
    
    /**
     * standard keys to provided ldap user info data
     */
    static final protected String USER_ID_KEY               = "uid";
    static final protected String PASSWORD_ENCRYPTED_KEY    = "userpassword";    
    static final protected String PASSWORD_DECRYPTED_KEY    = "passworddecrypted";    
    static final protected String FAX_KEY                   = "facsimiletelephonenumber";    
    static final protected String OBJECT_CLASS_KEY          = "objectclass";
    static final protected String FIRST_NAME_KEY            = "sn";    
    static final protected String LAST_NAME_KEY             = "givenname";
    static final protected String FULL_NAME_KEY             = "cn";
    static final protected String EMAIL_KEY                 = "mail";
    static final protected String PHONE_KEY                 = "telephonenumber";
}

/*
 * Change Log:
 * $Log: LDAPUserInfo.java,v $
 */
