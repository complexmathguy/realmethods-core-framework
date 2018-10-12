/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/

package com.framework.integration.dao;

/* hibernate 2.1
import net.sf.hibernate.*;
*/

import org.hibernate.*; 

import com.framework.common.FrameworkBaseObject;
import com.framework.common.properties.PropertyFileLoader;
import com.framework.integration.persistent.FrameworkHibernatorInterceptor;
import com.framework.integration.persistent.FrameworkHibernatorInterceptorFactory;
import com.framework.common.misc.Utility;
import com.framework.integration.persistent.FrameworkPersistenceHelper;

/**
 * Base class for all Hibernate DAO classes generated and supported
 * by the framework.
 * 
 * @author	realMethods, Inc.
 */
public class FrameworkHibernateDAO extends FrameworkDAO
{
//	public static SessionFactory sessionFactory = null;
	public static final ThreadLocal session = new ThreadLocal();
	protected Session externalSession = null;
	protected Transaction externalTransaction = null;
	
	static 
	{
		try 
		{
			// Create the SessionFactory
			//sessionFactory = PropertyFileLoader.getInstance().getHibernateConfiguration().buildSessionFactory();
			//logInfoMessage( "FrameworkHibernateDAO - Initial Hibernate SessionFactory creation passed. " );
		} 
		catch (Throwable ex) 
		{
			//logErrorMessage( "Initial Hibernate SessionFactory creation failed - " +  ex.getMessage() );
		}
	}
	
	static public SessionFactory getSessionFactory()
	{
		return( FrameworkPersistenceHelper.self().getSessionFactory() );
	}
	
	public Session currentSession() 
	throws HibernateException 
	{
		if ( externalSession != null )
			return( externalSession );
		else
		{
			Session s = (Session) session.get();
		
			// Open a new Session, if this Thread has none yet or the session is closed or not connected
			if (s == null || !s.isOpen() || !s.isConnected() ) 
			{
				// Note: dynamically create the class Interceptor and apply here,			
				// if one is in use ...			
				
				s = getSessionFactory().openSession( /*getAuditTrailInterceptor() */);
				session.set(s);
				s.setFlushMode( FlushMode.COMMIT );
			}
			return s;
		}
	}

	public void closeSession() 
	throws HibernateException 
	{
		if ( externalSession == null )
		{
			Session s = (Session) session.get();
			session.set(null);
			
			if (s != null)
			{
				s.close();
			}
		}
	}

	protected void commitTransaction( Transaction transaction )
	throws HibernateException
	{
		if ( externalTransaction != transaction )
			transaction.commit();
		
		// do nothing otherwise since the transaction is external to us...
	}

	protected void rollbackTransaction( Transaction transaction )
	throws HibernateException
	{
		if ( externalTransaction != transaction )
			transaction.rollback();
		
		// do nothing otherwise since the transaction is external to us...
	}
	
	protected Transaction currentTransaction( Session s )
	throws HibernateException
	{
		if ( externalTransaction != null )
			return( externalTransaction );
		else
			return( s.beginTransaction() );
	}
	
	public void assignExternalSession( Session session )
	{
		externalSession = session;
	}
	
	public void assignExternalTransaction( Transaction transaction )
	{
		externalTransaction = transaction;
	}
	
	protected FrameworkHibernatorInterceptor getAuditTrailInterceptor()
	{
		return FrameworkHibernatorInterceptorFactory.getInstance().getHibernateInterceptor();		
	}
	
}
