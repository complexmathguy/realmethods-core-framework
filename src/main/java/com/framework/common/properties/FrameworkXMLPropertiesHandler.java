/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.common.properties;

import java.util.Map;

/**
 * Provider of property access to the contents of the framework related config file.
 * <p>
 * @author		realMethods, Inc.
 */
public class FrameworkXMLPropertiesHandler
	extends XMLPropertiesHandler
	implements IFrameworkPropertiesHandler
{

//************************************************************************    
// Constructors
//************************************************************************


	public FrameworkXMLPropertiesHandler()
	{
		super();
	}
	
//************************************************************************    
// Public Methods
//************************************************************************
  	
// IFrameworkPropertiesHandler implementations

	/**
	 * Returns the root parameters.
	 * @return	all presentation and integration tier properties
	 */
	public Map getParams()
	{
		Map params = (Map)cache( "framework-tier-properties" );
		
		if ( params == null )
		{
			params = getFrameworkXMLParser().getAttributesForFirstOccurance( "presentation-tier" );
			params.putAll( getFrameworkXMLParser().getAttributesForFirstOccurance( "integration-tier" ) );
			params.putAll( getFrameworkXMLParser().getAttributesForFirstOccurance( "esb-params" ) );
			
			cache( "framework-tier-properties", params );
		}
		
		return ( params );
	}

	/**
	 * Returns the parameter value for the provided key.
	 * <p>
	 * @param 	key
	 * @return	parameter
	 */
	public String getParam( String key )
	{
		Map params 		= getParams();
		String param	= null;
		
		if ( params != null )
		{
			param = (String)params.get( key );
		}
				
		return( param );
	}
		
	/**
	 * Returns the parameter value for the provided key.  If the key doesn't exist, or no
	 * value has been applied, returns the defValue.
	 * <p>
	 * @param key		key to parameter value
	 * @param defValue	default value
	 * @return			related parameter, or the def value if not found
	 */		
	public String getParam( String key, String defValue )
	{
		String param = getParam( key );
		
		// apply the defValue if not found
		if ( param == null )
		{
			param = defValue;
		}
		
		return( param );
	}

	/**
	 * Returns the mapping of jndi args.
	 * @return	jndi args
	 */
	public Map getESBArgs()
	{
		Map params = (Map)cache( "ESB" );
		
		if ( params == null )
		{
			params = getFrameworkXMLParser().getAttributesForFirstOccurance( "esb-params" );
			cache( "ESB", params );
		}
		
		return ( params );		
	}	
	/**
	 * Returns the mapping of jndi args.
	 * @return	jndi args
	 */
	public Map getJNDIArgs()
	{
		Map params = (Map)cache( "JNDI_ARGS" );
		
		if ( params == null )
		{
			params = getFrameworkXMLParser().getAttributesForFirstOccurance( "JNDI_ARGS" );
			cache( "JNDI_ARGS", params );
		}
		
		return ( params );		
	}
  	
	/**
	 * Returns the mapping of startup declarations.
	 * @return		startups
	 */
	public Map getStartups()
	{
		Map params = (Map)cache( "startups" );
		
		if ( params == null )
		{
			params = getFrameworkXMLParser().getAttributesForFirstOccurance( "startups" );
			cache( "startups", params );
		}
		
		return ( params );	
	}
	/**
	 * Returns the mapping of hook declarations.
	 * @return	hooks
	 */
	public Map getHooks()
	{
		Map params = (Map)cache( "hooks" );
		
		if ( params == null )
		{
			params = getFrameworkXMLParser().getAttributesForFirstOccurance( "hooks" );
			cache( "hooks", params );
		}
		
		return ( params );			
	}
  	
	/**
	 * Returms the mapping of factory declarations.
	 * @return	factory declarations
	 */
	public Map getFactoryDecls()
	{
		Map params = (Map)cache( "factoryDecls" );
		
		if ( params == null )
		{
			params = getFrameworkXMLParser().getAttributesForFirstOccurance( "factoryDecls" );
			cache( "factoryDecls", params );
		}
		
		return ( params );
	}
	
//*************************************************************************    
// Protected / Protected Methods
//************************************************************************

//************************************************************************    
// Attributes
//************************************************************************

}

/*
 * Change Log:
 * $Log$
 */
