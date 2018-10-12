/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.integration.esb;


/**
 * 
 * Interface to an Enterprise Service Bus Manager
 * <p>
 * @author    realMethods, Inc.
 */
public interface IESBManager 
{
    /** 
     * Helper method used to make a call to an endpoint URI on the bus, but
     * without expecting a return
     */
     public void dispatch( String service, String methodName, Object payload, java.util.Properties props )
     throws com.framework.common.exception.ProcessingException;
    
    
   /** 
    * Helper method used to make a call to an endpoint URI on the bus, but waits
    * for a return
    */
    public Object send( String service, String methodName, Object payload, java.util.Properties props )
    throws com.framework.common.exception.ProcessingException;
    
    
    
	public java.util.Map<String, String> getProperties();
	public void setProperties( java.util.Map<String, String> props );
}