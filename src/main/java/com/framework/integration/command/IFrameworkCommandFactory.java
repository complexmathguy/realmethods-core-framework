/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.command;

/**
 * Command factory interface
 * <p>
 * @author    realMethods, Inc.
 */
public interface IFrameworkCommandFactory 
    extends java.io.Serializable
{
   /**
     * Returns the Class representation of the named Command class
     * <p>
     * @param       commandClassName	fully qualified Command class name
     * @return      					class instance inferred from commandClassName
     */
    public Class getCommandAsClass( String commandClassName );

    /**
     * Returns the IFrameworkCommand representation of the 
     * fully qualified named IFrameworkCommand class
     * <p>
     * @param       commandClassName	fully qualified Command class name
     * @return      					instance of object created by commandClassName
     */
    public IFrameworkCommand getCommandAsObject( String commandClassName );    
}    

/*
 * Change Log:
 * $Log: IFrameworkCommandFactory.java,v $
 */
