package com.framework.integration.persistent;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.*;
import org.hibernate.service.*;

import com.framework.common.FrameworkBaseObject;
import com.framework.common.namespace.FrameworkNameSpace;
import com.framework.common.properties.PropertyFileLoader;
import com.framework.integration.persistent.FrameworkHibernatorInterceptorFactory;

public class FrameworkPersistenceHelper
{
    private static final ThreadLocal session = new ThreadLocal();
    private static FrameworkPersistenceHelper self = null;
    private static Configuration cfg = null;
    static private SessionFactory sessionFactory;
    
    static public FrameworkPersistenceHelper self()
    {
    	if ( self == null )
    		self = new FrameworkPersistenceHelper();
    	return( self );
    }
    
    public void jumpStart()
    { 
    	// no_op
    }

//********************    
//HIBERNATE RELATED
//********************
    static 
    {
    	createSessionFactory();
    }

    public SessionFactory getSessionFactory() 
    {
    	if ( sessionFactory == null || sessionFactory.isClosed() )
    		self().createSessionFactory();
    	
        return sessionFactory;
    }
    
    public Session getCurrentSession()
    {
    	Session s = (Session) session.get();

    	// Open a new Session, if this Thread has none yet or has been closed or disconnected
		if (s == null || s.isOpen() == false || s.isConnected() == false ) 
		{
			// Note: dynamically create the class Interceptor and apply here,			
			// if one is in use ...			
			s = getSessionFactory().openSession( /*FrameworkHibernatorInterceptorFactory.getInstance().getHibernateInterceptor()*/);
			session.set(s);
			s.setFlushMode( FlushMode.COMMIT );
		}
		return s;    	
    }
    
    public void closeSession()
    {
		Session s = (Session) session.get();
		session.set(null);
		
		if (s != null)
		{
			s.close();
		}
    }

//********************    
// JDO RELATED
//********************
    
    private PersistenceManagerFactory pmFactory()
	{
		return com.framework.integration.persistent.jdo.PMF.get();
	}

	public PersistenceManager getPersistenceManager()
	{
		if (session.get() == null)
		{
			session.set(pmFactory().getPersistenceManager());
		}
		return (PersistenceManager)session.get();
	}

    public void closePersistenceManager()
    {
    	PersistenceManager pm = (PersistenceManager) session.get();
		session.set(null);
		
		if (pm != null && !pm.isClosed() )
		{
			pm.close();
		}
    }

//********************    
// JPA RELATED
//********************
	
	private EntityManagerFactory emFactory()
	{
		return  com.framework.integration.persistent.jpa.EMF.get();
	}

	public EntityManager getEntityManager()
	{
		if (session.get() == null)
		{
			session.set(emFactory().createEntityManager());
		}
		return (EntityManager)session.get();
	}
	
	public void closeEntityManager()
	{
    	EntityManager em = (EntityManager) session.get();
		session.set(null);
		
		if (em != null && em.isOpen())
		{
			em.close();
		}		
	}
	
	static protected void createSessionFactory()
	{
	    try 
	    {
	        // up front, create the SessionFactory from hibernate.cfg.xml since it takes for ever to laod
	    	if ( FrameworkNameSpace.USING_HIBERNATE )
	    	{
	    		if ( cfg == null )
	    		{
	    			cfg = PropertyFileLoader.getInstance().getHibernateConfiguration();
	    		
		    		if ( cfg != null )
		    			System.out.println( "** hibernate config is : " + cfg.toString() );
		    		else
		    			System.out.println( "\n\n** hibernate config is null!!!\n\n" );
		    		
		    		// check to see if we need to override the connection related default params if provided
		    		// as runtime args (connection url, user name, and password)
		    		String dbConnUrl = java.lang.System.getProperty( "DB_CONNECTION_URL" );
		    		
		    		if ( dbConnUrl != null && dbConnUrl.length() > 0 )
		    		{
		    			System.out.println( "Setting hibernate.connection.url to env var value " + dbConnUrl );	    			
		    			cfg = cfg.setProperty( "hibernate.connection.url", dbConnUrl );
		    		}
		    		String dbUserName = java.lang.System.getProperty( "DB_USER_NAME" );
		    		
		    		if ( dbUserName != null && dbUserName.length() > 0 )
		    		{
		    			System.out.println( "Setting hibernate.connection.username to env var value " + dbUserName );
		    			cfg = cfg.setProperty( "hibernate.connection.username", dbUserName);
		    		}
		    		String dbPassword = java.lang.System.getProperty( "DB_PASSWORD" );
		    		
		    		if ( dbPassword != null && dbPassword.length() > 0 )
		    		{
		    			System.out.println( "Setting hibernate.connection.password to env var value " + dbPassword );
		    			cfg = cfg.setProperty( "hibernate.connection.password", dbPassword );
		    		}
	
		    		String dbDriver= java.lang.System.getProperty( "DB_DRIVER" );
		    		
		    		if ( dbDriver != null && dbDriver.length() > 0 )
		    		{
		    			System.out.println( "Setting hibernate.connection.driver_class to env var value " + dbDriver );
		    			cfg = cfg.setProperty( "hibernate.connection.driver_class", dbDriver );
		    		}
	
		    		String dbDialect = java.lang.System.getProperty( "DB_DIALECT" );
		    		
		    		if ( dbDialect != null && dbDialect.length() > 0 )
		    		{
		    			System.out.println( "Setting hibernate.dialect to env var value " + dbDialect );
		    			cfg = cfg.setProperty( "hibernate.dialect", dbDialect );
		    		}
	    		}
	    		
	    		ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder().applySettings(cfg.getProperties());
	    		sessionFactory = cfg.buildSessionFactory(serviceRegistryBuilder.buildServiceRegistry());
        	    		
	    		new FrameworkBaseObject().logInfoMessage( "Utility:static - Initial Hibernate SessionFactory creation passed. " );
	    	}
	    	else
	    	{
	    		sessionFactory = null;
	    	}
	    } 
	    catch (Throwable ex) 
	    {
	    	sessionFactory = null;
	            // Make sure you log the exception, as it might be swallowed
	        // Create the SessionFactory from hibernate.cfg.xml
	    	if ( FrameworkNameSpace.USING_HIBERNATE )
	    	{
	            System.err.println("Warning!!! - Hibernate Session unitialized..." + ex.toString() );
	            ex.printStackTrace();
	//          throw new ExceptionInInitializerError(ex);
	    	}
	    }
		
	}
}