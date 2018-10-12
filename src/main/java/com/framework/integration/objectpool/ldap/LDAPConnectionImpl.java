/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.objectpool.ldap;

//************************************
// Imports
//************************************
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.management.*;

import javax.naming.Context;

import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.Control;

import com.framework.common.exception.ConnectionActionException;
import com.framework.common.exception.InitializationException;
import com.framework.common.exception.ObjectCreationException;

import com.framework.integration.objectpool.ConnectionImpl;

/**
 * A poolable helper class for making use of an LDAP connection
 * <p>
 * @author    realMethods, Inc.
 */
public class LDAPConnectionImpl extends ConnectionImpl
{
//****************************************************
// Public Methods
//****************************************************
    public LDAPConnectionImpl()
    {
    }

	/** 
	 * Initializes and makes a connection to the connection source.
	 * This base implementation does not make the actual connection.
	 * This method must be overriden in the subclasses.
	 * <p>
	 * @param	name 		The pool that this connection is associated with.
	 * @param  	properties 	Map of properties needed to create the connection.
	 * 						Actual values are dependent on the associated type of connection.
	 * @exception IllegalArgumentException Thrown if the props is null.
	 * @exception InitializationException 
	 */ 
    public void initialize( String name, Map properties )
    throws IllegalArgumentException, InitializationException
    {
        super.initialize( name, properties );        
    }    
    
    /**
     * client notification that a transaction is about to begin
     */
    public void startTransaction()
    throws ConnectionActionException
    {
    }

    /**
     * client notification to commit the current transaction
     */
    public void commit()
    throws ConnectionActionException
    {}

    /**
     * client notification to rollback the current transaction
     */
    public void rollback()
    throws ConnectionActionException
    {}

    /**
     * client notification to disconnect from the resource
     */    
    public void disconnect()
    throws ConnectionActionException
    {
		try
  		{
        	//********************************************
            // close the session only if it's open
            //********************************************
            if ( initialLDAPContext != null )
            {
                initialLDAPContext.close();
            }
        }
        catch ( Exception e )
        {
            logErrorMessage( "LDAPConnectionImpl:disconnect() - InitialDirContext could not be closed" );
        }
        finally
        {
            initialLDAPContext = null;            
            super.disconnect();
        }
    }
    
// ConnectionPoolImpl overloads - from ConnectionPoolMBean

    /**
     * Allows the value of the specified attribute of the Dynamic MBean to be obtained.
     * <p>
     * @param       name		attribute to return
     * @return      Object
     * @exception   AttributeNotFoundException
     * @exception   MBeanException
     * @exception   ReflectionException 
     */
    public Object getAttribute( String name ) 
	throws AttributeNotFoundException, MBeanException, ReflectionException 
	{
	    Object attribute = null;
	    
	    if ( name.equals( USER_KEY ) )
	    {	        
	        attribute = this.getLDAPUserKey();
	    }
	    else if ( name.equals( PASSWORD_KEY ) )
	    {
	        attribute = this.getLDAPPasswordKey();
	    }
	    else if ( name.equals( ROLE_KEY ) )
	    {
	        attribute = this.getLDAPRoleKey();
	    }
	    else if ( name.equals( ROOT_DOMAIN_NAME ) )
	    {
	        attribute = this.getLDAPRootDomainName();
	    }
	    else if ( name.equals( USE_PASSWORD_ENCRYPTION ) )
	    {
	        attribute = this.userPasswordEncryption();
	    }
	    else
	    {
    	    // If attribute_name has not been recognized delegate to the subclass
	        attribute = super.getAttribute( name );
	    }
	    
	    return( attribute );
    }

    /**
     * Sets the value of the specified attribute of the Dynamic MBean.
     * <p>
     * @param       attribute 
     * @exception   AttributeNotFoundException
     * @exception   InvalidAttributeValueException,
	 * @exception   MBeanException
	 * @exception   ReflectionException 
     */
    public void setAttribute( Attribute attribute ) 
	throws AttributeNotFoundException, InvalidAttributeValueException,
	       MBeanException, ReflectionException 
	{

	    // Check attribute is not null to avoid NullPointerException later on
	    if (attribute == null) 
	    {
	        throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"), 
			    			 "Cannot invoke a setter of " + getClassName() + " with null attribute");
	    }
	    
	    String name = attribute.getName();
//	    Object value = attribute.getValue();

	    if (name == null) 
	    {
	        throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), 
						 "Cannot invoke the setter of " + getClassName() + " with null attribute name");
	    }
	    
        // Since none of the parameters are settable here, delegate to the parent
	    {
	        super.setAttribute( attribute );
		}
    }



    /**
     * Returns the description of this MBean
     * @return      String      
     */
    protected String getMBeanDescription()
    {
        return( "LDAP Connection Implementation MBean" );
    }

    /**
     * Returns all the attributes for this class as an array of MBeanAttributeInfos.
     * <p>
     * @return MBeanAttributeInfo[]
     */
    protected MBeanAttributeInfo[] getMBeanAttributes()
    {
        MBeanAttributeInfo[] mbAttributeInfo = new MBeanAttributeInfo[ 5 ];    

        
        mbAttributeInfo[0] = new MBeanAttributeInfo( USER_KEY, "java.lang.String", "UserKey: The key used to access the LDAP user attribute.", true, false, false );
        mbAttributeInfo[1] = new MBeanAttributeInfo( PASSWORD_KEY, "java.lang.String", "PasswordKey: The key used to access the LDAP password attribute.", true, false, false );
        mbAttributeInfo[2] = new MBeanAttributeInfo( ROLE_KEY, "java.lang.String", "RoleKey: The key used to access the LDAP attribute designated as the user role.", true, false, false );
        mbAttributeInfo[3] = new MBeanAttributeInfo( ROOT_DOMAIN_NAME, "java.lang.String", "RootDomain: The initial domain in the LDAP hierarchy of interest.", true, false, false );
        mbAttributeInfo[4] = new MBeanAttributeInfo( USE_PASSWORD_ENCRYPTION, "java.lang.Boolean", "UsePasswordEncryption: T/F indicator for password encryption in use within LDAP.", true, false, false );
        
        return( combineAttributeInfos( super.getMBeanAttributes(), mbAttributeInfo ) );
    }

// access methods

    /**
     * Returns the intent of the class...the InitialDirContext
     * as relates to this implementation of an LDAP connection.
     * <p>
     * @return      InitialDirContext
     */
    public InitialLdapContext getInitialDirContext()
    {
		if ( initialLDAPContext == null )
		{
			try
			{
	            logInfoMessage( "LDAPConnectionImpl:getInitialDirContext() - about to create InitialDirContext for LDAP pool " + getPoolName() + "using props: " + getProperties() );            
	            initialLDAPContext = createInitialDirContext( getProperties() );           
	            logInfoMessage( "LDAPConnectionImpl:getInitialDirContext() - Create InitialDirContext succedded for LDAP pool " + getPoolName() );
			}
			catch( Exception exc )
			{
	            logInfoMessage( "LDAPConnectionImpl:getInitialDirContext() - Create InitialDirContext failed for LDAP pool " +  getPoolName() );				
			}
			
		}	    	
        return( initialLDAPContext );
    }

    /**
     * Returns the intent of the class...the InitialDirContext
     * as it relates to this implementation of an LDAP connection.
     * <p>
     * @param		userid			may be req'd to get the InitialDirContext
     * @param		password		may be req'd to get the InitialDirContext
     * @return   	InitialDirContext - may be null in case of creation error
     */
    public InitialLdapContext getInitialDirContext( String userid, String password )
    {
        // just send what is needed
        Properties props = new Properties();

        props.put(Context.INITIAL_CONTEXT_FACTORY, 
                    getProperties().get( Context.INITIAL_CONTEXT_FACTORY ) );

        props.put(Context.SECURITY_AUTHENTICATION,
                    getProperties().get(Context.SECURITY_AUTHENTICATION) );

        props.put(Context.SECURITY_PRINCIPAL, userid );

        props.put(Context.SECURITY_CREDENTIALS, password );

        props.put(Context.PROVIDER_URL,
                    getProperties().get(Context.PROVIDER_URL) );

        props.put(TIMEOUT_CLASS_KEY,
                getProperties().get(TIMEOUT_CLASS_KEY) );

        props.put(TIMEOUT_CLASS_VALUE,
                getProperties().get(TIMEOUT_CLASS_VALUE) );
        
        try
        {
            initialLDAPContext = new InitialLdapContext( props, controls );
        }
        catch ( Exception e )
        {
            logInfoMessage( "LDAPConnectionImpl:getInitialDirContext( String sUserID, String sPassword ) - " + e );
        }
        
        return( initialLDAPContext );
    }

    /**
     * Returns the key used to access a user value.
     * @return  String
     */
    public String getLDAPUserKey()
    {
       return( (String)getProperties().get( USER_KEY )  );        
    }
    
    /**
     * Returns the key used to access a password value.
     * @return  String
     */    
    public String getLDAPPasswordKey()
    {
        return( (String)getProperties().get( PASSWORD_KEY )  );        
    }

    /**
     * Returns the key used to access a role value.
     * @return  Boolean
     */    
    public Boolean userPasswordEncryption()
    {
        return( new Boolean( (String)getProperties().get( USE_PASSWORD_ENCRYPTION ) ) );        
    }
    
    /**
     * Returns the key used to access a role value.
     * @return  String
     */    
    public String getLDAPRoleKey()
    {
        return( (String)getProperties().get( ROLE_KEY )  );        
    }

    /**
     * Returns the root domain of LDAP for this implementation.
     * @return  String
     */    
    public String getLDAPRootDomainName()
    {
        return( (String)getProperties().get( ROOT_DOMAIN_NAME )  );        
    }

// helper methods

	/**
	 * Helper method used to assist in creating the internal LDAP InitialDirContext.  Will
	 * only create and initialize the contained InitialDirContext once.
	 * <p>
	 * @param		props				properties to provide to the InitialDirContext during its creation
	 * @return		InitialDirContext
	 * @exception	ObjectCreationException
	 */
	protected InitialLdapContext createInitialDirContext( Map props )    
	throws ObjectCreationException
	{
		if ( initialLDAPContext == null )
		{
			try
			{
				Hashtable hash = new Hashtable();
				hash.putAll( props );
				
				initialLDAPContext = new InitialLdapContext( hash, controls );
			}
			catch( Throwable exc )
			{
				throw new ObjectCreationException( "LDAPConnectionImpl:createInitialDirContext( Map) - " + exc, exc );
			}
		}
		
		return( initialLDAPContext );
	}
	
// attributes

    /**
     * What is being wrapped...the InitialDirContext
     */
    protected InitialLdapContext initialLDAPContext = null;
    
    /**
     * These fields map to the user and password keys necessary to access
     * the associated values within this LDAP connection implementation
     */
    static protected final String USER_KEY          = "ldap_user_key";
    static protected final String PASSWORD_KEY      = "ldap_password_key";
    static protected final String ROLE_KEY          = "ldap_role_key";    
    static protected final String ROOT_DOMAIN_NAME  = "ldap_root_dn";
    static protected final String USE_PASSWORD_ENCRYPTION = "use_password_encryption";
    static protected final String TIMEOUT_CLASS_KEY = "com.sun.jndi.ldap.connect.timeout";
    static protected final String TIMEOUT_CLASS_VALUE = "ldap_timeout_value";
    protected Control[] controls = null;
}

/*
 * Change Log:
 * $Log: LDAPConnectionImpl.java,v $
 */
