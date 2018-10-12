/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.common.event;


import com.framework.common.FrameworkBaseObject;

/**
 * Enumerated class used to contain a single Framework related event.
 * <p>
 * All support instances are contained as static final attributes.
 * <p> 
 * @author    realMethods, Inc.
 */
public class FrameworkEventType
    extends FrameworkBaseObject implements java.io.Serializable
{

// constructors

    /**
     * deter external instantiation
     */
    protected FrameworkEventType()
    {}
     
	/**
	 * @param type		uniquely named type		
	 */
    protected FrameworkEventType( String type )
    {
        eventType = type;
    }

// accessor methods

    /**
     * return the event type
     *
     * @return	event type
     */
    public String getEventType()
    {
        return( eventType );
    }
        
// attributes

    /**
     * static self instances, appropriately named
     */
    final static public FrameworkEventType FrameworkStartup             = new FrameworkEventType( "FrameworkStartup" );
    final static public FrameworkEventType FrameworkShutDown             = new FrameworkEventType( "FrameworkShutdown" );
    final static public FrameworkEventType PreHttpServletRequestProcessor    = new FrameworkEventType( "PreHttpServletRequestProcessor" );
    final static public FrameworkEventType PostHttpServletRequestProcessor   = new FrameworkEventType( "PostHttpServletRequestProcessor" );
    final static public FrameworkEventType HttpServletRequestProcessorError  = new FrameworkEventType( "HttpServletRequestProcessorError" );

    
    final static public FrameworkEventType PreTaskExecute               = new FrameworkEventType( "PreTaskExecute" );
    final static public FrameworkEventType PostTaskExecute              = new FrameworkEventType( "PostTaskExecute" );
    final static public FrameworkEventType TaskExecutionFailure         = new FrameworkEventType( "TaskExecutionFailure" );

    final static public FrameworkEventType PreCommandExecute          = new FrameworkEventType( "PreCommandExecute" );
    final static public FrameworkEventType PostCommandExecute         = new FrameworkEventType( "PostCommandExecute" );
    final static public FrameworkEventType CommandExecutionFailure    = new FrameworkEventType( "CommandExecutionFailure" );

    final static public FrameworkEventType PrePageRequestProcessor    = new FrameworkEventType( "PrePageRequestProcessor" );
    final static public FrameworkEventType PostPageRequestProcessor   = new FrameworkEventType( "PostPageRequestProcessor" );
    final static public FrameworkEventType PageRequestProcessorError  = new FrameworkEventType( "PageRequestProcessorError" );
 
    final static public FrameworkEventType UserSessionBound             = new FrameworkEventType( "UserSessionBound" );
    final static public FrameworkEventType UserSessionUnBound           = new FrameworkEventType( "UserSessionUnBound" );
    
    final static public FrameworkEventType CheckedExceptionThrown	= new FrameworkEventType( "CheckedExceptionThrown" );
    
	final static public FrameworkEventType Authentication     		= new FrameworkEventType( "Authorization" );
	final static public FrameworkEventType Authorization         	= new FrameworkEventType( "Authorization" );

    /**
     * the encapsulated event type
     */
    protected String eventType           = null;
}

/*
 * Change Log:
 * $Log: FrameworkEventType.java,v $
 */

