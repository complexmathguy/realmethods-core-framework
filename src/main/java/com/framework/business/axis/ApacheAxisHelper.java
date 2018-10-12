/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.axis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.namespace.QName;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.FrameworkSOAPException;

import com.framework.common.misc.Utility;

import com.framework.common.namespace.FrameworkNameSpace;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

/**
 * Use this class to invoke a method on a service located on a service running the Apache Axis service handler.
 * <p>
 * @author    realMethods, Inc.
 */
public class ApacheAxisHelper extends FrameworkBaseObject
{
// constructors

	/**
	 * default constructor
	 */
	private ApacheAxisHelper()
	{
		
	}
	
   /**
    * Helper method to make the call to an Apache Axis Server
    *
    * @param      	methodName		name of method to call   
    * @param      	axisParams 	 	Collection of FrameworkAxisSOAPParameters
    * @param      	targetURN		URI of the remmote target as a service
    * @param		typeMappings	Collection of FrameworkAxisTypeMappings
    * @param		returnType		null implies no return value
    * @return     	return value from  method invocation
    * @exception 	FrameworkSOAPException
    * @exception	IllegalArgumentException
    */
    static public Object makeAxisCall(	String methodName, 
										Collection axisParams,    
                        		        String targetURN,
                                		Collection typeMappings,
                                		QName returnType )
    throws FrameworkSOAPException, IllegalArgumentException
    {       
    	new FrameworkBaseObject().logDebugMessage(  "ApacheAxisHelper.makeSOAPCall() - " + methodName + " : " +  targetURN + " : " + axisParams + " : " + typeMappings );
    	
		if ( axisServerURI == null )
		{
			axisServerURI = Utility.getFrameworkProperties().getProperty( FrameworkNameSpace.SOAP_SERVER_URI );
		}
		    	
		// check the Target URN
		if ( targetURN == null || targetURN.length() == 0 )
			 throw new IllegalArgumentException( "ApacheAxisHelper:makeSOAPCall(...) - cannot provide a null or empty Target URN"  );
      	
		Service service = new Service();
		Call call    	= null;
		
		try
		{
			call = (Call) service.createCall();
		}
		catch( javax.xml.rpc.ServiceException exc )
		{				
			throw new FrameworkSOAPException( "ApacheAxisHelper.makeSOAPCall(...) - failed to create the Call object - " + exc, exc );
		}

		// assign the Target Object URI, append urn: if needed
		if ( targetURN.startsWith( "urn:" ) == false );
			targetURN = "urn:" + targetURN;
					
		try		
		{	
			call.setTargetEndpointAddress( new java.net.URL( axisServerURI + "/" + targetURN ) );							
		}
		catch( Throwable exc )
		{
			throw new FrameworkSOAPException( "ApacheAxisHelper.makeSOAPCall(...) - failed to set the setTargetEndpointAddress - " + exc, exc );			
		}

		call.setOperationName(new QName("urn:", methodName));
		
		// apply the soap parameters
		FrameworkAxisSOAPParameter soapParam 	= null;
		ArrayList args							= new ArrayList();
		
		if ( axisParams != null )
		{
			// if we havev params, need to assign the returntype
			if ( returnType != null )
				call.setReturnType( returnType );
				
			Iterator iter= axisParams.iterator();
			
			while ( iter.hasNext() )
			{
				soapParam = (FrameworkAxisSOAPParameter)iter.next();			
				call.addParameter( 	soapParam.getName(), 
									soapParam.getXMLType(), 
									soapParam.getParameterMode() );
									
				args.add( soapParam.getParameter() );
			}
		}
					
		typeMappings = getAxisTypeMappingRegistryMappings();
		
		// register the type mappings
		if ( typeMappings != null )
		{
			FrameworkAxisTypeMapping typeMapping = null;
			Iterator iter = typeMappings.iterator();
		
			while( iter.hasNext() )
			{
				typeMapping = (FrameworkAxisTypeMapping)iter.next();			
				call.registerTypeMapping( 	typeMapping.getMappingType(), 
											typeMapping.getXMLType(), 
											typeMapping.getSerializerFactory(), 
											typeMapping.getDeserializerFactory() );
			}
		}
		
		// make the call
		Object ret = null;
		
		try
		{
			ret = call.invoke( args.toArray() );			
			new FrameworkBaseObject().logDebugMessage( "ApacheAxisHelper.makeAxisCall on " + methodName + " returned " + ret );
		}
		catch( Throwable exc )
		{
			exc.printStackTrace();
			throw new FrameworkSOAPException( "ApacheAxisHelper.makeSOAPCall(...)  - failed during invocation - " + exc.getMessage(), exc );
		}
		
		return ret;        
    }            

    /**
     * Returns the Axis Type Mapping Registry.
     * <note>Not yet in use...</note>
     * @return Collection of FrameworkAxisTypeMappings
     */
    static public Collection getAxisTypeMappingRegistryMappings()
    {
    	if ( axisTypeMappings == null )
    	{
    		IFrameworkAxisTypeMappingRegistry typeMappingRegistry = null;
    		
    		try
			{
    			typeMappingRegistry = FrameworkAxisTypeMappingRegistryFactory.getInstance().getAxisTypeMappingRegistry();
    		
    			if ( typeMappingRegistry != null )
    			{    				
    				axisTypeMappings = typeMappingRegistry.getTypeMappings();
    				
    				System.out.println( "\n\naxisTypeMappings are: " + axisTypeMappings );
    			}
			}
    		catch( Throwable exc )
			{    			
			}
    	}

    	return( axisTypeMappings );
    }
    
// attributes

	static protected String axisServerURI	=  null;    
	static protected Collection axisTypeMappings = null;
	
}

/*
 * Change Log:
 * $Log:  $
 */
