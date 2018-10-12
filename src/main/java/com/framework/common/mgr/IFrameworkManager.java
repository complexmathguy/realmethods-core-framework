/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.mgr;

import java.util.Properties;

import com.framework.common.exception.ConnectionAcquisitionException;
import com.framework.common.exception.TaskExecutionException;

import com.framework.integration.objectpool.jms.JMSImpl;

import com.framework.integration.objectpool.ldap.LDAPConnectionImpl;

/**
 * The purpose of this interface has been diminished as of v3.0.  Simply provides indiret
 * access to JMS releted connections and the default LDAP connection, all created by the 
 * ConnectionPoolManager. 
 * <p> 
 * @author    realMethods, Inc.
 * @see		  com.framework.integration.objectpool.ConnectionPoolManager
 */
public interface IFrameworkManager
{
    /**
     * returns the global Framework common task execution JMSImpl
     *
     * @return      JMSImpl
     * @exception   ConnectionAcquisitionException
     */
    public JMSImpl getTaskExecutionJMSImpl()
    throws ConnectionAcquisitionException;

    /**
     * returns the global Framework common event related JMS Connection
     *
     * @return      JMSImpl
     * @exception   ConnectionAcquisitionException
     */
    public JMSImpl getFrameworkEventJMSImpl()
    throws ConnectionAcquisitionException;
    
    /**
     * returns the global Framework common LDAP Connection
     *
     * @return      LDAPConnectionImpl
     * @exception   ConnectionAcquisitionException
     */
    public LDAPConnectionImpl getDefaultLDAPConnectionImpl()
    throws ConnectionAcquisitionException;
    
    /**
     * returns the Framework related Properties
     *
     * @return      Properties
     */
    public Properties getProperties();


    /**
     * Helper method used to call a Task to execute.  Right now, the current
     * implementation sends a TaskJMSMessage on the Framework TaskExecutionQueue.
     * By default, the TaskJMSMessage indicates to the Task to handle execution
     * as though it were a single transaction.  If this is not the desired behavior
     * simple call getTaskExecutionQueue() and then call sendObject(), creating your
     * TaskJMSMessage with the required argument settings on construction.
     *
     * @param       taskName
     * @param       arg			argument to provide to the task
     * @exception   TaskExecutionException
     */
    public void executeTask( String taskName, Object arg )
    throws TaskExecutionException;
}

/*
 * Change Log:
 * $Log: IFrameworkManager.java,v $
 */
