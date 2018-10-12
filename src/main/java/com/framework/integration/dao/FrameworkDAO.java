/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/

package com.framework.integration.dao;

import java.util.*;

import com.framework.business.bo.FrameworkBusinessObject;

import com.framework.business.pk.FrameworkPrimaryKey;
import com.framework.business.pk.IFrameworkPrimaryKey;

import com.framework.business.vo.IFrameworkValueObject;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.FrameworkDAOException;
import com.framework.common.exception.VersionUpdateException;

import com.framework.common.misc.Utility;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.integration.cache.IFrameworkCache;
import com.framework.integration.cache.FrameworkCacheFactory;

import com.framework.integration.notify.IValueObjectNotificationManager;
import com.framework.integration.notify.ValueObjectNotificationEvent;
import com.framework.integration.notify.ValueObjectNotificationManager;
import com.framework.integration.notify.ValueObjectNotificationType;

/**
 * The base class for the concept of a Data Access Object (DAO) within the Framework
 * <p>
 * If framework.xml property DAO_CACHE_INTERNALLY is set to true, a static instance
 * of IFrameworkCache is used to cache read, created, or updated IFrameworkValueObjects.
 * <p> 
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.notify.ValueObjectNotificationManager
 * @see		  com.framework.integration.notify.ValueObjectNotificationEvent
 */
abstract public class FrameworkDAO 
    extends FrameworkBaseObject
    implements IFrameworkDAO
{
    
	public FrameworkDAO()
	{
		if ( useCache == null )
		{
			useCache = new Boolean( Utility.getFrameworkProperties().getProperty( FrameworkNameSpace.DAO_CACHE_INTERNALLY, "false" ) );
			logDebugMessage( "DAO internal caching is turned " + ( useCache.booleanValue() ? "ON." : "OFF." ) );
			
			try
			{
				cache = FrameworkCacheFactory.getInstance().createFrameworkReferencedCache();
			}
			catch( Throwable exc )
			{
				logErrorMessage( "FrameworkDAO() - failed to create a FrameworkCache from the FrameworkCacheFactory...will force caching OFF." );
				useCache = new Boolean( false );			
			}
		}
	}
	
    
// IFrameworkDAO implementations - really helpers to the sub-class

   /**
	* Creates an associated value object.
	* <p>
	* This method will create/insert the corresponding 
	* bean in the database based on the provided model
	*
	* @param       vo			the source to create within the persistent store
	* @return      				the result of the creation	
	* @exception   FrameworkDAOException
	*/
   public IFrameworkValueObject create( IFrameworkValueObject vo )
   throws FrameworkDAOException
    {        
        try
        {
        	postCreate( vo );
        }
        catch( Throwable exc )
        {
            logErrorMessage( "FrameworkDAO:create(...) - " + exc );
        }
        
        return( vo );
    }

   
	/**
	 * Stores the provided value object to the database.
	 *
	 * @param       vo		source of data to store
	 * @return		the results of saving to the persistent store
	 * @exception   FrameworkDAOException
	 * @exception   VersionUpdateException	thrown if vo suppoorts versioning and what is persisted isn't this version
	 */
	public IFrameworkValueObject save( IFrameworkValueObject vo )
	throws FrameworkDAOException, VersionUpdateException
    {        
        try
        {
        	postSave( vo );
        }
        catch( Exception exc )
        {
            logInfoMessage( "FrameworkDAO:save(...) - " + exc );
        }
        
        return( vo );
    }
    
	/**
	 * Retrieves the model from the persistent store, using the provided primary key. 
	 * If no match is found, a null IFrameworkValueObject is returned.
	 * <p>
	 * @param       pk  	identifier of entity to locate from the persistent store   
	 * @return      IFrameworkValueObject
	 * @exception   FrameworkDAOException
	 */
	public IFrameworkValueObject find( FrameworkPrimaryKey pk ) 
	throws FrameworkDAOException
	{
		return( getFromCache( pk ) );		
	}
	

	/**
	 * Retrieves all entities of the associated sub-class type.
	 * <p>
	 * @return      Collection<FrameworkBusinessObject>
	 * @exception   FrameworkDAOException
	 */
	public Collection<FrameworkBusinessObject> findAll() 
	throws FrameworkDAOException
	{
		throw new RuntimeException( "FrameworkDAO.findAll() - must be implemented by subclass." );    
	}
	
	
	/**
	 * Removes the associated model from the database.
	 *
	 * @param        key		identifier of object to remove from the persistent store
	 * @return       success or fail
	 * @exception    FrameworkDAOException
	 */
	 public boolean delete( FrameworkPrimaryKey key ) 
	 throws FrameworkDAOException 
    {
        if ( key != null )
        {
        	postDelete( valueObject );        	            
        }        
        
        // from our perspective, deletion was handled
        return( true );
    }    
    
    /**
     * Factory notification method of being released that the 
     * consumer is finished.
     */    
    public void release()
    {
        // no op
    }

    /**
     * Assign true/false to whether or not the DAO should automatically
     * release the associated connection after using it.
     * <p>
     * @param       release
     */
    public void autoReleaseConnection( boolean release )    
    {
        autoReleaseConnection = release;
    }
    
    /**
     * Returnes true/false to whether or not the DAO should automatically
     * release the associated connection after using it.
     * <p>
     * @return  boolean
     */
    public boolean autoReleaseConnection()    
    {
        return( autoReleaseConnection );
    }
    
    /**
     * Commit the assocate connection
     * <p>
     * @exception       FrameworkDAOException
     */
    public void commit()
    throws FrameworkDAOException
    {
    }
    
    /**
     * Rollback the associated connection
     *
     * @exception       FrameworkDAOException
     */
    public void rollback()
    throws FrameworkDAOException
    {
    }
        
// access methods

    /**
     * Retrieves the a value object representation of the entity bean.
     * @return IFrameworkValueObject
     */
    public IFrameworkValueObject getValueObject()
    {
        return( valueObject );
    }
    
    
    /**
     * Generate a Framework internal identifier, which is not guaranteed to
     * be unique.  Overload this method if more precision is necessary.
     * <p>
     * @return      String
     * @see			com.framework.common.misc.Utility#generateUID()
     */
    static public String generateUID()
    {
    	return( Utility.generateUID() );
    }

// protected methods

    protected IFrameworkValueObject postCreate( IFrameworkValueObject vo )
    {
	    try
	    {
			// apply to the internal cache
			cache( valueObject );
	    	
	        // notify interested parties of the creation
	        ValueObjectNotificationEvent notifyEvent = new ValueObjectNotificationEvent( vo, ValueObjectNotificationType.Create );
	        ValueObjectNotificationManager.getValueObjectNotificationManager().notifyValueObjectListeners( notifyEvent );                                    
	    }
	    catch( Exception exc )
	    {
	        logErrorMessage( "FrameworkDAO:notifyCreate(...) - " + exc );
	    }
	    
	    return( vo );
    }

    protected IFrameworkValueObject postSave( IFrameworkValueObject vo )
    {
        try
        {
        	// assign it internally
            setValueObject( vo );
            
            // attempt to cache it...
            cache( vo );                    	
        	
            // notify anybody interested in the model change
            ValueObjectNotificationEvent event = new ValueObjectNotificationEvent( vo, ValueObjectNotificationType.Update );        
            ValueObjectNotificationManager.getValueObjectNotificationManager().notifyValueObjectListeners( event );
                        
        }
        catch( Exception exc )
        {
            logInfoMessage( "FrameworkDAO:notifySave(...) - " + exc );
        }
        
        return( vo );
    	
    }

    protected void postDelete( IFrameworkValueObject vo )
    {
    	try
		{	
			// remove from the local cache
			removeFromCache( vo.getFrameworkPrimaryKey() );            
    		
    		ValueObjectNotificationEvent event = new ValueObjectNotificationEvent( vo, ValueObjectNotificationType.Delete );                    
    		IValueObjectNotificationManager mnmgr = ValueObjectNotificationManager.getValueObjectNotificationManager();
    		mnmgr.notifyValueObjectListeners( event );
		}
    	catch( Exception exc )
		{
    		logErrorMessage( "FrameworkDAO:notifyDelete(valueObject) - " + exc );
		}
    }
    
    
   /**
	* Cache retrieval, if caching is turned on.  If not enabled, returns null.
	* @param		key		key to object in the cache
	* @return		value corresponding to key in the cache
	*/
   	protected IFrameworkValueObject getFromCache( IFrameworkPrimaryKey key )
   	{
	   	IFrameworkValueObject vo = null;
		
	   	if ( useCache.booleanValue() == true )
	   	{
		   	vo = (IFrameworkValueObject)cache.get( key );
		   	if ( vo != null )
			   	logDebugMessage(  "FrameworkDatabaseDAO retrieved " + key + " from the local cache." );    		
	   	}
		return vo;    		
	}
    
   /**
	* Remove from the cache, if caching is turned on.
	* @param		key		key to object in the cache
	*/
	protected void removeFromCache( IFrameworkPrimaryKey key )
	{
		if ( useCache.booleanValue() == true )
		{
		   cache.remove( key );
		   logDebugMessage(  "FrameworkDAO removed " + key + " from the local cache." );
		} 		
	}
	     
   /**
	* Place in the cache, if caching is turned on
	* @param vo		to cache
	*/
   	protected void cache( IFrameworkValueObject vo )
   	{
		if ( useCache.booleanValue() == true )
	   	{
			cache.assign( vo.getFrameworkPrimaryKey(), vo );
		   	logDebugMessage(  "FrameworkDAO cached " + vo.getFrameworkPrimaryKey() );    		
	   	}
   	}
    
    /**
     * Returns an empty Framework primary key shell.  This is required
     * to determine the number and types of out parameters for such
     * required stored procedures.  Override this method in the event
     * you have a compound key, or a key that IS NOT a Long
     * <p>
     * @return      FrameworkPrimaryKey
     */
    protected FrameworkPrimaryKey getFrameworkPrimaryKeyShell()
    {
        return( new FrameworkPrimaryKey( new Long( 0 ) ) );
    }

    /**
     * Sets the value objectrepresentation of the entity bean.
     *
     * @param	vo
     */
    protected void setValueObject( IFrameworkValueObject vo )
    {
        valueObject = vo;   
    }

// attributes

    /**
     * Associated model being worked on
     */
    protected IFrameworkValueObject valueObject	= null;

    
    /**
     * auto release indicator
     */
    protected boolean autoReleaseConnection 		= true;
    
	/**
	 * internal global dao cache indicator
	 */
	static private Boolean useCache = null;
    
	/**
	 * global dao cache, not useful in clustered scenarios  for now...
	 */
	static private IFrameworkCache cache = null;
        
}

/*
 * Change Log:
 * $Log: FrameworkDAO.java,v $
 */
