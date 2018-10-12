/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.task;

/**
 * interface IFrameworkTaskFactory
 *
 * <p>
 * @author    realMethods, Inc.
 */
public interface IFrameworkTaskFactory 
    extends java.io.Serializable
{
   /**
     * returns the Class representation of the named Task class
     * <p>
     * @param       taskClassName
     * @return      Class
     */
    public Class getTaskAsClass( String taskClassName );

    /**
     * Returns the IFrameworkTask representation of the 
     * fully qualified named IFrameworkTask class
     * <p>
     * @param       taskClassName
     * @return      IFrameworkTask
     */
    public IFrameworkTask getTaskAsObject( String taskClassName );    
}    

/*
 * Change Log:
 * $Log: IFrameworkTaskFactory.java,v 
 */
