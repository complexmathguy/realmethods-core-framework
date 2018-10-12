/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.uid;

/**
 * class FrameworkUIDGenerator
 * 
 * Framework implementation of interface IFrameworkUIDGenerator.  If no fully qualified class name
 * is provided within the  framework.xml file for property FrameworkUIDGenerator, this class
 * will be used as the application provider for generating UIDs.
 * 
 * 
 * <p> 
 * @author    realMethods, Inc.
 */
public class FrameworkUIDGenerator implements IFrameworkUIDGenerator
{
	/**
	  * Generate a identifier, which may not be guaranteed to
	  * be unique, using the provided key
	  * <p>
	  * @param	key		client provided key to possibly assist in generating the uid
	  * @return	the unique identifier
	  */
	public String generateUID( Object key )
	{	
		// delgate
        long id = new Long( new java.text.SimpleDateFormat( "yyyyMMddhhmmss" ).format( java.util.Calendar.getInstance().getTime() ) ).longValue();
        
        if ( com.framework.common.namespace.FrameworkNameSpace.GOOGLE_LICENSE_ENV  == false )
        	id += new java.rmi.server.ObjID().toString().hashCode();
        
        return( String.valueOf( id ) );		
	}	
}

/*
 * Change Log:
 * $Log: FrameworkUIDGenerator.java,v $
 */



