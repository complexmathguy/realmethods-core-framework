/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.presentation.monitor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.ServletContext;

import com.framework.common.FrameworkBaseObject;

/**
 * Maintains a map of ApplicationInfo's.
 * <p>
 * @author    realMethods, Inc.
 */
public class ApplicationMonitor 
    extends FrameworkBaseObject implements java.io.Serializable 
{
//************************************************************************    
// Public Methods
//************************************************************************
    /** 
    * Empty constructor
    */
    public ApplicationMonitor()
    {
        // Instantiate HashMap
        applicationMap = new HashMap();
    }

    /** 
    * Constructor
    * 
    * @param	context
    */
    public ApplicationMonitor( ServletContext context )
    {
        // Instantiate HashMap
        applicationMap = new HashMap();
        
        setAppMonitorInServletContext( context );
    }

    /**
     * Queries the internal Application Map for ApplicationInfos
     * associated with the appID.
     * <p>
     * @param 	appID
     * @return 	boolean
     */
    public synchronized boolean containsApplication( String appID )
    {
        return applicationMap.containsKey( appID ) ;
    }
    
    /**
     * Clears the map.
     */
    public synchronized void clear()
    {
        applicationMap.clear();   
    }
    
    /**
     * Checks whether the associated application is contained in the map.
     * <p>
     * @param 	appInfo
     * @return 	boolean
     */
    public synchronized boolean containsApplication( ApplicationInfo appInfo )
    {
       return applicationMap.containsValue( appInfo );
    }
    
    /**
     * Returns a set view of the mappings contained in this map. Each element 
     * in the returned set is a Map.Entry. The set is backed by the map, so changes 
     * to the map are reflected in the set, and vice-versa. If the map is modified 
     * while an iteration over the set is in progress, the results of the iteration 
     * are undefined. The set supports element removal, which removes the corresponding 
     * mapping from the map, via the Iterator.remove, Set.remove, removeAll, retainAll 
     * and clear operations. It does not support the add or addAll operations.
     * <p>
     * @return A set view of the mappings contained in this session map.
     */
    public synchronized Set entrySet()
    {
       return applicationMap.entrySet(); 
    }
    
    /**
     * Retrieves the associated ApplicationInfo from the map from the 
     * key passed in (appID). Returns null if the map contains no 
     * mapping for this key. A return value of null does not necessarily 
     * indicate that the map contains no mapping for the key; it's also 
     * possible that the map explicitly maps the key to null. 
     * <p>
     * @param 	appID			the key
     * @return 	ApplicationInfo 
     */
    public synchronized ApplicationInfo getApplication( String appID )
    {
       return (ApplicationInfo)applicationMap.get( appID ); 
    }
    
    /**
     * Returns true if the Application Map contains no entries.
     * @return boolean
     */
    public synchronized boolean isEmpty()
    {
        return applicationMap.isEmpty();   
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
     * @return Set.
     */
    public synchronized Set keySet()
    {
        return applicationMap.keySet();   
    }

    /**
     * Associates the specified ApplicationInfo with the specified app id in this map. 
     * If the map previously contained a mapping for this key, the old value is replaced and returned
     * <p>
     * @param	appID
     * @param 	appInfo
     * @return 	ApplicationInfo 
     */
    public synchronized ApplicationInfo put(String appID, ApplicationInfo appInfo )
    {
        logDebugMessage("Adding Application ID : " + appID + " to the ApplicationMonitor.");
        
        return (ApplicationInfo)applicationMap.put(appID, appInfo );   
    }

    /**
     * Removes the mapping for this appIDIn from this map if present and returns it
     * <p>
     * @param 	appID			the key
     * @return 	ApplicationInfo
     */
    public synchronized ApplicationInfo remove( String appID )
    {
        logDebugMessage("Removing Application ID : " + appID + " to the ApplicationMonitor.");        
        
        return (ApplicationInfo)applicationMap.remove( appID );   
    }
    
    /**
     * Returns the number of key-value mappings.
     * @return int
     */
    public synchronized int size()
    {
        return applicationMap.size();   
    }
    
    /**
     * Returns a Collection of all the ApplicationInfos in the map.
     * @return Collection
     */
    public synchronized Collection values()
    {
        return applicationMap.values();   
    }

    /**
     * Puts the current application monitor in the servlet context.
     * <p>
     * @param		servletContext
     */
    public void setAppMonitorInServletContext( ServletContext servletContext )
    {
        if ( servletContext != null )
        {
            servletContext.setAttribute(APPLICATION_MONITOR_KEY, this);
        }
    }
    
    /** 
     * Removes the ApplicationMonitor from the given ServletContext.
     * <p>
     * @param 	servletContext
     */
    public void unsetAppMonitorInServletContext( ServletContext servletContext )
    {
        if ( servletContext != null )
        {
            servletContext.removeAttribute(APPLICATION_MONITOR_KEY);   
        }
    }
    
    /**
     * Retrieves the Application Monitor from the ServletContext
     * <p>
     * @param  context
     * @return ApplicationMonitor null if none are found.
     */
    public synchronized static ApplicationMonitor getAppMonitor( ServletContext context )
    {
        ApplicationMonitor returnMonitor = null;
        
        if ( context != null )
        {
            returnMonitor = (ApplicationMonitor)context.getAttribute(APPLICATION_MONITOR_KEY);
        }
        return returnMonitor;
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
    private HashMap applicationMap = null;
    
    /**
     * Application Monitor Key for (un)registering with the ServletContext.
     */
    private final static String APPLICATION_MONITOR_KEY = "ApplicationMonitorKey";
}

/*
 * Change Log:
 * $Log: ApplicationMonitor.java,v $
 */
