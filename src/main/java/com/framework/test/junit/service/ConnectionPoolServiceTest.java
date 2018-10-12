/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.test.junit.service;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import java.util.Iterator;
import java.util.Map;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.ConnectionAcquisitionException;
import com.framework.common.exception.ConnectionActionException;
import com.framework.common.exception.FrameworkJMSException;

import com.framework.common.message.TaskJMSMessage;

import com.framework.integration.objectpool.ConnectionPoolManagerFactory;
import com.framework.integration.objectpool.IConnectionImpl;
import com.framework.integration.objectpool.IConnectionPool;
import com.framework.integration.objectpool.IConnectionPoolManager;

import com.framework.integration.objectpool.db.DatabaseQuerier;

import com.framework.integration.objectpool.jms.JMSQueueImpl;
import com.framework.integration.objectpool.jms.JMSTopicImpl;

import com.framework.integration.objectpool.ldap.LDAPConnectionImpl;

import com.framework.integration.objectpool.mq.IFrameworkMQAdapter;
import com.framework.integration.objectpool.mq.FrameworkMQAdapter;
import com.framework.integration.objectpool.mq.MQConnectionImpl;

import com.framework.test.junit.FrameworkTestCase;

import com.framework.test.junit.etc.TestJMSMessageListener;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This class tests all of the major functionality of the different components that comprise the Framework
 * Connection Pooling service.  In order to appropriately tests the different types of supported connections,
 * be sure the actual connection exists.  For instance, if you choose to provide your own database entries into the 
 * <i>connectionpool.properties</i> file, be sure the actual connections exist, and the provided property values
 * are correct.  This is true for JMS, LDAP, and MQ usage as well.
 * <p>The following tests are executed :
 * <ul>
 * <li>Creation of a ConnectionPoolManager through the ConnectionPoolManagerFactory.  By doing so, this will
 * cause all relevant IConnectionPools and IConnectionImpls to be created, based on the entries in the
 * <i>connectionpool.properties</i> file.
 * <li>Verify the Collection of ConnectionPools contained by the ConnectionPoolManager are non-null.
 * <li>Iterate through the Collection of ConnectionPools and acquire a single IConnectionImpl.  This will cause
 * the creation and initialization of an IConnectionImpl implementation based on the class name provided for
 * parameter <i>className</i>.
 * <li>If an IConnectionImpl is successfully created and initialized, it's sub-class type is interrogated and
 * specific set of tests are performed.  The supported sub-types are:
 * <br>
 * <ul>
 * 	<li><i>com.framework.common.misc.DatabaseQuerier</i>
 * 	<ul>
 * 		<li>Verify mandatory attributes
 *		<li>To verify the JDBC connection, use its java.sql.DatabaseMetaData to output the table names
 *	</ul>
 * 	<li><i>com.framework.objectpool.JMSQueueImpl<i>
 * 	<ul>
 * 		<li>Verify mandatory attributes
 *		<li>Assign a listener using TestJMSMessageListener
 * 		<li>Send a message
 * 		<li>Send an Object
 * 		<li>Send an Object with a correlation ID
 *	</ul>
 * 	<li><i>com.framework.objectpool.JMSTopicImpl<i>
 * 	<ul>
 * 		<li>Verify mandatory attributes
 *		<li>Assign a listener using TestJMSMessageListener
 * 		<li>Send a message
 * 		<li>Send an Object
 * 		<li>Send an Object with a correlation ID
 *	</ul>
 * 	<li><i>com.framework.objectpool.LDAPConnectionImpl<i>
 * 	<ul>
 * 		<li>Verify mandatory attributes
 *		<li>Call <i>getInitialDirContext()</i> and verify the result is non-null.
 *	</ul>
 * 	<li><i>com.framework.objectpool.MQConnectionImpl<i>
 * 	<ul>
 * 		<li>Verify mandatory attributes
 *		<li>Send a message
 * 		<li>Receive a message
 *	</ul>
 * </ul>
 * <li>Each acquired connection is released by to its ConnectionPool
 * <li>Empty the ConnectionPools on the ConnectionPoolManager, and verify its empty.
 * </ul>
 * <br>
 * 
 * <p>
 * @author    realMethods, Inc.
 */
public class ConnectionPoolServiceTest extends FrameworkTestCase 
{

// constructors
	
    /**
     * construtor
     *
     * @param    testCaseName		unique name applied to this TestCase
     */    
    public ConnectionPoolServiceTest( String testCaseName )
    {
        super( testCaseName );

    }
    
    /**
	 * main test driver
     */
    public static void main(String[] args) 
    {
        TestSuite suite = new TestSuite();
        suite.addTest( new ConnectionPoolServiceTest( "fullTest" ) );

        junit.textui.TestRunner.run( suite );
    }

// TestCase overloads

    /**
     * Overload from JUnit TestCase, providing the method name to invoke for testing.  
     * The other methods can be invoked on their own, but fullTest covers all the bases.
     *
     * @return    junit.framework.Test
     */
    public static Test suite() 
    { 
        TestSuite suite= new TestSuite(); 
        suite.addTest(new ConnectionPoolServiceTest("fullTest"));
        return suite; 
    }
    
// test methods

    /** 
     * full test of the component
     */
    public void fullTest()
    throws Throwable
    {    		
		new FrameworkBaseObject().logMessage( "*****\nConnectionPoolServiceTest:fullTest() - starting...\n*****" ); 
		   	
		// this single test will not only exercise funtionality on the ConnectionPoolManagerFactory
		// and a ConnectionPoolManager, but will also do so on the ConnectionPools and ConnectionImpls
		// which resulted from innitializing the ConnectionPoolManager with the contents of the
		// connectionpool.properties, whose location was provided as a -DFRAMEWORK_HOME Java
		// system property.
		testConnectionPoolManager();
		
		new FrameworkBaseObject().logMessage( "*****\nConnectionPoolServiceTest:fullTest() - passed...*****" );			
    }    
        
    /**
     * Tests the aspects of the ConnectionPoolManager
     * <p>
     * @throws Throwable
     */
    public void testConnectionPoolManager()
    throws Throwable 
    {
    	IConnectionPoolManager manager = null;
    	
    	try
    	{
    		manager = ConnectionPoolManagerFactory.getObject();
    		
    		assertNotNull( manager );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnectionPoolManager() failed on ConnectionPoolManagerFactory.getObject() - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	// get the Map of ConnectionPools
    	Map pools = manager.getConnectionPools();
    	
    	try
    	{
    		assertNotNull( pools );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnectionPoolManager() failed due to a null Map of pools returned by the ConnectionPoolManager - " + exc.toString() );
            throw exc;    		
    	}
    	
    	// iterate through the keys for the ConnectionPools and exercise functionality on the
    	// ConnectionPoolManager    
		Iterator keys 				= pools.keySet().iterator();
		IConnectionPool pool 		= null;
		IConnectionImpl connection 	= null;
		    	    	
		while ( keys.hasNext() )
		{
			pool = (IConnectionPool)pools.get( keys.next() );
			
	    	try
    		{
    			assertNotNull( pool );    		
	    	}
    	   	catch( Throwable exc )
       		{
				new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnectionPoolManager() failed dues to a null ConnectionPool found in the Map of ConnectionPools created and owned by the ConnectionPoolManager - " + exc.toString() );
	            throw exc;    		
    		}			
    		
    		// get a connection from the manager
    		try
    		{
	    		connection = manager.getConnection( pool.getPoolName() );
	    		assertNotNull( connection );
	    		
	    		// move on to test the connection, but don't stop if it fails
	    		try
	    		{
		    		testConnection( connection );
	    		}
	    		catch( Throwable exc )
	    		{
	    			throw exc;
	    		}
    		}
    		catch( ConnectionAcquisitionException caExc )
    		{
				new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnectionPoolManager() failed - " + caExc.toString() );
	            throw caExc;    			
    		}
    		catch( AssertionFailedError error )
    		{
				new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnectionPoolManager() failed on null test of ConnectionManager.getConnection(String) call - " + error.toString() );
	            throw error;     			
    		}
    		    		
    		// now safe to test the pool itself, since in the testing
    		// it may affect one or more contained connections
    		try
    		{
    			testConnectionPool( pool );    		    		
    		}
    		catch( Throwable exc )
    		{
    			throw exc;
    		}    		
		}
			
		// finally empty the ConnectionPoolMannager of all pools, which will cause each
		// ConnectionPool to empty itself of all IConnectionImpls
		manager.emptyPools();
		
		// verify they are empty
		try
		{
    		assertTrue( manager.getConnectionPools().isEmpty() );
		}
		catch( Throwable exc )
		{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnectionPoolManager() failed due to a call to ConnectionPoolManager.emptyPools() not empmtying the Map of IConnectionPools - " + exc.toString() );
            throw exc;    			
		}
    						    	    	
    	new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnectionPoolManager() - passed..." );      		
    }
        
    
    /**
     * Tests ConnectionPool
     * <p>
     * @throws Throwable
     */
    public void testConnectionPool( IConnectionPool pool )
    throws Throwable 
    {
    	try
    	{
    		assertNotNull( pool );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnectionPool() failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	// test presence of mandatory attributes
    	String varName 	= null;
    	String poolName	= pool.getPoolName();
    	
		try
		{
			varName = "poolName";
			assertNotNull( poolName );
			
			varName = "connectionClassName";
			assertNotNull( pool.getConnectionClassName() );
			
			varName = "properties";
			assertNotNull( pool.getProperties() );
			
			varName = "connections";
			assertNotNull( pool.getConnections() );						
		}
		catch( Throwable exc )
		{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnectionPool() for pool " + poolName + " failed due to invalid state for member variable ConnectionPool." + varName + " - " + exc.toString() );
            throw exc; 			
		}
			     	
    	// acquire a connection and release it back through the connection pool manager
    	IConnectionImpl connection = null;
    	
    	try
    	{
    		connection = pool.getConnection();
    		assertNotNull( connection );
    		connection.releaseToConnectionPoolManager();
    	}
    	catch( AssertionFailedError error )
    	{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnectionPool() for pool " + poolName + " failed due to a null IConnectionImpl returned from ConnecionPool.getConnection() - " + error.toString() );
            throw error;    		
    	}
    	catch( ConnectionActionException caExc )
    	{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnectionPool() for pool " + poolName + " failed during call to ConnectionImpl().releaseToConnectionPoolManager() - " + caExc.toString() );
            throw caExc;    		
    	}
    	catch( ConnectionAcquisitionException caqExc )
    	{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnectionPool() for pool " + poolName + " failed to ConnectionPool.getConnection() - " + caqExc.toString() );
            throw caqExc;    		    		
    	}
    	catch( Throwable exc )
    	{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnectionPool() for pool " + poolName + " failed - " + exc.toString() );
            throw exc;    		
    	}
    	
    	new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnectionPool() for pool " + poolName + " - passed..." );      		
    }
    
    /**
     * Tests Connection
     * <p>
     * @throws Throwable
     */
    public void testConnection( IConnectionImpl connection )
    throws Throwable 
    {
    	try
    	{
    		assertNotNull( connection );    		
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnection failed - " + exc.toString() );
            throw exc;    		
    	}    
    	
    	String poolName = connection.getPoolName();
    	
    	// verify the mandatory attributes   
    	String varName = null;
    	try
    	{
    		varName =  "poolName";
    		assertNotNull( poolName );    	
    		
    		varName = "properties";
    		assertNotNull( connection.getProperties() );	
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnection() for connection " + poolName + " failed due to invalid state for member variable ConnectionImpl." + varName + " - " + exc.toString() );
            throw exc;    		
    	}    	 		

    	try
    	{
			// verify inuse flag is set
			assertTrue( connection.getInUse().booleanValue() );
    	}
       	catch( Throwable exc )
       	{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnection() for connection " + poolName + " failed due to ConnectionImpl.inUse flag being set to false when it should be set to true. - " + exc.toString() );
            throw exc;    		
    	}  
    					
		// check the type and test accordingly
		if ( connection instanceof com.framework.integration.objectpool.db.DatabaseQuerier )
			testDatabaseConnection( (DatabaseQuerier)connection );
		else if ( connection instanceof com.framework.integration.objectpool.jms.JMSQueueImpl )
			testJMSQueueConnection( (JMSQueueImpl)connection );
		else if ( connection instanceof com.framework.integration.objectpool.jms.JMSTopicImpl )
			testJMSTopicConnection( (JMSTopicImpl)connection );			
		else if ( connection instanceof com.framework.integration.objectpool.ldap.LDAPConnectionImpl )
			testLDAPConnection( (LDAPConnectionImpl)connection );			
		else if ( connection instanceof com.framework.integration.objectpool.mq.MQConnectionImpl )
			testMQConnection( (MQConnectionImpl)connection );

			
		        	
		// finally release the connection back to its owning pool
		try
		{
			connection.releaseToConnectionPoolManager();
		}
		catch( Throwable exc )
		{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnection failed - " + exc.toString() );
            throw exc; 			
		}
				        	
    	new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testConnection() for connection " + poolName + " - passed..." );      		
    }  
    
    /**
     * Tests DatabaseQuerier types
     */
    public void testDatabaseConnection( DatabaseQuerier connection )
    throws Throwable
    {              
    	String varName		= null;
    	String poolName		= connection.getPoolName();
    	
    	// validate the state of mandatory member variables    
    	try
    	{
    		varName = "getDatabaseDriverName";
    		assertNotNull( connection.getDatabaseDriverName() );
    		
    		varName  = "getDatabaseURL";
    		assertNotNull( connection.getDatabaseURL() ); 

// get safe connection actually acquires the connection from the jdbc source...

			connection.getSafeConnection();  		
			
    		varName  = "getConnection";    		
    		assertNotNull( connection.getConnection() );    
    		
    		varName  = "verifyConnection";
    		assertTrue( connection.verifyConnection() ); 
    		
    		varName  = "getUserID";
    		assertNotNull( connection.getUserID() ); 
    		
    		varName  = "getPassword";
    		assertNotNull( connection.getPassword() );    		
    		
    		 		
    	}
    	catch( Throwable exc )
    	{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testDatabaseConnection() for connection " + poolName + " failed due to invalid state for member variable ConnectionImpl." + varName + " - " + exc.toString() );	    		
			throw exc;
    	}
    	
    	// test the underlying JDBC Connection...
    	StringBuffer tableNames =  new StringBuffer( "Table name(s) : ");
    	try
    	{
	        // Gets the database metadata
    	    DatabaseMetaData dbmd = connection.getConnection().getMetaData();
    
	        // Specify the type of object; in this case we want tables
    	    String[] types = {"TABLE"};
        	ResultSet resultSet = dbmd.getTables(null, null, "%", types);
    
	        // Verify the existence of table name, and retrieve them
	        assertTrue( resultSet.first() );
	        
    	    while ( resultSet.next() ) 
    	    {
    	    	tableNames.append( resultSet.getString(3) );
    	    	tableNames.append( " ");
    	    }
    	        		
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testDatabaseConnection() for connection " + poolName + " - in validating the JDBC connection, discovered the following tables - " + tableNames.toString() );	    		
	    	    	    	 		
    	}
    	catch( Throwable exc )
    	{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testDatabaseConnection() for connection " + poolName + " failed because the underlying JDBC connection could did not return any table names  - " + exc.toString() );	    		
			throw exc;
    	}    
    	
		new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testDatabaseConnection() for connection " + poolName + " - passed..." );      					
		    		
    }
    
    /**
     * Tests JMSQueueImpl
     */
    public void testJMSQueueConnection( JMSQueueImpl connection )
    throws Throwable
    {  
    	String varName		= null;
    	String poolName		= connection.getPoolName();
    	
    	// validate the state of mandatory member variables    
    	try
    	{
    		varName = "jmsName";
    		assertNotNull( connection.getJMSName() );
    		
    		varName  = "queueConnectionFactory";
    		assertNotNull( connection.getJMSFactory() ); 
    	}
    	catch( Throwable exc )
    	{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testJMSQueueConnection() for connection " + poolName + " failed due to invalid state for member variable ConnectionImpl." + varName + " - " + exc.toString() );	    		
			throw exc;
    	}	     

		// mandatory funtionality:
		// assign as a listener
		TestJMSMessageListener listener = new TestJMSMessageListener();
		
		try
		{
			connection.assignAsListener( listener );
		}
		catch( FrameworkJMSException exc )
		{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.ConnectionPoolServiceTest.testJMSQueueConnection() for connection " + poolName + " - call to JMSQueueImpl.assignAsListener() failed " + exc.toString() );	    					
			throw exc;
		}    	       
		
		// mandatory funtionality:		
		// send a simple message
		try
		{
			connection.sendMessage( "ConnectionPoolServiceTest:testMessage" );
		}
		catch( FrameworkJMSException exc )
		{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testJMSQueueConnection() for connection " + poolName + " - call to JMSQueueImpl.sendMessage(String) failed " + exc.toString() );	    					
			throw exc;
		} 
			
		// mandatory funtionality:		  	
		// send a Serializable object with a correlation id
		try
		{			
			connection.sendObject( new TaskJMSMessage( "ConnectionPoolServiceTest:testMessage" ), CORRELATION_ID );
		}
		catch( FrameworkJMSException exc )
		{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testJMSQueueConnection() for connection " + poolName + " - call to JMSQueueImpl.sendObject(Serializable,String) failed " + exc.toString() );	    					
			throw  exc;
		} 
			
		// optional functionality :
		// send/receive a Serializable object with a correlation id...may not work if
		// the JMS configuration isn't set up to handle this...
/*		try
		{			
			connection.sendObjectandReceive( new TaskJMSMessage( "test" ), CORRELATION_ID, 2000 );
		}
		catch( FrameworkJMSException exc )
		{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testJMSQueueConnection() for connection " + poolName + " - call to JMSQueueImpl.sendObject(Serializable,String) failed " + exc.toString() );	    					
			throw exc;
		} 					
*/			
		new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testJMSQueueConnection() for connection " + poolName + " - passed..." );      					
						
    }    
    
    /**
     * Tests JMSTopicImpl 
     */
    public void testJMSTopicConnection( JMSTopicImpl connection )
    throws Throwable    
    {  
    	String varName		= null;
    	String poolName		= connection.getPoolName();
    	
    	// validate the state of mandatory member variables    
    	try
    	{
    		varName = "jmsName";
    		assertNotNull( connection.getJMSName() );
    		
    		varName  = "queueConnectionFactory";
    		assertNotNull( connection.getJMSFactory() );
    	}
    	catch( Throwable exc )
    	{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testJMSTopicConnection() for connection " + poolName + " failed due to invalid state for member variable JMSTopicImpl." + varName + " - " + exc.toString() );
			throw exc;
    	}	     

		// mandatory funtionality:
		// assign as a listener
		TestJMSMessageListener listener = new TestJMSMessageListener();
		
		try
		{
			connection.assignAsListener( listener );
		}
		catch( FrameworkJMSException exc )
		{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testJMSTopicConnection() for connection " + poolName + " - call to JMSTopicImpl.assignAsListener() failed " + exc.toString() );	    					
			throw exc;
		}    	       
		
		// mandatory funtionality:		
		// send a simple message
		try
		{
			connection.sendMessage( "ConnectionPoolServiceTest:testMessage" );
		}
		catch( FrameworkJMSException exc )
		{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testJMSTopicConnection() for connection " + poolName + " - call to JMSTopicImpl.sendMessage(String) failed " + exc.toString() );	    					
			throw exc;
		} 
			
		// mandatory funtionality:			
		// send a Serializable object with a correlation id
		try
		{			
			connection.sendObject( new TaskJMSMessage( "ConnectionPoolServiceTest:testMessage" ), CORRELATION_ID );
		}
		catch( FrameworkJMSException exc )
		{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testJMSTopicConnection() for connection " + poolName + " - call to JMSTopicImpl.sendObject(Serializable,String) failed " + exc.toString() );	    					
			throw exc;
		} 
			
		// optional functionality :
		// send/receive a Serializable object with a correlation id...may not work if
		// the JMS configuration isn't set up to handle this...
/*		try
		{			
			connection.sendObjectandReceive( new TaskJMSMessage( "test" ), CORRELATION_ID, 2000 );
		}
		catch( FrameworkJMSException exc )
		{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testJMSTopicConnection() for connection " + poolName + " - call to JMSTopicImpl.sendObject(Serializable,String) failed " + exc.toString() );	    					
			throw exc;
		} 					
*/
    	new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testJMSTopicConnection() for connection " + poolName + " - passed..." );      					
				
    } 
        
    /**
     * Tests LDAPConnectionImpl
     */
    public void testLDAPConnection( LDAPConnectionImpl connection )
    throws Throwable    
    {    
    	String varName		= null;
    	String poolName		= connection.getPoolName();
    	
    	// validate the state of mandatory member variables    
    	try
    	{    		
    		varName = "getLDAPUserKey()";
    		assertNotNull( connection.getLDAPUserKey() );
    		
    		varName  = "getLDAPPasswordKey()";
    		assertNotNull( connection.getLDAPPasswordKey() );

    		varName = "getLDAPRoleKey()";
    		assertNotNull( connection.getLDAPRoleKey() );
    		
    		varName  = "getLDAPRootDomainName()";
    		assertNotNull( connection.getLDAPRootDomainName() );
    		    		    		
    	}
    	catch( Throwable exc )
    	{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testLDAPConnection() for connection " + poolName + " failed due to invalid state returned from access method LDAPConnectionImpl." + varName + " - " + exc.toString() );
			throw exc;
    	}
    	
    	// attempt to get an InitialDirContext, which is an attempt to connect
    	// to the LDAP directory system in use
    	try
    	{    		
    		assertNotNull( connection.getInitialDirContext() );
    	}
    	catch( Throwable exc )
    	{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testLDAPConnection() for connection " + poolName + " failed due to null InitialDirContext returned from getInitialDirContext.  Verify all property values within the connectionpool.properties file. - " + exc.toString() );
			throw exc;
    	}    		
    	
	    new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testLDAPConnection() for connection " + poolName + " - passed..." );      					
		    		 	          
    }
    
    /**
     * Tests MQ type Connection
     */
    public void testMQConnection( MQConnectionImpl connection )
    throws Throwable    
    {    
    	String varName		= null;
    	String poolName		= connection.getPoolName();
   	
    	// validate the state of mandatory member variables    
    	try
    	{    		    		
    		varName  = "getQueueManager()";
    		assertNotNull( connection.getQueueManager() );

    		varName = "getQueueName()";
    		assertNotNull( connection.getQueueName() );
    		
    		varName  = "getQueueManagerName()";
    		assertNotNull( connection.getQueueManagerName() );
    		    		
    		varName = "getEnvironment()";
    		assertNotNull( connection.getEnvironment() );  
    		
    		varName  = "getHostName()";
    		assertNotNull( connection.getHostName() );

    		varName = "getChannel()";
    		assertNotNull( new Integer( connection.getChannel() ) );
    		
    		varName  = "getQueueManagerName()";
    		assertNotNull( connection.getQueueManagerName() );
    		    		
    		varName = "getExpiry()";
    		assertNotNull( new Integer( connection.getExpiry() ) ); 	
    		
    		varName = "getTheFrameworkMQQueue()";
    		assertNotNull( connection.getTheFrameworkMQQueue() ); 	
    		  		
    	}
    	catch( Throwable exc )
    	{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testMQConnection() for connection " + poolName + " failed due to invalid state returned from access method LDAPConnecctionImpl." + varName + " - " + exc.toString() );
			throw exc;
    	}
    	
    	// attempt to send on the queue...
    	try
    	{    		
    		connection.send( new FrameworkMQAdapter( "Test MQ Message" ) );
    	}
    	catch( Throwable exc )
    	{    		
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testMQConnection() for connection " + poolName + " failed due to null InitialDirContext returned from getInitialDirContext.  Verify all property values within the connectionpool.properties file. - " + exc.toString() );
			throw exc;
    	}    		
    	
    	// attempt to send on the queue...
    	try
    	{    		
    		IFrameworkMQAdapter adapter = connection.receive();
    		assertNotNull( adapter );
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testMQConnection() - for connection " + poolName + ", received the following - " + adapter.getReceviedDataAsString() );	    		
    	    		
    	}
    	catch( Throwable exc )
    	{
			new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testMQConnection() for connection " + poolName + " failed due to null InitialDirContext returned from getInitialDirContext.  Verify all property values within the connectionpool.properties file. - " + exc.toString() );	    		
			throw exc;
    	} 
    	    	
    	new FrameworkBaseObject().logMessage( "ConnectionPoolServiceTest.testMQConnection() for connection " + poolName + " - passed..." );      					
   		 	           
    }       
        
// attributes

	static public final String CORRELATION_ID	= "1000";

}
    
/*
 * Change Log:
 * $Log: ConnectionPoolServiceTest.java,v $
 * Revision 1.2  2003/10/07 22:11:51  tylertravis
 * No longer test sendObjectandReceive on JMSTopicImmpl and JMSQueueImpl
 *
 * Revision 1.1.1.1  2003/09/15 01:16:08  tylertravis
 * initial check-in
 *
 */    
