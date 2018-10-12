/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.objectpool.mq;

import java.util.Map;

import javax.management.*;

import com.framework.common.exception.ConnectionAcquisitionException;
import com.framework.common.exception.ConnectionActionException;
import com.framework.common.exception.FrameworkMQBaseException;
import com.framework.common.exception.InitializationException;
import com.framework.common.exception.ObjectCreationException;

import com.framework.common.message.IFrameworkMQMessage;

import com.framework.common.namespace.FrameworkNameSpace;

import com.framework.integration.log.FrameworkLogHandlerFactory;
import com.framework.integration.log.IFrameworkLogHandler;

import com.framework.integration.objectpool.ConnectionImpl;
import com.framework.integration.objectpool.ConnectionPoolManagerFactory;

import com.ibm.mq.MQC;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;

/**
 * Base class for making an MQ related connection 
 * and performing related operations.
 *
 * <p>
 * @author    realMethods, Inc.
 */
public class MQConnectionImpl extends ConnectionImpl
    implements IMQConnectionImpl
{ 
    
// constructors
    public MQConnectionImpl()
    {}
    
    //****************************************************
    // Public Methods
    //****************************************************

    
// ConnectionImpl overloads

    /**
     * initialize the creation and connection of the queue and associated queue manager
     *
     * @param      	name
     * @param     	props
     * @exception	IllegalArgumentException
     * @exception	InitializationException
     */
    public void initialize( String name, Map props )
    throws IllegalArgumentException, InitializationException
    {
        super.initialize( name, props );

        try
        {
            initMQ();
        }
        catch ( Throwable exc )
        {
            throw new InitializationException( "MQConnectionImpl:initialize() - " + exc, exc );
        }
    }    

    /**
     * client notification that a transaction is about to begin.
     * 
     * @exception	ConnectionActionException
     */
    public void startTransaction()
    throws ConnectionActionException
    {
    }

    /**
     * client notification to commit the current transaction.
     * 
     * @exception	ConnectionActionException
     */
    public void commit()
    throws ConnectionActionException
    {
        try
        {
            getQueueManager().commit();
        }
        catch ( MQException exc )
        {
            String sMsg = "MQConnectionImpl:commit() - MQException with reasonCode: " + exc.reasonCode;
            
            this.incrementPrimaryFailureCount();
            
            logError( sMsg );
            throw new ConnectionActionException( sMsg, exc );                                                
        }
        catch ( Throwable exc )
        {
            String sMsg = "MQConnectionImpl.commit(): - " + exc;
            
            logError( sMsg );
            throw new ConnectionActionException( sMsg, exc );                        
        }
    }

    /**
     * client notification to rollback the current transaction.
     * 
     * @exception	ConnectionActionException
     */
    public void rollback()
    throws ConnectionActionException
    {
        try
        {
            getQueueManager().backout();
        }
        catch ( Throwable exc )
        {
            String sMsg = "MQConnectionImpl:rollBack() - during MQ Queue Manager backout - " + exc;
                        
            incrementPrimaryFailureCount();
            
            logError( sMsg );
            
            throw new ConnectionActionException( sMsg, exc );                     
        }
    }

    /**
     * Disconnects the underlying connection.  
     * 
     * @exception	ConnectionActionException
     */
    public void disconnect()
    throws ConnectionActionException
    {
        try
        {
            getQueueManager().commit();
            getQueueManager().disconnect();

            // assign to null, since creation is the only way to connect...
            theFrameworkMQQueueManager = null;

            // close all the queues
            closeAllQueues();
        }
        catch ( MQException exc )
        {
            String sMsg = "MQConnectionImpl:disconnect() - MQException with reasonCode - " + exc.reasonCode;
            
            incrementPrimaryFailureCount();
            
            logError( sMsg );
            throw new ConnectionActionException( sMsg, exc );                        
        }
        catch ( Throwable exc )
        {
            String sMsg = "MQConnectionImpl:disconnect() - " + exc.toString();
            
            incrementPrimaryFailureCount();
            
            logError( sMsg );
            throw new ConnectionActionException( sMsg, exc );                        
        }
        finally
        {
        	super.disconnect();
        	backUpQueue.releaseToConnectionPoolManager();        	
        }
    }

    /**
     * called by the pool manager to notify the connection it is 
     * being released.
     * 
     * @exception	ConnectionActionException
     */
    public void connectionBeingReleased()
    throws ConnectionActionException
    {
        if ( backUpQueue != null )
        {
        	try
        	{
            	ConnectionPoolManagerFactory.getObject().releaseConnection( backUpQueue );
        	}
        	catch( Throwable exc )
        	{
        		throw new ConnectionActionException( "MQConnectionImpl:connectionBeingReleased()  - " + exc, exc );
        	}
        }
        
    }

// ConnectionPoolImpl overloads - from ConnectionPoolMBean

    /**
     * Allows the value of the specified attribute of the Dynamic MBean to be obtained.
     *
     * @param       name
     * @return      Object
     * @exception   AttributeNotFoundException
     * @exception   MBeanException
     * @exception   ReflectionException 
     */
    public Object getAttribute( String name ) 
	throws AttributeNotFoundException, MBeanException, ReflectionException 
	{
	    Object attribute = null;
	    	    
	    if ( name.equals( QUEUE_NAME ) )
	    {	        
	        attribute = this.getQueueName();
	    }
	    else if ( name.equals( QUEUE_MANAGER_NAME ) )
	    {
	        attribute = this.getQueueManagerName();
	    }
	    else if ( name.equals( ERROR_LOG_HANDLER_NAME ) )
	    {
	        attribute = this.getErrorLogHandlerName();
	    }
	    else if ( name.equals( BACKUP_MQ_POOL_NAME ) )
	    {
	        attribute = this.getBackupMQPoolName();
	    }
	    else if ( name.equals( HOSTNAME ) )
	    {
	        attribute = this.getHostName();
	    }
	    else if ( name.equals( PORT ) )
	    {
	        attribute = new Integer( this.getPort() );
	    }
	    else if ( name.equals( CHANNEL ) )
	    {
	        attribute = this.getChannel();
	    }
	    else if ( name.equals( WAIT_TIME ) )
	    {
	        attribute = new Integer( this.getWaitTime() );
	    }	    
	    else if ( name.equals( EXPIRY ) )
	    {
	        attribute = new Integer( this.getExpiry() );
	    }
	    else if ( name.equals( TOTAL_PRIMARY_FAILURES ) )
	    {
	        attribute = new Integer( this.getTotalPrimaryFailures() );
	    }	    	    
	    else
	    {
    	    // If attribute_name has not been recognized delegate to the subclass
	        attribute = super.getAttribute( name );
	    }
	    
	    return( attribute );
    }

    /**
     * Sets the value of the specified attribute of the Dynamic MBean.
     * 
     * @param       attribute 
     * @exception   AttributeNotFoundException
     * @exception   InvalidAttributeValueException,
	 * @exception   MBeanException
	 * @exception   ReflectionException 
     */
    public void setAttribute( Attribute attribute ) 
	throws AttributeNotFoundException, InvalidAttributeValueException,
	       MBeanException, ReflectionException 
	{

	    // Check attribute is not null to avoid NullPointerException later on
	    if (attribute == null) 
	    {
	        throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"), 
			    			 "Cannot invoke a setter of " + getClassName() + " with null attribute");
	    }
	    
	    String name = attribute.getName();
//	    Object value = attribute.getValue();

	    if (name == null) 
	    {
	        throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), 
						 "Cannot invoke the setter of " + getClassName() + " with null attribute name");
	    }
	    
        // Since none of the parameters are settable here, delegate to the parent
	    {
	        super.setAttribute( attribute );
		}
    }

    /**
     * Returns the description of this MBean
     * @return      String      
     */
    protected String getMBeanDescription()
    {
        return( "MQ Connection Implementation MBean" );
    }
    
    /**
     * Returns all the attributes for this class as an array of MBeanAttributeInfos.
     *
     * @return MBeanAttributeInfo[]
     */
    protected MBeanAttributeInfo[] getMBeanAttributes()
    {
        MBeanAttributeInfo[] mbAttributeInfo = new MBeanAttributeInfo[11 ];    

        mbAttributeInfo[0] = new MBeanAttributeInfo( QUEUE_NAME, "java.lang.String", "UserKey: The key used to access the LDAP user attribute.", true, false, false );
        mbAttributeInfo[1] = new MBeanAttributeInfo( QUEUE_MANAGER_NAME, "java.lang.String", "PasswordKey: The key used to access the LDAP password attribute.", true, false, false );
        mbAttributeInfo[2] = new MBeanAttributeInfo( ERROR_LOG_HANDLER_NAME, "java.lang.String", "RoleKey: The key used to access the LDAP attribute designated as the user role.", true, false, false );
        mbAttributeInfo[3] = new MBeanAttributeInfo( BACKUP_MQ_POOL_NAME, "java.lang.String", "RootDomain: The initial domain in the LDAP hierarchy of interest.", true, false, false );
        mbAttributeInfo[4] = new MBeanAttributeInfo( ENV, "java.lang.String", "UsePasswordEncryption: T/F indicator for password encryption in use within LDAP.", true, false, false );
        mbAttributeInfo[5] = new MBeanAttributeInfo( HOSTNAME, "java.lang.String", "UserKey: The key used to access the LDAP user attribute.", true, false, false );
        mbAttributeInfo[6] = new MBeanAttributeInfo( PORT, "java.lang.String", "PasswordKey: The key used to access the LDAP password attribute.", true, false, false );
        mbAttributeInfo[7] = new MBeanAttributeInfo( CHANNEL, "java.lang.String", "RoleKey: The key used to access the LDAP attribute designated as the user role.", true, false, false );
        mbAttributeInfo[8] = new MBeanAttributeInfo( WAIT_TIME, "java.lang.String", "RootDomain: The initial domain in the LDAP hierarchy of interest.", true, false, false );
        mbAttributeInfo[9] = new MBeanAttributeInfo( EXPIRY, "java.lang.String", "UsePasswordEncryption: T/F indicator for password encryption in use within LDAP.", true, false, false );
        mbAttributeInfo[10] = new MBeanAttributeInfo( TOTAL_PRIMARY_FAILURES, "java.lang.String", "UsePasswordEncryption: T/F indicator for password encryption in use within LDAP.", true, false, false );
        
        return( combineAttributeInfos( super.getMBeanAttributes(), mbAttributeInfo ) );
    }
    

// IMQConnectionImpl implementations

    /**
     * Returns the underlying IFrameworkMQQueue
     *
     * @return IFrameworkMQQueue
     */
    public IFrameworkMQQueue getTheFrameworkMQQueue()
    {
        return( theFrameworkMQQueue );
    }
    
    /**
     * Send an IFrameworkMQMessage, supplied by the IFrameworkMQAdapater implementor, onto the
     * corresponding IFrameworkMQQueue.
     * <p>
     * @param       adapter
     * @exception   FrameworkMQBaseException
     */
    public void send( IFrameworkMQAdapter adapter ) 
    throws FrameworkMQBaseException
    {
        
        try
        {
            // first step is to validate the adapter;
            validateMQAdapter( adapter );

            // get the queue from the queue manager
            theFrameworkMQQueue = FrameworkMQQueue.getInstance( getQueueName(), this, true /*send mode*/ );        
            
            IFrameworkMQMessage mqMessage = adapter.provideDataToSend();
            
            // apply a correlationID, if provided externally
            mqMessage = applyCorrelationID( mqMessage );        

            // assign the expiry value
            mqMessage.getMQMessage().expiry = getExpiry();
            
            // initially, try to send it locally
            if ( theFrameworkMQQueueManager != null && useThisPrimaryConnection() )
            {
                theFrameworkMQQueue.put( mqMessage );
                return;
            }
        }
        catch( Throwable exc )
        {            
            incrementPrimaryFailureCount();
            logError( "MQConnectionImpl:send( IFrameworkMQAdapter )" + exc );
        }
        
        // If we are here, either the underlying MQ Manager is unaccessible,
        // or the put on the IBM MQ Queue failed
        
        // Try to send it to the backup, but first assign the correlation ID
        IMQConnectionImpl backup = null;
        
        try
        {
        	backup = getBackupQueue();
        }
        catch( ConnectionAcquisitionException exc )
        {
        	throw new FrameworkMQBaseException( "MQConnectionImpl.send(IFrameworkMQAdapter) - " + exc, exc );
        }
        
        if ( backup != null )
        {
            backup.send( adapter );
        }
    }

    /**
     * Receive a message from the queue into an FrameworkMQAdapter, and wait
     * for an unlimited amount of time.
     *
     * @return      IFrameworkMQAdapter
     * @exception   FrameworkMQBaseException
     */
    public IFrameworkMQAdapter receive() 
    throws FrameworkMQBaseException
    {
        return( receive( this.getWaitTime() ) );
    }

    /**
     * Receive a message from the queue into the specifed adapter, and wait
     * for the specified amount of time.  0 implies wait indefinitely.
     * <p>
     * @param       wait_interval_in_seconds
     * @return      IFrameworkMQAdapter
     * @exception   FrameworkMQBaseException
     */
    public IFrameworkMQAdapter receive( int wait_interval_in_seconds ) 
    throws FrameworkMQBaseException
    {
        
        IFrameworkMQAdapter mqAdapter   = new FrameworkMQAdapter( "" );
        boolean bReceivedSuccess        = false;
        
        try
        {
            
            // get the queue from the queue manager
            theFrameworkMQQueue = FrameworkMQQueue.getInstance( getQueueName(), this, false /*not send mode*/ );        

            if ( theFrameworkMQQueue != null && useThisPrimaryConnection() )
            {
                // determine wait interval
                if ( wait_interval_in_seconds > 0 )
                {
                    theFrameworkMQQueue.getGetMessageOptions().waitInterval = wait_interval_in_seconds;
                }
                else
                {
                    theFrameworkMQQueue.getGetMessageOptions().waitInterval = MQC.MQWI_UNLIMITED;
                }
     
                String sMessage = null;

                if ( correlationID != null )
                {
                    // establish standard options
                    theFrameworkMQQueue.getGetMessageOptions().matchOptions = MQC.MQMO_MATCH_CORREL_ID;                    
                }

                do
                {
                    sMessage = theFrameworkMQQueue.get( applyCorrelationID( mqAdapter.provideDataToSend() ) );

                    if ( sMessage != null )
                    {
                        mqAdapter.receiveData( sMessage );
                    }
                }
                while( mqAdapter.isAbleToReceiveMoreData( sMessage ) == true );
                
                bReceivedSuccess = true;
                theFrameworkMQQueue.getGetMessageOptions().matchOptions = MQC.MQMO_NONE;
            }

        }
        catch ( Throwable exc )
        {
            logError( "MQConnectionImpl:receive(int) - " + exc.toString() );
            bReceivedSuccess = false;
        }
        
        // if the receive didn't go well, for whatever reason, try the backup
        if ( bReceivedSuccess == false )
        {
            incrementPrimaryFailureCount();
            
	        IMQConnectionImpl backup = null;            
	        
		    try
		    {
		    	backup = getBackupQueue();
		    }
		    catch( ConnectionAcquisitionException exc )
		    {
		    	throw new FrameworkMQBaseException( "MQConnectionImpl.receive(int) - " + exc, exc );
		    }            
        
            if ( backup != null )
            {
                logError( "MQConnectionImpl:receive(int) - Failed on main MQ Queue " + this.getQueueName() + " Receiving on Backup Queue from Pool " + this.getBackupMQPoolName() );
                
                try
                {
                    mqAdapter = backup.receive( wait_interval_in_seconds );
                }
                catch( Throwable exc )
                {
                	throw new FrameworkMQBaseException( "MQConnectionImpl:receive(int) - " + exc, exc );
                }
            }
        }
        
        return( mqAdapter );
    }

    /**
     * Calls send and then received asynchronously.
     * <p>
     * @param  		sendAdapter   
     * @return  	IFrameworkMQAdapter
     * @exception 	FrameworkMQBaseException
     */
    public IFrameworkMQAdapter sendReceive( IFrameworkMQAdapter sendAdapter ) 
    throws FrameworkMQBaseException
    {
        // first step is to validate the adapter;
        validateMQAdapter( sendAdapter );

        // send on the queue
        send( sendAdapter );

        // received on the queue...indefinitely
        return( receive() );
    }

// accessor methods

    /**
     * Indicates if internally, this MQConnectionImpl is still acknowledged.
     * If false, it is no longer considered, in favor of the associated 
     * back up connection.
     *
     * @return      boolean
     */
    public boolean isPrimaryInUse()
    {
        return( primaryInUse );
    }
        
    /**
     * Determines the queue name.
     * @return      String
     */
    public String getQueueName()
    {
        // Give the user a chance to specify it as part of the properties file.
        // If not specified, default...
        String s = (String)getProperties().get( QUEUE_NAME );

        return( s );            
    }

    /**
     * Returns the name of the Queue manager.
     * @return       String      
     */
    public String getQueueManagerName()
    {
        String s = (String)getProperties().get( QUEUE_MANAGER_NAME );        

        return( s );
    }

    /**
     * Returns the wait time.
     * @return       String      
     */
    public int getWaitTime()
    {
        String s = (String)getProperties().get( WAIT_TIME );        
        int waitTime = 0;
        
        if ( s != null && s.length() > 0 )
        {
            try
            {
                waitTime = new Integer(s).intValue();
            }
            catch( NumberFormatException nfExc )
            {
                try
                {
                    logError( "MQConnectionImpl:getWaitTime() - Wait time parameter of " + s + " is invalid." );
                }
                catch( Exception exc )
                {
                }
            }
        }
        
        return( waitTime );
    }


    /**
     * Returns the expiry value.
     * @return       String      
     */
    public int getExpiry()
    {
        String s = (String)getProperties().get( EXPIRY );        
        int expiry = -1;
        
        if ( s != null && s.length() > 0 )
        {
            try
            {
                expiry = new Integer(s).intValue();
            }
            catch( NumberFormatException nfExc )
            {
                try
                {
                    logError( "MQConnectionImpl:getWaitTime() - Expiry parameter of " + s + " is invalid." );
                }
                catch( Exception exc )
                {}
            }
        }
        
        return( expiry );
    }

    /**
     * Returns the name of the backup connection MQ name.
     * @return       String      
     */
    public String getBackupMQPoolName()
    {
        String s = (String)getProperties().get( BACKUP_MQ_POOL_NAME );        

        return( s );
    }

    /**
     * Returns the name of the environment where this connection exists.
     * @return      String 
     */
    public String getEnvironment()
    {
        // Give the user a chance to specify it as part of the properties file.
        // If not specified, default...
        String s = (String)getProperties().get( ENV );

        return( s );            
    }

    /**
     * Returns the host name.
     * @return      String 
     */
    public String getHostName()
    {
        // Give the user a chance to specify it as part of the properties file.
        // If not specified, default...
        String s = (String)getProperties().get( HOSTNAME);

        return( s );            
    }

    /**
     * Returns the channel.
     * @return      String 
     */
    public String getChannel()
    {
        // Give the user a chance to specify it as part of the properties file.
        // If not specified, default...
        String s = (String)getProperties().get( CHANNEL );

        return( s );            
    }

    /**
     * Returns the port
     *
     * @return      String 
     */
    public int getPort()
    {
        // Give the user a chance to specify it as part of the properties file.
        // If not specified, default...
        Integer port = new Integer( (String)getProperties().get( PORT ) );

        return( port.intValue() );            
    }

    /**
     * Returns the total # of failures on the primary queue.
     * @return      String 
     */
    public int getTotalPrimaryFailures()
    {
        if ( m_AssignedTotalPrimaryFailures == -9999 ) // first time throug
        {
            // Give the user a chance to specify it as part of the properties file.
            // If not specified, default...
            try
            {
                Integer totalFailures           = new Integer( (String)getProperties().get( TOTAL_PRIMARY_FAILURES ) );
                m_AssignedTotalPrimaryFailures  = totalFailures.intValue();
            }
            catch( NumberFormatException nfExc )
            {
                logErrorMessage( "MQConnectionImpl:getTotalPrimaryFailures() - failed to get totalPrimaryFailures property for MQConnection pool name " + this.getPoolName() + " : " + nfExc );
                m_AssignedTotalPrimaryFailures = -1;            
            }
        }
        
        return( m_AssignedTotalPrimaryFailures );            
    }

    /**
     * Returns the MQQueueManager in use.
     * @return      MQQueueManager 
     * @exception   FrameworkMQBaseException
     */
    public MQQueueManager getQueueManager()
    throws FrameworkMQBaseException 
    {
        // first of all, if they are both null, try to create again due
        // to failure to init()
        if ( theFrameworkMQQueueManager == null && backUpQueue == null )
        {
            this.createMQManager();
        }        
        
        MQQueueManager whichIBMMQManager = null;
        
        if ( theFrameworkMQQueueManager != null )
        {
            // use the internal one for this MQConnectionImpl
            whichIBMMQManager = theFrameworkMQQueueManager;
        }
        else 
        {
        	IMQConnectionImpl backup = null;
                	
	        try
	        {
	        	backup = getBackupQueue();
	        }
	        catch( ConnectionAcquisitionException exc )
	        {
	        	throw new FrameworkMQBaseException( "MQConnectionImpl.send(IFrameworkMQAdapter) - " + exc, exc );
	        }        	

            // get it from the backup
            if ( backup != null )
            {
                whichIBMMQManager = backup.getQueueManager();
            }
            else // even the back is unavailable
            {
                throw new FrameworkMQBaseException( "MQConnectionImpl:getQueueManager() - Main MQ Connection " + getQueueManagerName() + " unaccessible - Backup MQ Connection " + getBackupMQPoolName() + " is also unaccessible." );
            }
        }
        
        return( whichIBMMQManager );
    }
    
    
    /**
     * Assigns the correlationID to use.
     * @param	correlationID
     */
    public void setCorrelationID( byte[] correlationID )
    {
        this.correlationID = correlationID;                
    }
   
// helper methods

    /**
     * Increments the total failures in try to access or use this
     * MQConnectionImpl's associated IBM Queue.
     */
    protected void incrementPrimaryFailureCount()
    {
        // don't bother incrementing if the primary isn't even in use
        if ( primaryInUse == true )
        {
            currentTotalPrimaryFailures++;
        }
    }
    
    /**
     * Determines if the primary should be used, in favor of trying to 
     * get to the backup.  If the m_AssignedTotalPrimaryFailures is equal
     * -1, or is greater than the currentTotalPrimaryFailures, the primary
     * can continue to be used.
     */
    protected boolean useThisPrimaryConnection()
    {
        if ( primaryInUse == true )
        {
            if ( this.getTotalPrimaryFailures() != -1 &&
                    currentTotalPrimaryFailures >= m_AssignedTotalPrimaryFailures )
            {
                // class scope indicator for efficiency
                primaryInUse = false;
                
                // don't even bother using it from here on out...
                theFrameworkMQQueueManager = null;
            }
        }
        
        return( primaryInUse );
    }
    
    /**
     * Conditionally applies the assigned correlationID to the IFrameworkMQMessage
     * being worked on internally.   Returns the provided message.
     * <p>
     * @param        mqMessage
     * @return      IFrameworkMQMessage     
     */
    protected IFrameworkMQMessage applyCorrelationID( IFrameworkMQMessage mqMessage )
    {               
        if ( correlationID != null )
        {
            mqMessage.setCorrelationID( correlationID );
        }
        
        return( mqMessage );
    }
    
    /**
     * Creates the Queue Manager by name.
     * @exception	InitializationException
     */
    protected void initMQ()
    throws InitializationException
    {
        // make sure a queue name and queue manager name have been specified
        String s = getQueueName();

        if ( s == null || s.length() == 0 )
            throw new InitializationException( "MQConnectionImpl:initMQ() - invalid Queue Name provided in properties." );

        s = getQueueManagerName();

        if ( s == null || s.length() == 0 )
            throw new InitializationException( "MQConnectionImpl:initMQ() - invalid Queue Manager Name provided in properties." );

        // this is required in the event the MQ Host is running on a different machine
        String sHostName = getHostName();
        
        if ( sHostName != null )
        {
            com.ibm.mq.MQEnvironment.hostname  = sHostName;
            com.ibm.mq.MQEnvironment.channel   = getChannel(); 
            com.ibm.mq.MQEnvironment.port      = getPort();

            com.ibm.mq.MQEnvironment.properties.put(   com.ibm.mq.MQC.TRANSPORT_PROPERTY,//Set TCP/IP or server
                                                        com.ibm.mq.MQC.TRANSPORT_MQSERIES);//Connection
        }
        
        try
        {
            // create the wrapped IBM MQ Connection Manager
            createMQManager();
        }
        catch( Throwable exc )
        {
            throw new InitializationException( "MQConnectionImpl:initMQ() - " + exc, exc );
        }
    }

    /**
     * Validates an IFrameworkMQAdapter as not null.
     * <p>
     * @param       adapter
     * @exception   FrameworkMQBaseException
     */
    protected void validateMQAdapter( IFrameworkMQAdapter adapter )
    throws FrameworkMQBaseException
    {
        if ( adapter == null )
            throw new FrameworkMQBaseException( "MQConnectionImpl:validateMQAdapter(...) - null adapter provided." );
    }

    /**
     * Helper method used to close all open queues.
     */
    protected void closeAllQueues()
    {
        closeQueue( theFrameworkMQQueue );
    }

    /**
     * Internal helper method to close an IFrameworkMQQueue.
     * @param	queue
     */
    protected void closeQueue( IFrameworkMQQueue queue )
    {
        if ( queue != null )
        {
            try
            {
                queue.getTheIBMMQQueue().close();
            }
            catch ( MQException mqExc )
            {
            }
            finally
            {
                // null out the queue to deter future usage
                queue = null;
            }
        }
    }

    /**
     * Creates the underlying IBM MQ Manager.
     * @exception       FrameworkMQBaseException
     */
    protected void createMQManager()
    throws FrameworkMQBaseException
    {
        try
        {
            //*************************************
            // creating an MQManager implies a connection
            //*************************************
            theFrameworkMQQueueManager = new MQQueueManager( getQueueManagerName() );
        }
        catch ( MQException mqExc )
        {
            // for future safe reference
            theFrameworkMQQueueManager = null;
            
            logError( "MQConnectionImpl:createMQManager() - failed to create the Queue manager " +  getQueueManagerName() );
            
            try
            {
                logError( "MQConnectionImpl:initMQ() - attempting to create backup " + this.getBackupMQPoolName() );                                    
                getBackupQueue();                    
            }
            catch( Throwable exc2 )
            {
                String sMsg = "MQConnectionImpl:initMQ() - failed to create backup " + this.getBackupMQPoolName();                
                logError( sMsg );
                
                backUpQueue = null;
                
                throw new FrameworkMQBaseException( "MQConnectionImpl:createMQManager() - failed to create the primary for pool name " + getPoolName() + " and its backup " + this.getBackupMQPoolName() + " - " + exc2 );
            }
        }        
    }
    
    /**
     * Returns the designated IMQConnectionImpl as a backup to the primary.
     * <p>
     * @return	the designated back up mq connecton
     * @throws ConnectionAcquisitionException
     */
    protected IMQConnectionImpl getBackupQueue()
    throws ConnectionAcquisitionException
    {
        if ( backUpQueue == null )
        {
        	try
        	{
	            // failed to created attempt to get the backup up and running
    	        backUpQueue = (IMQConnectionImpl)ConnectionPoolManagerFactory.getObject().getConnection( getBackupMQPoolName() );
                            
        	    if ( backUpQueue == null )
            	{
                	String sMsg = "MQConnectionImpl:getBackupQueue() - failed to create backup " + this.getBackupMQPoolName();
                                
	                logError( sMsg );
                                
    	            throw new ConnectionAcquisitionException( "MQConnectionImpl:getBackupQueue() - failed to create the primary for pool name " + getPoolName() + " and its backup " + this.getBackupMQPoolName() );                        
        	    }
        	}
        	catch( Throwable exc )
        	{
        		throw new ConnectionAcquisitionException( "MQConnectionImpl:getBackupQueue() - " + exc );
        	}
        }

        return( backUpQueue );
    }
    
    /**
     * If there is a loghandler associated, use it.  Otherwise, use
     * the framework logging....
     * <p>
     * @param msg
     */
    protected void logError( String msg )
    {               
        try
        {
            IFrameworkLogHandler logHandler = FrameworkLogHandlerFactory.getInstance().getLogHandler( errorLogHandlerName );
            
            if ( logHandler != null )
                logHandler.error( msg );
            else
                logErrorMessage( msg );
        }
        catch( Throwable exc )
        {
            logInfoMessage( "MQConnectionImpl.logError() - Using internal logInfoMessage instead - " + exc );
            logErrorMessage( msg );
        }
    }

    /**
     * determines the error queue name
     *
     * @return      String
     */
    protected String getErrorLogHandlerName()
    {
        if ( errorLogHandlerName == null )
        {
            errorLogHandlerName = (String)getProperties().get( ERROR_LOG_HANDLER_NAME );
            
            if ( errorLogHandlerName == null )
            {
                // use the default one instead
                errorLogHandlerName = FrameworkNameSpace.DEFAULT_LOG_HANDLER_NAME;
            }
        }
        
        return( errorLogHandlerName );
    }


// helper methods

    /**
     * Factory helper method for creation.
     * <p>
     * @return	    MQConnectionImpl
     * @exception	ObjectCreationException 		
     */
    static public MQConnectionImpl create()
    throws ObjectCreationException
    {
    	try
    	{ 
    		return( new MQConnectionImpl() );
    	}
    	catch( Throwable exc )
    	{
			throw new ObjectCreationException( "MQConnectionImpl:create() - " + exc, exc );
    	}
    }

// attributes

    /**
     * the queue to do our work against
     */
    protected IFrameworkMQQueue theFrameworkMQQueue          = null;

    /**
     * the associated queue manager
     */
    protected MQQueueManager theFrameworkMQQueueManager      = null;

    /**
     * the name of the Framework Log Handler, declared in the 
     * log.properties and referenced in the properties for this
     * MQConnectionImpl
     */
    protected String errorLogHandlerName         = null;

    /**
     * client conditionally provided correlation id
     */
    protected byte[] correlationID               = null;
    
    /**
     * static helper data
     */
    static final protected String QUEUE_NAME            = "queueName";
    static final protected String QUEUE_MANAGER_NAME    = "queueManagerName";
    static final protected String ERROR_LOG_HANDLER_NAME = "errorLogHandlerName";
    static final protected String BACKUP_MQ_POOL_NAME   = "backupMQPoolName";
    static final protected String ENV                   = "env";
    static final protected String HOSTNAME              = "hostName";
    static final protected String PORT                  = "port";
    static final protected String CHANNEL               = "channel";
    static final protected String WAIT_TIME             = "waitTime";  
    static final protected String EXPIRY                = "expiry";  
    static final protected String TOTAL_PRIMARY_FAILURES = "totalPrimaryFailures";
    
    /**
     * refers to the BackUp MQ Connection
     */
    protected IMQConnectionImpl backUpQueue           = null;
    
    /**
     * MQ Connection Pool property indicator for total 
     * primary failures to acknowledge
     */
    protected int m_AssignedTotalPrimaryFailures        = -9999;
    
    /**
     * Current number of failures on the this MQConnectionImpl instance
     */
    protected int currentTotalPrimaryFailures         = 0;
    
    /**
     * Indicated if this primary connection is still 
     * to be utilized internally
     */
    protected boolean primaryInUse                     = true;
}

/*
 * Change Log:
 * $Log: MQConnectionImpl.java,v $
 * Revision 1.4  2003/10/08 02:01:28  tylertravis
 * Use logxxxMessage in place of logInfoMessage.
 *
 * Revision 1.3  2003/09/02 19:52:54  tylertravis
 * Changed javadoc comment on public int getTotalPrimaryFailures()
 *
 * Revision 1.2  2003/08/05 12:16:17  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:44  tylertravis
 * initial sourceforge cvs revision
 *
 */
