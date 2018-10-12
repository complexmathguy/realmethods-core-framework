/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.presentation.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * This class should be used as a means of automatically running the 
 * the Framework in situations where a web tier isn't present, yet the Framework
 * needs to be explicity started on application deployment
 * <p>
 * @author    realMethods, Inc.
 */

public class FrameworkStartupServlet extends FrameworkBaseServlet 
{
  
    /**
     * Initialization method.
     * @param 		config 		ServletConfig object.
     * @exception 	ServletException
     */  
    public void init( ServletConfig config ) 
    throws ServletException
    {
        // call base class
        super.init(config);        
    }
}

/*
 * Change Log:
 * $Log: FrameworkStartupServlet.java,v $
 */
