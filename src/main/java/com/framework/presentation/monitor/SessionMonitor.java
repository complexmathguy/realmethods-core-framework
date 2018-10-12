/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.presentation.monitor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.framework.common.FrameworkBaseObject;

/**
 * Maintains a map of user identifiers to HttpSessions
 * <p>
 * @author    realMethods, Inc.
 */
public class SessionMonitor extends FrameworkBaseObject 
    implements java.io.Serializable
{
//************************************************************************    
// Public Methods
//************************************************************************
    /** 
    * Empty constructor.
    */
    public SessionMonitor()
    {
        // Instantiate HashMap
        sessionMap = new HashMap();
    }

    /**
     * Queries the internal Session Map for HTTPSessions
     * associated with the UserID.
     * 
     * @param 		userID
     * @return	 	boolean
     */
    public synchronized boolean containsUser( String userID )
    {
        return sessionMap.containsKey( userID );
    }
    
    /**
     * Clears the map.
     */
    public synchronized void clear()
    {
        sessionMap.clear();   
    }
    
    /**
     * Checks whether the associated session is contained in the map.
     * 
     * @param	session
     * @return 	true if contained
     */
    public synchronized boolean containsSession( HttpSession session )
    {
       return sessionMap.containsValue( session );
    }
    
    /**
     * Returns a set view of the mappings contained in this map. Each element 
     * in the returned set is a Map.Entry. The set is backed by the map, so changes 
     * to the map are reflected in the set, and vice-versa. If the map is modified 
     * while an iteration over the set is in progress, the results of the iteration 
     * are undefined. The set supports element removal, which removes the corresponding 
     * mapping from the map, via the Iterator.remove, Set.remove, removeAll, retainAll 
     * and clear operations. It does not support the add or addAll operations.
     * <p
     * @return Set.
     */
    public synchronized Set entrySet()
    {
       return sessionMap.entrySet(); 
    }
    
    /**
     * Retrieves the associated HttpSession from the map from the 
     * key passed in (userID). Returns null if the map contains no 
     * mapping for this key. A return value of null does not necessarily 
     * indicate that the map contains no mapping for the key; it's also 
     * possible that the map explicitly maps the key to null. 
     * <p>
     * @param 	userID
     * @return 	HttpSession 
     */
    public synchronized HttpSession getSession( String userID )
    {
       return (HttpSession)sessionMap.get( userID ); 
    }
    
    /**
     * Returns true if the Session Map contains no entries.
     * <p>
     * @return boolean
     */
    public synchronized boolean isEmpty()
    {
        return sessionMap.isEmpty();   
    }
    
    /**
     * Returns a set view of the keys contained in this map. 
     * The set is backed by the map, so changes to the map are 
     * reflected in the set, and vice-versa. If the map is modified 
     * while an iteration over the set is in progress, the results 
     * of the iteration are undefined. The set supports element removal, 
     * which removes the corresponding mapping from the map, via the Iterator.remove, 
     * Set.remove, removeAll retainAll, and clear operations. It does not support 
     * the add or addAll operations.
     * <p>
     * @return	Set
     */
    public synchronized Set keySet()
    {
        return sessionMap.keySet();   
    }

    /**
     * Associates the specified HttpSession with the specified userid in this map. 
     * If the map previously contained a mapping for this key, the old value is replaced.
     * <p>
     * @param	userID		the key
     * @param 	session		the value
     * @return 	provided  	HttpSession	
     */
    public synchronized HttpSession put( String userID, HttpSession session )
    {
        logDebugMessage("Adding User ID : " + userID + " to the SessionMonitor.");        
        return (HttpSession)sessionMap.put( userID, session );   
    }

    /**
     * Removes the mapping for this userID from this map if present.
     * <p>
     * @param 	userID		key
     * @return 	HttpSession 
     */
    public synchronized HttpSession remove( String userID )
    {
        logDebugMessage("Removing User ID : " + userID + " from the SessionMonitor.");        
        return (HttpSession)sessionMap.remove( userID );   
    }
    
    /**
     * Returns the number of key-value mappings.
     * 
     * @return int
     */
    public synchronized int size()
    {
        return sessionMap.size();   
    }
    
    /**
     * Returns a Collection of all the HttpSessions in the map.
     * <p>
     * @return Collection
     */
    public synchronized Collection values()
    {
        return sessionMap.values();   
    }
//************************************************************************    
// Private / Protected Methods
//************************************************************************

//************************************************************************    
// Attributes
//************************************************************************
    /**
     * Internal Hashmap.  The key into the map is UserID.
     */
    private HashMap sessionMap = null;
}

/*
 * Change Log:
 * $Log: SessionMonitor.java,v $
 */
