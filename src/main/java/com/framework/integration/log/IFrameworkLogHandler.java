/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.log;

import java.util.Collection;
import java.util.Map;

import com.framework.common.event.FrameworkLogEventType;

import com.framework.common.exception.FrameworkLogException;

import com.framework.common.jmx.IFrameworkDynamicMBean;

/**
 * Base interface for a Framework related log handler implementation.
 * <p>
 * @author    realMethods, Inc.
 */
public interface IFrameworkLogHandler extends IFrameworkDynamicMBean
{
    /**
     * Returns the log name
     * @return      String 
     */
    public String getLogName();
    
    /**
     * Returns the synchronous flag
     * @return      boolean 
     */
    public boolean isSynchronous();
    
    /**
     * Returns the Collection of debug related destinations as Strings
     * @return      Collection
     */    
    public Collection getDebugDestinations(); 
       
    /**
     * Returns the Collection of info related destinations as Strings
     * @return      Collection
     */    
    public Collection getInfoDestinations();
    
    /**
     * Returns the Collection of warning related destinations as Strings.
     *
     * @return      Collection
     */    
    public Collection getWarningDestinations();
    
    /**
     * Returns the Collection of error related destinations as Strings
     * @return      Collection
     */    
    public Collection getErrorDestinations();
    
    /**
     * Returns the correct destination collection based on the FrameworkLogEventType
     * @return      Collection
     */
    public Collection getDestinations( FrameworkLogEventType type );  
    
    /**
     * Returns the datetime format to use
     * @return      String
     */
    public String getDateTimeStampFormatted();    
    
    /**
     * Returns the internal properties
     *
     * @return      Map
     */
    public Map getProperties();    
    
    /**
     * Default handler for logging debug related messages.
     * <p>
     * @param msg   text of the exception
     */
    public void debug( String msg );

    /**
     * Default handler for logging information related messages.
     * <p>
     * @param msg   text of the exception
     */
    public void info( String msg );

    /**
     * Default handler for logging warning related messages.
     * <p>
     * @param msg   text of the exception
     */
    public void warning( String msg );
    
    /**
     * Default handler for logging error related messages.
     * <p>
     * @param msg   text of the exception
     */
    public void error( String msg );
    
    /**
     * Convenience method for sending a message of a certain log event type to the
     * default log handler.
     * @param      	logEventType
     * @param     	msg
     * @exception	FrameworkLogException
     */
    public void log( FrameworkLogEventType logEventType, String msg )
    throws FrameworkLogException;    
    
	// static final helpers
    public static final String SYSTEM_OUT 	= "SYSTEM_OUT";
    public static final String SYNCHRONOUS	= "synchronous";
    public static final String DEBUG       	= "debug";
    public static final String INFO       	= "info";
    public static final String WARNING      = "warn";
    public static final String ERROR        = "error";
    public static final String DATE_TIME_STAMP_FORMAT = "dateTimeStampFormat";
    
}

/*
 * Change Log:
 * $Log: IFrameworkLogHandler.java,v $
 */
