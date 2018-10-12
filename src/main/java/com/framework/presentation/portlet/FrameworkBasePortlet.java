/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.presentation.portlet;

import javax.portlet.ActionRequest; 
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest; 
import javax.portlet.RenderResponse;

import org.apache.portals.bridges.struts.StrutsPortlet;

import com.framework.common.properties.PropertyFileLoader;
import com.framework.common.startup.StartupManager;

/**
 * Base class for all Framework related portlets.
 * <p>
 * @author    realMethods, Inc.
 */
public class FrameworkBasePortlet extends StrutsPortlet
{
//************************************************************************    
// Public Methods
//************************************************************************

    /**
     * Initialization method.
     * @exception ServletException
     */
	public void init( PortletConfig config )
    throws PortletException
    {    	        
		System.out.println( "\n\n*** Inside of Porlet Init\n\n" );
		
        //------------------------------------------------------
        // Get the Framework started...
        //------------------------------------------------------    
        try
        {
    		super.init( config );        	
            //startFramework();
        }
        catch( Throwable exc )
        {
        	exc.printStackTrace();
            throw new PortletException( "FrameworkBasePortlet:init() - " + exc );
        }        
        
        System.out.println( "\n\n*** Leaving of Porlet Init\n\n" );
    }
    
    /** 
     * Destroy method - called when servlet is unloaded.
     */
    public void destroy()
    {   
    	super.destroy();
    	
    	// stop the Framework
    	StartupManager.getInstance().stop();
    }

    public void processAction( ActionRequest request,ActionResponse response )
    throws PortletException, java.io.IOException
    {
		System.out.println( "\n\n*** Inside of Porlet processAction\n\n" );
    	
    	super.processAction( request, response );
    }
    
    public void render( RenderRequest request, RenderResponse response )
    throws PortletException, java.io.IOException
	{
		System.out.println( "\n\n*** Inside of Porlet render\n\n" );
    	
    	super.render( request, response );
	}
    
    public void doView( RenderRequest request,RenderResponse response ) 
    throws PortletException, java.io.IOException
    {
		System.out.println( "\n\n*** Inside of Porlet doView\n\n" );
    	
    	super.doView( request, response );
    }

    public void doEdit( RenderRequest request,RenderResponse response )
    throws PortletException, java.io.IOException
    {
		System.out.println( "\n\n*** Inside of Porlet doEdit\n\n" );
    	
    	super.doEdit( request, response );
    }
    

// action methods

    /**
     * Method gets the Framework started
     */
    public void startFramework()
    {
        // preload all property files
    	PortletRequest request = null;
    	PortletResponse response = null;
    	
        try
		{
			if ( PropertyFileLoader.getInstance().hasLoadedPropertyfiles() == false )			
				PropertyFileLoader.getInstance().loadPropertyFiles( getServletContext( this, request, response ).getResource( "/WEB-INF/properties" ) );
        }
        catch( Throwable exc )
        {
            System.out.println( "***************" );
            System.out.println( "Framework Property Loading Warning" );
            System.out.println( "FrameworkBaseServlet:startFramework() - unable to locate the necessary property in directory /WEB-INF/properties under the Servlet context.");
			System.out.println( exc );
            System.out.println( "***************" );         
            
        }    
        
        StartupManager.getInstance().start( this.getPortletName(), null );
        
    }

//************************************************************************    
// Private / Protected Methods
//************************************************************************    

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
