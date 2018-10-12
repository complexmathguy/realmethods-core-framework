/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.jmx;

import java.util.Properties;

import javax.management.MBeanServer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.framework.common.exception.CreateJMXServerException;

/**
 * Default provider of the JBoss MBean Server
 * <p>
 * @author    realMethods, Inc.
 */
public class JBossJMXServerFactory 
	implements IFrameworkJMXServerFactory
{
	/**
	 * Return the JBOSS MBean Server from JNDI
	 * 
	 * @return 		MBeanServer
	 * @exception	CreateJMXServerException
	 */
	public MBeanServer createMBeanServer()
	throws CreateJMXServerException
	{
		if ( mBeanServer == null )
 		{
			try 
			{
				String CONTEXT_PROVIDER_URL = "localhost:1099";
				String CONTEXT_FACTORY_NAME = "org.jnp.interfaces.NamingContextFactory";
				String CONTEXT_FACTORY_PKGS = "org.jboss.naming:org.jnp.interfaces";
				
				Properties properties = new Properties();
				
				properties.put ( "Context.PROVIDER_URL" , CONTEXT_PROVIDER_URL ) ;
				properties.put ( "java.naming.provider.url" , CONTEXT_PROVIDER_URL ) ;
				properties.put ( "java.naming.factory.initial" , CONTEXT_FACTORY_NAME ) ;
				properties.put ( "java.naming.factory.url.pkgs" , CONTEXT_FACTORY_PKGS ) ;

 				Context aJNDIContext = new InitialContext( properties );
 
 				Object server = aJNDIContext.lookup( "ejb/jmx/ejb/Adaptor" );
 				
 			//	RemoteMBeanServer connector = new RMIConnectorImpl ( (RMIAdaptor) this.initialContext.lookup("jmx:server:rmi"));
 				
				if( server != null ) 
				{
	        		if( server instanceof MBeanServer ) 
	        		{
						mBeanServer = (MBeanServer)server; 
	        		} 
/*	        		else if ( server instanceof RemoteMBeanServer )
	        		{
 						throw new CreateJMXServerException( "JBoss JMX Server found in JNDI is of type RemoteMBeanServer" ); 	        			
	        		}
*/	        		
 					else 
				 	{
 						throw new CreateJMXServerException( "JBoss JMX Server found in JNDI is not of type MBeanServer but rather " + server.getClass().getName() ); 
					} 
				}
			}
 			catch( NamingException ne ) 
 			{
 				throw new CreateJMXServerException( ne.getExplanation() );
 
 			}
		}
 					
		return(  mBeanServer );
	}
	
// attributes 
	
   /**
	* singleton instance of the created MBeanServer
	*/
   public static MBeanServer mBeanServer           	= null;
	
}

/*
 * Change Log:
 * $Log: JBossJMXServerFactory.java,v $
 */
