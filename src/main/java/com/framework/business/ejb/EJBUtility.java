/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.business.ejb;

import java.util.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.framework.common.FrameworkBaseObject;
import com.framework.integration.locator.EJBServiceLocator;

/**
 * Utility class used to provide the ejb related classes of the Business Tier with 
 * common helper methods.
 * <p>
 * @author    realMethods, Inc.
 */
public class EJBUtility extends com.framework.common.misc.Utility
{
    /**
     * Delegates to EJBServiceLocator getInitialContext
     *
     * @return               	InitialContextContext
     * @exception           	NamingException
     */
    static public InitialContext getInitialContext() 
    throws NamingException 
    {
        return( EJBServiceLocator.getInstance().getInitialContext() );
    }   
   

    /**
     * Delegates to EJBServiceLocator lookup(String)
     *
     * @param		nameToLookup
     * @return     	Object
     * @exception	NamingException
     */
    static public Object lookupHomeInterface( String nameToLookup ) 
    throws NamingException 
    {
    	Object homeInterface =  null;
    	
        try
        {
            homeInterface = EJBServiceLocator.getInstance().lookup( nameToLookup );
        }
        catch( Exception exc )
        {            
            throw new NamingException( "EJBUtility:lookupHomeInterface() - " + exc );
        }
        
        return( homeInterface );
    }   

    /** 
     * Transforms a Collection of Framework Remote interfaces into
     * a Collection of IFrameworkValueObjects.  Returns null if the 
     * transformation fails.
     *
     * @param       remoteInterfaces	Collection of FrameworkRemotes
     * @return      Collection
     */
    static public Collection transformRemotesToBOs( Collection remoteInterfaces )
    {
        List bos = Collections.EMPTY_LIST;
        
        if ( remoteInterfaces != null )
        {
        	bos						= new ArrayList( remoteInterfaces.size() );
            Iterator iter           = remoteInterfaces.iterator();
            FrameworkRemote remote  = null;
            
            try
            {
                while( iter.hasNext() )
                {
                    remote = (FrameworkRemote)iter.next();    
                    bos.add( remote.getBusinessObject() );
                }
            }
            catch( Throwable exc )
            {
                bos = null;
                new FrameworkBaseObject().logErrorMessage( "eJBUtility:transformToValueObjects(Collection) - Failed to convert a Collection of RemoteInterfaces to a Collection of IFrameworkValueObjects - " + exc );
            }
        }
        
        return( bos );
    }
    

    /** 
     * Transforms a Collection of Framework local interfaces into
     * a Collection of IFrameworkValueObjects.  Returns null if the
     * transformation fails.
     *
     * @param       localInterfaces		Collection of FrameworkLocals
     * @return      Collection
     */
    static public Collection transformLocalsToBOs( Collection localInterfaces )
    {
        List bos =  Collections.EMPTY_LIST;
        
        if ( localInterfaces != null )
        {
        	bos				= new ArrayList( localInterfaces.size() );
            Iterator iter   = localInterfaces.iterator();
            FrameworkLocal local  = null;
            
            try
            {
                while( iter.hasNext() )
                {
                    local = (FrameworkLocal)iter.next();    
                    bos.add( local.getBusinessObject() );
                }
            }
            catch( Throwable exc )
            {
                bos = null;
                new FrameworkBaseObject().logErrorMessage( "EJBUtility:transformLocalsToValueObjects(Collection) - Failed to convert a Collection of LocalInterfaces to a Collection of IFrameworkValueObjects - " + exc );
            }
        }
        
        return( bos );
    }

}

/*
 * Change Log:
 * $Log:  $
 */
