/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.presentation.monitor;

//***********************************
// Imports
//***********************************
import com.framework.common.FrameworkBaseObject;

/**
 * Class ApplicationInfo
 * 
 * Simple container for a Session monitor.  There will be one of these for each HTTP Session.
 *
 * <p>
 * @author    realMethods, Inc.
 */
public final class ApplicationInfo extends FrameworkBaseObject
{
//************************************************************************    
// Public Methods
//************************************************************************
    /**
     * Constructor.
     */
    public ApplicationInfo()
    {
        // Create Session Monitor
        sessionMonitor = new SessionMonitor();
    }

    /**
     * Retrieves the underlying applications SessionMonitor.
     * 
     * @return SessionMonitor
     */
    public synchronized SessionMonitor getSessionMonitor()
    {
        return sessionMonitor;   
    }
//************************************************************************    
// Private / Protected Methods
//************************************************************************

//************************************************************************    
// Attributes
//************************************************************************
    /**
     * HttpSession Monitor.
     */
    private SessionMonitor sessionMonitor = null;
}

/*
 * Change Log:
 * $Log: ApplicationInfo.java,v $
 * Revision 1.2  2003/08/05 12:16:17  tylertravis
 * - Updated header language to LGPL
 * - Improved exception handling globally
 * - Modified member variables to conform to Java Beans std.
 *
 * Revision 1.1.1.1  2003/07/05 03:05:21  tylertravis
 * initial sourceforge cvs revision
 *
 */


