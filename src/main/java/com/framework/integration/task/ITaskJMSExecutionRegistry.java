/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.task;

import java.util.HashMap;

import com.framework.common.exception.TaskJMSRegistrationException;

import com.framework.common.jmx.IFrameworkDynamicMBean;

/**
 * TaskJMSExecutionRegistry interface
 * <p>
 * @author    realMethods, Inc.
 */
public interface ITaskJMSExecutionRegistry extends IFrameworkDynamicMBean
{
    /**
     * Use this method to register a Task class name with a message to 
     * execute on.  A TaskJMSRegistrationException is thrown if the registration fails.
     * If a Task by the classname is already registered, first call unregister, otherwise
     * a TaskJMSRegistrationException will be thrown.
     * <p>
     * @param       taskClassName                                                      
     * @param       executionMessage
     * @exception   IllegalArgumentException
     * @exception   TaskJMSRegistrationException
     */
    public void registerTask( String taskClassName, String executionMessage )
    throws IllegalArgumentException, TaskJMSRegistrationException;
    
   /**
     * returns the Collection of TaskJMSExecutionHandler.
     * @return      loaded and/or registered TaskJMSExecutionHandlers
     */
    public HashMap getTaskJMSExecutionHandlers();    
    
}

/*
 * Change Log:
 * $Log: ITaskJMSExecutionRegistry.java,v $
 */
