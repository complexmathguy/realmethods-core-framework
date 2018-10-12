/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.presentation.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.framework.common.properties.PropertyFileLoader;

import com.framework.common.startup.StartupManager;

/**
 * Base class for all Framework related servlets.
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkBaseServlet 
extends HttpServlet
{
//************************************************************************    
// Public Methods
//************************************************************************

    /**
     * Initialization method.
     * @exception ServletException
     */
    public void init() throws ServletException
    {    	
        // call base class
        super.init();
        
        //------------------------------------------------------
        // Get the Framework started...
        //------------------------------------------------------    
        try
        {
	        startDateTime = java.util.Calendar.getInstance();
        	
            startFramework();
        }
        catch( Throwable exc )
        {
            throw new ServletException( "FrameworkBaseServlet:init() - " + exc );
        }
        
    }
    
    /**
     * Initialization method.
     * 
     * @param 		config 			ServletConfig object
     * @exception ServletException
     */
    public void init( ServletConfig config ) throws ServletException
    {
        // call base class
        super.init(config);        
    }

    /** 
     * Destroy method - called when servlet is unloaded.
     */
    public void destroy()
    {        
    	// stop the Framework
    	StartupManager.getInstance().stop();
    	
        super.destroy();
    }

    /**
     * Returns the description of the servlet.
     * @return The description of the servlet.
     */
    public String getServletInfo()
    {
        return "Framework Base Servlet";
    }


    /**
     * Service method that handles standard HTTP requests by dispatching them to
     * the appropriate implemented method.
     * <p>
     * @param 		req 		the HttpServletRequest
     * @param 		resp 		the HttpServletResponse
     * @exception 	ServletException If the request cannot be serviced.
     * @exception 	IOException If there was an IO exception.
     */
    public void service( HttpServletRequest req, HttpServletResponse resp )
        throws ServletException, IOException
    {
        // call base class
        super.service(req, resp);
    }

    /**
     * Services all GET requests for the servlet.
     * <p>
     * @param 		req 		the HttpServletRequest
     * @param 		resp 		the HttpServletResponse
     * @exception 	ServletException If the request cannot be serviced.
     * @exception 	IOException If there was an IO exception.
     */
    public void doGet( HttpServletRequest req, HttpServletResponse resp )
        throws ServletException, IOException
    {
        // process request
        processRequest(req, resp);
    }

    /**
     * Services all POST requests for the servlet.
     * <p>
     * @param 		req 		the HttpServletRequest
     * @param 		resp 		the HttpServletResponse
     * @exception 	ServletException If the request cannot be serviced.
     * @exception 	IOException If there was an IO exception.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        // process request
        processRequest(req, resp);
    }

    /**
     * Services all PUT requests for the servlet.
     * <p>
     * @param 		req 		the HttpServletRequest
     * @param 		resp 		the HttpServletResponse
     * @exception 	ServletException If the request cannot be serviced.
     * @exception 	IOException If there was an IO exception.
     */
    public void doPut( HttpServletRequest req, HttpServletResponse resp )
        throws ServletException, IOException
    {
        // call base class
        super.doPut(req, resp);
    }

    /**
     * Services all DELETE requests for the servlet.
     * <p>
     * @param 		req 		the HttpServletRequest
     * @param 		resp 		the HttpServletResponse
     * @exception 	ServletException If the request cannot be serviced.
     * @exception 	IOException If there was an IO exception.
     */
    public void doDelete( HttpServletRequest req, HttpServletResponse resp )
        throws ServletException, IOException
    {
        // call base class
        super.doDelete(req, resp);
    }


// accessor

    /**
     * Returns the start date time
     * <p>
     * @return      java.util.Calendar
     */
    public java.util.Calendar getStartDateTime()
    {
        return( startDateTime );
    }

// action methods


    /**
     * Method gets the Framework started
     */
    public void startFramework()
    {
        // preload all property files
                
        try
		{
   //     	System.out.println( "*** servlet context is: " + getServletContext() );
   //     	System.out.println( "*** resource is: " + getServletContext().getResource( "/WEB-INF/properties" ) );

			if ( PropertyFileLoader.getInstance().hasLoadedPropertyfiles() == false )			
				PropertyFileLoader.getInstance().loadPropertyFiles( getServletContext().getResource( "/WEB-INF/properties" ) );
        }
        catch( Throwable exc )
        {
            System.out.println( "***************" );
            System.out.println( "Framework Property Loading Warning" );
            System.out.println( "FrameworkBaseServlet:startFramework() - unable to locate the necessary property in directory /WEB-INF/properties under the Servlet context.");
			System.out.println( exc );
            System.out.println( "***************" );  
            exc.printStackTrace();
            
        }    
        
        StartupManager.getInstance().start( getServletConfig().getServletName(), null );
        
    }

//************************************************************************    
// Private / Protected Methods
//************************************************************************

    /**
     * Process method that is called by the doGet() and doPost(). Sub-classes do the real work by
     * overloading this method.
     * <p>
     * @param 		req 		the HttpServletRequest
     * @param 		resp 		the HttpServletResponse
     * @exception 	ServletException If the request cannot be serviced.
     * @exception 	IOException If there was an IO exception.
     */
    protected void processRequest( HttpServletRequest req,
                                    HttpServletResponse resp )
        throws ServletException, IOException
    {
        return;   
    }
    

//************************************************************************    
// Attributes
//************************************************************************
    
    /**
     * Servlet start date and time
     */
    protected java.util.Calendar startDateTime	= null;

}

/*
 * Change Log:
 * $Log: FrameworkBaseServlet.java,v $
 */
