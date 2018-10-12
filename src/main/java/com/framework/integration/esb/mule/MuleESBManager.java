/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.esb.mule;

import com.framework.integration.esb.*;
import com.framework.common.FrameworkBaseObject;

import com.framework.common.namespace.FrameworkNameSpace;
import com.framework.common.properties.PropertyFileLoader;
;

/**
 * 
 * Mule ESB, subclass of the ESBManager
 * <p>
 * Authentication requires a CallbackHandler to provide a userid and password.
 * <p>
 * @author    realMethods, Inc.
 */
public class MuleESBManager extends ESBManager
{
	// static block
    {
    	try
    	{
    		if ( FrameworkNameSpace.GOOGLE_LICENSE_ENV == false )
    		{
	    		// get the parameters from the framework properties file
	    		endPointURL = PropertyFileLoader.getInstance().getFrameworkPropertiesHandler().getParam( ENDPOINTURL );
	    		configurationFiles = PropertyFileLoader.getInstance().getFrameworkPropertiesHandler().getParam( CONFIG_FILES );
	    		
	    		org.mule.config.builders.MuleXmlConfigurationBuilder builder = new org.mule.config.builders.MuleXmlConfigurationBuilder();
			    		
	    		// starts the UMOManager, applying the configuration file
	    		muleManager = builder.configure( configurationFiles );
	    		
	    		// for use later on 
	    		muleClient = new org.mule.extras.client.MuleClient();
    		}
    		else
    			System.out.println( "Mule ESB Manaager not started due to it being unsupported by the Google App Engine..." );
    	}
    	catch( Throwable e)
    	{
    		e.printStackTrace();
    		new FrameworkBaseObject().logErrorMessage( "Failed to start Mule ESB: " + e.getMessage() );
    	}
    }
    
	/** 
	 * Default Constructor
	 */
	public MuleESBManager()
	{
	}
	
    /** 
     * Helper method used to make a call to an endpoint URI on the bus, but
     * without expecting a return
     */
     public void dispatch( String service, String methodName, Object payLoad, java.util.Properties props )
     throws com.framework.common.exception.ProcessingException
     {
         try
         {
             muleClient.dispatch( buildEndPointURL( service, methodName ), payLoad, null);
         }
         catch( org.mule.umo.UMOException exc )
         {
             throw new com.framework.common.exception.ProcessingException( "MuleESBManager:dispatch(...) - Failed to call serevice" + service + ":" + methodName + ":" + payLoad + " - " + exc );
         }
    	 
     }
     
     /** 
      * Helper method used to make a call to an endpoint URI on the bus, but waits
      * for a return
      */
    public Object send( String service, String methodName, Object payLoad, java.util.Properties props )
    throws com.framework.common.exception.ProcessingException
    {
        Object returnValue = null;
        
        try
        {
            org.mule.umo.UMOMessage result = muleClient.send( buildEndPointURL( service, methodName ), payLoad, props );
            returnValue = result.getPayload();
        }
        catch( org.mule.umo.UMOException exc )
        {
            throw new com.framework.common.exception.ProcessingException( "MuleESBManager:send(...) - Failed to call serevice" + service + ":" + methodName + ":" + payLoad + " - " + exc );
        }
                     
    	return( returnValue );   
    }
    
    /**
     * Helper method used to build an axis endpoint url
     */
    protected String buildEndPointURL( String serviceName, String methodName )
    {
        if ( serviceName == null )
            logErrorMessage( "MuleESBManager.buildEndPointURL() - Null serviceName argument" );
            
        if ( methodName != null && methodName.length() > 0 )
            logErrorMessage( "MuleESBManager.buildEndPointURL() - Non-null but empty methodName argument for service " + serviceName );
     
        String endpoint = org.mule.MuleManager.getInstance().lookupEndpointIdentifier(serviceName, null);
        
        if ( endpoint == null )
        {
            logDebugMessage( "MuleESBManager.buildEndPointURL() - endpoint is null for service named " + serviceName );
            return( serviceName );
        }
        
        if (endpoint.startsWith("glue") || endpoint.startsWith("axis") ||
                endpoint.startsWith("soap"))
        {
            int i = endpoint.indexOf('?');
            if(i > -1) 
            {
                endpoint = endpoint.replaceFirst("\\?", "/" );
                if ( methodName != null && methodName.length() > 0 )
                	endpoint += serviceName + "?method=" + methodName + "\\&";
            } 
            else 
            {
                endpoint += "/" + serviceName;
                if ( methodName != null && methodName.length() > 0 )
                	endpoint += "?method=" + methodName;
            }
        }

        logDebugMessage( "Transformed Endpoint is " + endpoint + " for service " + serviceName );

        return endpoint;
        
    }

      
// attributes
    
    protected static String endPointURL = null;
    protected static String configurationFiles = null;
    protected static org.mule.extras.client.MuleClient muleClient;
    protected static org.mule.umo.manager.UMOManager muleManager;
    private static final String CONFIG_FILES = "config-files";
    private static final String ENDPOINTURL = "endPointURL";
}
