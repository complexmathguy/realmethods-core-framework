/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.security;

import java.util.Map;

import com.framework.common.IFrameworkBaseObject;

/**
 * Encapsulated LDAP related user information
 * <p>
 * @author    realMethods, Inc.
 */
public interface ILDAPUserInfo extends IFrameworkBaseObject
{

    /**
     * returns the first name
     * @return      String
     */
    public String getFirstName();

    /**
     * returns the last name
     * @return      String
     */    
    public String getLastName();

    /**
     * returns the full common name
     * @return      String
     */    
    public String getFullCommonName();
    
    /**
     * returns the userid 
     * @return      String
     */    
    public String getUserID();
    
    /**
     * returns the password encrypted
     * @return      String
     */    
    public String getPasswordEncrypted();

    /**
     * returns the password decrypted
     * @return      String
     */
    public String getPassword();
    
    /**
     * returns the email address
     * @return      String
     */    
    public String getEmailAddress();

    /**
     * returns the phone number
     * @return      String
     */
    public String getPhoneNumber();
    
    /**
     * returns the fax number
     * @return      String
     */    
    public String getFaxNumber();

    /**
     * returns the object class
     * @return      String
     */
    public String getObjectClass();
    
    /**
     * returns the associated value
     * @param		key
     * @return		String
     */
    public String getValue( String key );
    
    /**
     * returns the bound Map of key/value pairings
     * @return      Map
     */    
    public Map getUserInfo();
    
// helper methods

    /**
     * assigns the Map of data internally
     * @param	ldapData	key/value pairings of ldap user info 
     */
    public void applyLDAPData( Map ldapData );

    /**
     * applies a value by key to the internal m_UserData
     *
     * @param   key
     * @param   value
     */
    public void applyValue( String key, String value );
}

/*
 * Change Log:
 * $Log: ILDAPUserInfo.java,v $
 */
