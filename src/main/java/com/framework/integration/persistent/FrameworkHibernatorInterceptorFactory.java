/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.persistent;

import com.framework.common.FrameworkBaseObjectFactory;
import com.framework.common.misc.Utility;

public class FrameworkHibernatorInterceptorFactory
extends FrameworkBaseObjectFactory 
{
	
	/** 
	 * constructor - deter instantiation 
	 */ 
	protected FrameworkHibernatorInterceptorFactory()
	{
		super( false );
	}

	
	static public FrameworkHibernatorInterceptorFactory getInstance()
	{ 
	   if ( instance == null )
		   instance  =  new FrameworkHibernatorInterceptorFactory();
	 		
	   return( instance );	 		
	}	 
	
	/**
	 * Factory method used to create a FrameworkHibernatorInterceptor.
	 * The value is retrieved from the framework.xml file AuditTrailInterceptor 
	 * property
	 * <p>
	 * @return      FrameworkHibernatorInterceptor
	 */
	public FrameworkHibernatorInterceptor getHibernateInterceptor()
	{	    
		if ( interceptor == null )
		{
			String nameToInstantiate = Utility.getFrameworkProperties().getProperty( "AuditTrailInterceptor", "" );
			
			if ( nameToInstantiate == null || nameToInstantiate.length() == 0 )
			{
				nameToInstantiate = "com.framework.integration.persistent.FrameworkHibernatorInterceptor";				
				logDebugMessage( "FrameworkHibernatorInterceptorFactory.getHibernateInterceptor()- framework property AuditTrailInterceptor not found, using default value " + nameToInstantiate );
			}
			
			try
			{
				interceptor = (FrameworkHibernatorInterceptor)getObject( nameToInstantiate );	
			}
			catch( Throwable exc )
			{
				logErrorMessage( "FrameworkHibernatorInterceptorFactory.getHibernateInterceptor() - " + exc );
			}
		}
			
		return( interceptor );    	
	}
		

// attributes

	/**
	 * Singleton factory instance
	 */
	private static FrameworkHibernatorInterceptorFactory instance = null;
	protected FrameworkHibernatorInterceptor interceptor = null;

}
 
