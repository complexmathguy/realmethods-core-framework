/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.persistent;

import java.io.Serializable;
import java.util.Iterator;

import org.hibernate.*;
import org.hibernate.type.*;

import com.framework.common.FrameworkBaseObject;

/**
 * This class is provided do a new Hibernate Session in order to apply
 * a time stamp to any Auditable class being persisted.  This is 
 * a good place to put other audit trail functionality related to 
 * persistence actions.
 * 
 * @author realMethods, Inc.
 */
public class FrameworkHibernatorInterceptor
	extends EmptyInterceptor 
	implements Serializable 
{

	//private int updates;
	//private int creates;
/*
	public String onPrepareStatement( String stmt )
	{
		return( stmt );
	}
	
	public void onCollectionRemove( Object object, Serializable serial )
	{}
	
	public void onCollectionUpdate( Object object, Serializable serial )
	{}
	
	public void onCollectionRecreate( Object object, Serializable serial )
	{}
	
	public void onDelete(Object entity,
						 Serializable id,
						 Object[] state,
						 String[] propertyNames,
						 Type[] types) 
	{
		FrameworkBaseObject.logDebugMessage( "FrameworkHibernatorInterceptor:onDelete() - " + entity.toString() );		
	}
*/
	public boolean onFlushDirty(Object entity, 
								Serializable id, 
								Object[] currentState,
								Object[] previousState,
								String[] propertyNames,
								Type[] types) 
	{
//		FrameworkBaseObject.logDebugMessage( "FrameworkHibernatorInterceptor:onFlushDirty() - " + entity.toString() );
		
		if ( entity instanceof com.framework.integration.persistent.Auditable ) 
		{
	//		updates++;
			
			for ( int i=0; i < propertyNames.length; i++ ) 
			{
				if ( "lastUpdateTimestamp".equals( propertyNames[i] ) ) 
				{
					new FrameworkBaseObject().logDebugMessage( "Applying lastUpdateTimeStamp to " + entity.toString() );					
					currentState[i] = new java.util.Date();
					return true;
				}
			}
		}
		
		return false;
	}

	public boolean onLoad(Object entity, 
						  Serializable id,
						  Object[] state,
						  String[] propertyNames,
						  Type[] types) 
	{		
		if ( entity instanceof Persistent )
		{
			((Persistent)entity).onLoad();
			return( true );
		}
		else
			new FrameworkBaseObject().logDebugMessage( "FrameworkHibernatorInterceptor:onLoad() - object doesn't implemente Persistent interface : " + entity.toString() );

		return( false );
	}

	public boolean onSave(Object entity,
						  Serializable id,
						  Object[] state,
						  String[] propertyNames,
						  Type[] types) 
	{   
		boolean saveIndicator = false;
		
		if ( entity instanceof Auditable ) 
		{
		//	creates++;
			for ( int i=0; ((i<propertyNames.length) && !(saveIndicator)); i++ ) 
			{
				if ( "createTimestamp".equals( propertyNames[i] ) ) 
				{
					new FrameworkBaseObject().logDebugMessage( "Applying createTimeStamp to " + entity.toString() );					
					state[i] = new java.util.Date();
					saveIndicator = true;
				}
			}						
		}
		else
			new FrameworkBaseObject().logDebugMessage( "FrameworkHibernatorInterceptor:onSave() - object doesn't implement Auditable interface : " + entity.toString() );
		
		if ( !saveIndicator )		
			saveIndicator = checkOnSave( entity );
		else
			checkOnSave( entity );
						
		return saveIndicator ;
	}

/*	public void postFlush(Iterator entities) 
	{
		// FrameworkBaseObject.logDebugMessage("Creations: " + creates + ", Updates: " + updates);
	}
*/
	public void preFlush(Iterator entities) 
	{
		//updates=0;
		//creates=0;
	}
	
	public Object instantiate(Class clazz, Serializable id)
	{
		// call default constructor for an instance of clazz
		return( null );	  	
	}
	

	public Boolean isUnsaved(Object entity)
	{		
		boolean unsaved = true;
		
		if ( entity instanceof Persistent )
		{
			unsaved = !((Persistent)entity).isSaved();
		}
		
		return( new Boolean( unsaved ) );
	}
	
	public int[] findDirty(Object entity,
						   Serializable id,
						   Object[] currentState,
						   Object[] previousState,
						   String[] propertyNames,
						   Type[] types)
	{
		// use Hibernate's default dirty checking algorithm
		return( null );	
	}
	
	protected boolean checkOnSave( Object entity )
	{	
		if ( entity instanceof Persistent )
		{			
			((Persistent)entity).onSave();
			return( true );
		}
		else
			new FrameworkBaseObject().logDebugMessage( "FrameworkHibernatorInterceptor:checkOnSave() object doesn't implement Persistent interface : " + entity.toString() );

		return( false );		
		
	}
/*	
	public void afterTransactionBegin(Transaction tx)
	{}
	
	public void beforeTransactionCompletion(Transaction tx)
	{}
	
	public void afterTransactionCompletion(Transaction tx)
	{}
	
	public Object getEntity( String entityName, Serializable id )
    throws CallbackException
	{
		// use Hibernate's default dirty checking algorithm		
		return(null);
	}
	
	public String getEntityName(Object object)
    throws CallbackException
	{
		String name = null;
		// use Hibernate's default dirty checking algorithm		
		return( name );
	}

	public Object instantiate(String entityName,
            					EntityMode entityMode,
								Serializable id)
    throws CallbackException
	{		
		// use Hibernate's default dirty checking algorithm		
		return( null );
	}
	
	public Boolean isTransient(Object entity)
	{	
//		FrameworkBaseObject.logDebugMessage( "FrameworkHibernatorInterceptor:isTransient() for Persistent Entity - " + entity.toString() );
		
		// use Hibernate's default dirty checking algorithm		
		return( new Boolean(false) );
	}
*/	
 }
 
