/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.security; 

// **********************
// Imports
// **********************

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingEnumeration;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.FrameworkSecurityManagerException;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.integration.objectpool.ConnectionPoolManagerFactory;

import com.framework.integration.objectpool.ldap.LDAPConnectionImpl;

/**
 * Base Security implementation for the using LDAP as a means of authentication and authorization.
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkLDAPSecurityAdapter extends FrameworkBaseObject
    implements IAuthenticateUser, ISecurityRoleLoader, ISecurityUserLoader
{
//************************************************************************
// Public Methods
//************************************************************************
    /**
     * Default Constructor
     * <p>
     * Uses the default ldap connnection pool name specified in the framework.xml
     * file and defined in the connectionpool.properties.
     */
    public FrameworkLDAPSecurityAdapter()
    {
        super();
    }

    /**
     * Constructor
     * 
     * @param		ldapConnPoolName
     */
    public FrameworkLDAPSecurityAdapter( String ldapConnPoolName )
    {    	
        super();
        
        this.ldapConnPoolName = ldapConnPoolName;
    }
    /** 
     * Authenticates the user.
     * <p>
     * @param   	userID		user identifier
     * @param   	password 	user's password
     * @param   	appID       ignored, but may be used for additonal security context info
     * @return 	 				true if the user is authenticated and false otherwise.
     * @exception 	FrameworkSecurityManagerException 
     */
    public boolean authenticateUser( String userID, String password, String appID)
    throws FrameworkSecurityManagerException
    {
        boolean bAuthenticate           = false;
        ILDAPUserInfo userInformation   = retrieveUserInformation( userID, password );
        
        logDebugMessage( "FrameworkLDAPSecurityAdapter.authenticateUser(...) - LDAPUserInfo is " + userInformation );
        
        if ( userInformation != null && userInformation.getUserID() != null )
            bAuthenticate = true;        
        
        return( bAuthenticate );
    }

    /** 
     * Returns all attribute data from LDAP pertaining to the provided user information.
     * <p>
     * @param   	userID			user identifier
     * @param   	password		user's password
     * @return  	ILDAPUserInfo	ldap user information
     * @exception 	FrameworkSecurityManagerException Thrown when any error occurs.
     */
    public ILDAPUserInfo retrieveUserInformation( String userID, String password )
    throws FrameworkSecurityManagerException    
    {
        // clear it out to be applied for this cycle of related calls
        ldapUserInfo = null;
        
        SearchResult searchResult = null;
        
        // for use throughout the process
        this.userID   = userID;
        this.password = password;
        
        try
        {
            ////////////////////////////////////////////////////////////////////////
            // Step #1
            // Use the named LDAP connection found within the framework.xml
            // DEFAULT_LDAP_CONNECTION_NAME property
            ////////////////////////////////////////////////////////////////////////
            ldapConnectionImpl = getLDAPConnection();
            
            if ( ldapConnectionImpl == null )
                throw new FrameworkSecurityManagerException( "FrameworkLDAPSecurityAdapater:retrieveUserInformation() failed due to no default LDAP Connection defined.  Check connectionpool.properties and framework.xml." );            
                
            ////////////////////////////////////////////////////////////////////////
            // Step #2: 
            // Obtain the InitialDirContext from the LDAPConnectionImpl
            ////////////////////////////////////////////////////////////////////////
            
            InitialDirContext directoryContext      = ldapConnectionImpl.getInitialDirContext( userID, password );
            
            if ( directoryContext == null )
                throw new FrameworkSecurityManagerException( "FrameworkLDAPSecurityAdapater:retrieveUserInformation() failed due create the DirContext to LDAP.  Check connectionpool.properties for this LDAP connection." );            
                
            ////////////////////////////////////////////////////////////////////////
            // Step #3:            
            // Call the abstract methods to access the search and filter criteria
            ////////////////////////////////////////////////////////////////////////                    
            String sSearchFilter            = getSearchFilter();
            SearchControls searchControls   = getSearchControls();
            
            searchControls.setReturningAttributes( null );  // will return all the attributes

            ////////////////////////////////////////////////////////////////////////
            // Step #4:            
            // Run the search using the previous acquired parameters
            ////////////////////////////////////////////////////////////////////////                    
            NamingEnumeration searchEnum =  directoryContext.search( ldapConnectionImpl.getLDAPRootDomainName(), 
                                                                        sSearchFilter, searchControls );

            logDebugMessage( "NamingEnumeration is " + searchEnum + " for root domain " + ldapConnectionImpl.getLDAPRootDomainName() + " " + sSearchFilter + " " + searchControls );
            
            ////////////////////////////////////////////////////////////////////////
            // Step #5:            
            // If there is even one, just use that one
            ////////////////////////////////////////////////////////////////////////                    
            boolean bAuthenticated = false;
            
            if ( searchEnum.hasMore() ) 
            {
                searchResult = (SearchResult)searchEnum.nextElement();
                bAuthenticated = true;
                logDebugMessage( "Search Result is " + searchResult + " " + searchResult.getAttributes() );
                                
            }
            else 
            {
                logDebugMessage( "NamingEnumeration has no elements" );                
                bAuthenticated = false;
            }

            ////////////////////////////////////////////////////////////////////////
            // Step #8:
            // If the user is authenticated, get all the attributes from the user
            // information returned, and repackaged into a Map
            ////////////////////////////////////////////////////////////////////////                                                                
            if ( bAuthenticated )   // user found
            {
                ldapUserInfo = acquireLDAPInfoFromAttributes( searchResult.getAttributes() );

                // supply this password for safe keeping and ease of use...
                ldapUserInfo.applyValue( "passworddecrypted", password );
                                
            }
            
            // release the connection back to the connection pool manager
            ConnectionPoolManagerFactory.getObject().releaseConnection( ldapConnectionImpl );
            
        }
        catch( Exception exc )
        {
            throw new FrameworkSecurityManagerException( "FrameworkLDAPSecurityAdapater:retrieveUserInformation() failed : " + exc, exc );
        }        
                
        return( ldapUserInfo );
    }
    
    /** 
     * Returns attribute data from LDAP pertaining to the provided user information,
     * conditioned on the value of the provided SearchControls parameter.
     * <p>
     * @param   	userID				user identifier
     * @param   	password			user's password
     * @param   	searchControls 		filter to apply, null implies get all atrbibutes
     * @return  	ILDAPUserInfo    	ldap related information
     * @exception FrameworkSecurityManagerException Thrown when any error occurs.
     */
    public ILDAPUserInfo retrieveUserInformation( String userID, String password, SearchControls searchControls  )
    throws FrameworkSecurityManagerException    
    {
        // clear it out to be applied for this cycle of related calls
        ldapUserInfo = null;
        
        SearchResult searchResult = null;
        
        // for use throughout the process
        this.userID   = userID;
        this.password = password;
        
        try
        {
            ////////////////////////////////////////////////////////////////////////
            // Step #1
            // Use the named LDAP connection found within the framework.xml
            // DEFAULT_LDAP_CONNECTION_NAME property
            ////////////////////////////////////////////////////////////////////////
            ldapConnectionImpl = getLDAPConnection();
            
            if ( ldapConnectionImpl == null )
                throw new FrameworkSecurityManagerException( "FrameworkLDAPSecurityAdapater:retrieveUserInformation() failed due to no default LDAP Connection defined.  Check connectionpool.properties and framework.xml." );            
                
            ////////////////////////////////////////////////////////////////////////
            // Step #2: 
            // Obtain the InitialDirContext from the LDAPConnectionImpl
            ////////////////////////////////////////////////////////////////////////
            
            InitialDirContext directoryContext      = ldapConnectionImpl.getInitialDirContext();
            
            if ( directoryContext == null )
                throw new FrameworkSecurityManagerException( "FrameworkLDAPSecurityAdapater:retrieveUserInformation() failed due create the DirContext to LDAP.  Check connectionpool.properties for this LDAP connection." );            
                
            ////////////////////////////////////////////////////////////////////////
            // Step #3:            
            // Call the abstract methods to access the search and filter criteria
            ////////////////////////////////////////////////////////////////////////                    
            String sSearchFilter            = getSearchFilter();
            
            if ( searchControls == null )
            {
                searchControls = getSearchControls();
                searchControls.setReturningAttributes( null );  // will return all the attributes
            }

            ////////////////////////////////////////////////////////////////////////
            // Step #4:            
            // Run the search using the previous acquired parameters
            ////////////////////////////////////////////////////////////////////////                    
            NamingEnumeration searchEnum =  directoryContext.search( ldapConnectionImpl.getLDAPRootDomainName(), 
                                                                        sSearchFilter, searchControls );

            logDebugMessage( "NamingEnumeration is " + searchEnum + " for root domain " + ldapConnectionImpl.getLDAPRootDomainName() + " " + sSearchFilter + " " + searchControls );
            
            ////////////////////////////////////////////////////////////////////////
            // Step #5:            
            // If there is even one, just use that one
            ////////////////////////////////////////////////////////////////////////                    
            boolean bAuthenticated = false;
            
            if ( searchEnum.hasMore() ) 
            {
                searchResult = (SearchResult)searchEnum.nextElement();
                
                logDebugMessage( "Search Result is " + searchResult + " " + searchResult.getAttributes() );
                
                ////////////////////////////////////////////////////////////////////////
                // Step #6:            
                // Obtain the password related attribute from the search result
                ////////////////////////////////////////////////////////////////////////                                    
                Attribute attribute = searchResult.getAttributes().get( ldapConnectionImpl.getLDAPPasswordKey() );
                
                if ( attribute == null )
                {
                    throw new FrameworkSecurityManagerException( "FrameworkLDAPSecurityAdapater:retrieveUserInformation() failed - password attribute " + ldapConnectionImpl.getLDAPPasswordKey() + " not found." );                
                }                
                
                String sAttributePassword   = null;
                String encryptedPassword    = applyPasswordEncrpytion( password );
                                    
                ////////////////////////////////////////////////////////////////////////
                // Step #7:            
                // Since an attribute can have many values associated with it, search
                // through all until the match is found
                ////////////////////////////////////////////////////////////////////////                                                    
                for ( int i = 0; i < attribute.size(); i++ )
                {
                    sAttributePassword = new String( (byte[])attribute.get(i) );
                    
                    if ( sAttributePassword.equals( encryptedPassword ) )    // found it!!
                    {
                        logInfoMessage( "User " + userID + " has been authenticated via LDAP" );                        
                        bAuthenticated = true;                        
                        break;
                    }
                }
            }
            else 
            {
                logDebugMessage( "NamingEnumeration has no elements" );                
                bAuthenticated = false;
            }

            ////////////////////////////////////////////////////////////////////////
            // Step #8:
            // If the user is authenticated, get all the attributes from the user
            // information returned, and repackaged into a Map
            ////////////////////////////////////////////////////////////////////////                                                                
            if ( bAuthenticated )   // user found
            {
                ldapUserInfo = acquireLDAPInfoFromAttributes( searchResult.getAttributes() );

                // supply this password for safe keeping and ease of use...
                ldapUserInfo.applyValue( "passworddecrypted", password );
                                
            }
            
            // release the connection back to the connection pool manager
            ConnectionPoolManagerFactory.getObject().releaseConnection( ldapConnectionImpl );
            
            ldapConnectionImpl = null;
        }
        catch( Exception exc )
        {
            throw new FrameworkSecurityManagerException( "FrameworkLDAPSecurityAdapater:retrieveUserInformation() failed : " + exc, exc );
        }        
                
        return( ldapUserInfo );
    }
    

    /** 
     * Loads the SecurityRoles for the associated SecurityUser and application id.
     * <p>
     * @param 		userID 		user identifier
     * @param		appID		ignored, but could provide additional security context info
     * @return 					Collection of ISecurityRoles.  If there are no security roles then
     * 							the map will be empty.  
     * @exception IllegalArgumentException Thrown if user is null.
     * @exception FrameworkSecurityManagerException Thrown when any other error occurs.
     */
    public Collection loadSecurityRolesForUser(ISecurityUser userID, String appID ) 
        throws IllegalArgumentException, FrameworkSecurityManagerException
    {
        ArrayList returnList        = new ArrayList();
        
        try
        {
            ////////////////////////////////////////////////////////////////////////
            // Step #1
            // Use the named LDAP connection found within the framework.xml
            // DEFAULT_LDAP_CONNECTION_NAME property
            ////////////////////////////////////////////////////////////////////////                
            ldapConnectionImpl = getLDAPConnection();
                
            ////////////////////////////////////////////////////////////////////////
            // Step #2: 
            // Obtain the InitialDirContext from the LDAPConnectionImpl
            ////////////////////////////////////////////////////////////////////////
                
            InitialDirContext directoryContext      = ldapConnectionImpl.getInitialDirContext();
                
            ////////////////////////////////////////////////////////////////////////
            // Step #3:            
            // Call the abstract methods to access the search and filter criteria
            ////////////////////////////////////////////////////////////////////////                    
            SearchControls searchControls   = getSearchControls();
            String sSearchFilter            = getSearchFilter();
                
            ////////////////////////////////////////////////////////////////////////
            // Step #4:            
            // Run the search using the previous acquired parameters
            ////////////////////////////////////////////////////////////////////////                    
            NamingEnumeration searchEnum =  directoryContext.search( ldapConnectionImpl.getLDAPRootDomainName(), 
                                                               		 sSearchFilter, searchControls );
                
            ////////////////////////////////////////////////////////////////////////
            // Step #5:            
            // If there is even one, just use that one
            ////////////////////////////////////////////////////////////////////////                    

            if ( searchEnum.hasMore() ) 
            {
                SearchResult searchResult = (SearchResult)searchEnum.nextElement();
                    
                ////////////////////////////////////////////////////////////////////////
                // Step #6:            
                // Obtain the role related attribute from the search result
                ////////////////////////////////////////////////////////////////////////                                    
                Attribute attribute = searchResult.getAttributes().get( ldapConnectionImpl.getLDAPRoleKey() );
                    
                ////////////////////////////////////////////////////////////////////////
                // Step #7:            
                // Since an attribute can have many values associated with it, use
                // all the one's discovered...
                ////////////////////////////////////////////////////////////////////////                                                    
                NamingEnumeration attribEnum    = attribute.getAll();
                String roleName                = null;
                    
                while( attribEnum.hasMore() )
                {
                    roleName = attribEnum.nextElement().toString();//new String( (byte[])attribEnum.nextElement() );
                    returnList.add((ISecurityRole)new SecurityRole( roleName ));
                }
            }
        }
        catch( Exception exc )
        {
            throw new FrameworkSecurityManagerException( "FrameworkLDAPSecurityAdapater:loadSecurityRolesForUser() failed : " + exc, exc );            
        }
        
        return returnList;
    }

    /** 
     * Creates a SecurityUser from the userID and appID.
     * @param 	userID
     * @param	appID		can be null
     * @return ISecurityUser 
     * @exception IllegalArgumentException Thrown if userID is null.
     * @exception FrameworkSecurityManagerException Thrown if anyother errors occur.
     */
    public ISecurityUser retrieveSecurityUser(	String userID,
                                                String appID )
        throws IllegalArgumentException, FrameworkSecurityManagerException
    {
        ISecurityUser returnUser = null;        
        
        returnUser = new SecurityUser( userID );
        
        return returnUser;
    }
        
	/**
	 * Returnns the LDAPConnectionImpl to use.
	 * <p>
	 * @return		the ldap connection impl from the ConnectionPoolManager
	 * @throws 		FrameworkSecurityManagerException	thrown if there is failure to acquire the connection.
	 */
    protected LDAPConnectionImpl getLDAPConnection()
    throws FrameworkSecurityManagerException
    {
        if ( ldapConnectionImpl == null )
        {
            try
            {
            	// if the ldap connection pool name wasn't provided during construction
            	// assign the default ldap connection pool name.
				if ( ldapConnPoolName == null || ldapConnPoolName.length() == 0 )
					ldapConnPoolName = FrameworkNameSpace.DEFAULT_LDAP_CONNECTION_NAME;
					
                ldapConnectionImpl = (LDAPConnectionImpl)ConnectionPoolManagerFactory.getObject().getConnection( ldapConnPoolName );
            }
            catch( Throwable exc )
            {
                throw new FrameworkSecurityManagerException( "FrameworkLDAPSecurityAdapter:getLDAPConnection() failed to get the default LDAP Connection: " + exc, exc );                
            }
        }
        
        return( ldapConnectionImpl );
    }
    
// must overloads
    
    /**
     * returns the most recently acquire user information from LDAP
     * 
     * @return      ILDAPUserInfo
     */
    public ILDAPUserInfo getLDAPUserInfo()
    {
        return( ldapUserInfo );
    }

// helper methods

    /**
     * Helper method for returning standard attributes key/value pairs as an implementation
     * of ILDAPUserInfo.  Overload this method when more/different attributes 
     * are provided and required.
     * <p>
     * @param       attributes
     * @return      ILDAPUserInfo
     */
    protected ILDAPUserInfo acquireLDAPInfoFromAttributes( Attributes attributes )
    {
//        Attribute userAttribute             = null;
        NamingEnumeration attributeNames    = attributes.getIDs();
        String attributeName                = null;
        Map ldapInfoHash              = new HashMap( attributes.size() );
                
        while ( attributeNames.hasMoreElements() )
        {
            attributeName = (String)attributeNames.nextElement();
                    
            try
            {
                // add the attribute name and attribute value to the Map
                // of the user information
                ldapInfoHash.put( attributeName, attributes.get( attributeName ).get(0).toString() );
            }
            catch( Exception exc )
            {
                logErrorMessage( "FrameworkLDAPSecurityAdapter:acquireLDAPInfoFromAttributes(...) - " + exc );
            }
        }

        return( new LDAPUserInfo( ldapInfoHash ) );
    }
    
    /**
     * Provides the search criteria for authentication.
     * <p>
     * @return      SearchControls
     */
    protected SearchControls getSearchControls()
    {
        SearchControls searchControls = new SearchControls();
        
        try
        {
            String[] returnAttributes    = {getLDAPConnection().getLDAPUserKey(), 
                                            getLDAPConnection().getLDAPPasswordKey(),
                                            getLDAPConnection().getLDAPRoleKey()};
            
            searchControls.setCountLimit(1);        
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);        
            searchControls.setReturningAttributes(returnAttributes);
        }
        catch( Exception exc )
        {}
        
        return( searchControls );
    }

    /**
     * Provides the filtering criteria for a more focused search through
     * LDAP for the purpose of authenticiation.  Overload this method in a sub-class
     * to provide a different search filter.
     * <p>
     * @return      the search filter
     */
    protected String getSearchFilter()
    {
        String filter = null;
        
        try
        {
            filter  = "(" + getLDAPConnection().getLDAPUserKey() + "=" + userID + ")";
        }
        catch( Exception exc )
        {}
        
        return( filter  );
    }
    

//************************************************************************
// Protected / Private Methods
//************************************************************************

    /* Converts an arbitrary length string to the a fixed length, one-way hashed
     * base64 encoded string (the length depends upon the Digest alogirithm 
     * chosen).  The hashing is done through the use of a MessageDigest object
     * from the java.security package, while the base64 encoding is done by
     * a base64Encode() on this class.
     *
     * This format is an often used format for persisting passwords in a system.
     * e.g. OpenLDAP persists passwords using a base64 SHA1 string.
     * <p>
     * Code for this method was taken from a page on javaworld.com.  This page can be
     * found at:<br> 
     * <a href="http://www.javaworld.com/javaworld/javatips/jw-javatip36.html">here</a>.
     * <p>
     * @param		algorithm	the digest algorith to use for hashing.
     *           				Should be one of the algorithms recognised by the
     *           				java.security.MessageDigest::getInstance() method.
     * @param 		inString	string to encode
     * @return					a string encoded with the algorithm given then base64 encoded.
     *        					String will be of format "EncodedString", not of the format
     *        				 	"{ALGORITHM}EncodedString".
     * @exception 	NoSuchAlgorithException If a non-existent is passed in.
     */
    protected final String base64Digest( String algorithm, String inString ) 
	throws NoSuchAlgorithmException 
	{
	    byte[] byteData = inString.getBytes();
	    byte[] hashedData;
	    byte[] base64Data;
	    
	    MessageDigest digest = MessageDigest.getInstance(algorithm);
	    
	    hashedData = digest.digest(byteData);
	    
	    base64Data = base64Encode(hashedData);
	    
	    return new String(base64Data);
    }
    
    /**
     * Converts an array of bytes to a base64 encoded array of bytes. 
     * <p> 
     * Code for this method was taken from a page on javaworld.com.  This page can be
     * found at:<br> 
     * <a href="http://www.javaworld.com/javaworld/javatips/jw-javatip36.html">here</a>.
     * <p>
     * @param 	byteData	array of bytes to encode.
     * @return 				the base64 encoded array of bytes
     */
    protected final static byte[] base64Encode(byte[] byteData) 
    {
	    if (byteData == null) return null;
	    int iSrcIdx; // index into source (byteData)
	    int iDestIdx; // index into destination (byteDest)
	    byte byteDest[] = new byte[((byteData.length+2)/3)*4];
    	
	    for (iSrcIdx=0, iDestIdx=0; iSrcIdx < byteData.length-2; iSrcIdx += 3) {
	        byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx] >>> 2) & 077);
	        byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx+1] >>> 4) & 017 |
					    (byteData[iSrcIdx] << 4) & 077);
	        byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx+2] >>> 6) & 003 |
					    (byteData[iSrcIdx+1] << 2) & 077);
	        byteDest[iDestIdx++] = (byte)  (byteData[iSrcIdx+2] & 077);
	    }
    	
	    if (iSrcIdx < byteData.length) {
	        byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx] >>> 2) & 077);
	        if (iSrcIdx < byteData.length-1) {
		    byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx+1] >>> 4) & 017 |
					        (byteData[iSrcIdx] << 4) & 077);
		    byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx+1] << 2) & 077);
	        }
	        else
	        byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx] << 4) & 077);
	    }
    	
	    for (iSrcIdx = 0; iSrcIdx < iDestIdx; iSrcIdx++) {
	        if (byteDest[iSrcIdx] < 26) byteDest[iSrcIdx] = (byte)(byteDest[iSrcIdx] + 'A');
	        else if (byteDest[iSrcIdx] < 52) byteDest[iSrcIdx] = (byte)(byteDest[iSrcIdx] + 'a'-26);
	        else if (byteDest[iSrcIdx] < 62) byteDest[iSrcIdx] = (byte)(byteDest[iSrcIdx] + '0'-52);
	        else if (byteDest[iSrcIdx] < 63) byteDest[iSrcIdx] = (byte)'+';
	        else byteDest[iSrcIdx] = (byte)'/';
	    }

	    for ( ; iSrcIdx < byteDest.length; iSrcIdx++)
	        byteDest[iSrcIdx] = (byte)'=';

	    return byteDest;
    }

	/**
	 * Applies password encryption if userPasswordEncryption() returns true
	 * <p>
	 * Code for this method was taken from a page on javaworld.com.  This page can be
     * found at:<br> 
     * <a href="http://www.javaworld.com/javaworld/javatips/jw-javatip36.html">here</a>.
	 * @param 	password
	 * @return 	encrypted password
	 * @throws NoSuchAlgorithmException
	 */
    protected String applyPasswordEncrpytion( String password )
    throws NoSuchAlgorithmException
    {
        if ( this.ldapConnectionImpl.userPasswordEncryption().booleanValue() == true )
        {
            return( "{SHA}" + base64Digest( "SHA1", password ) );           
        }
        else
        {
            // do nothing...
            return( password );
        }
    }
        
//************************************************************************
// Attributes
//************************************************************************

	/**
	 * Framework ldap connection implementation
	 */
    protected LDAPConnectionImpl ldapConnectionImpl		= null;
    /**
     * ldap user information abstraction
     */
    protected ILDAPUserInfo ldapUserInfo         		= null;
    
    protected String userID                          	= null;
    protected String password                        	= null;
    protected String appID                           	= null;
    
    /**
     * if null, uses the default name specfied in framework.xml
     */
    protected String ldapConnPoolName					= null;
}

/*
 * Change Log:
 * $Log: FrameworkLDAPSecurityAdapter.java,v $
 */



