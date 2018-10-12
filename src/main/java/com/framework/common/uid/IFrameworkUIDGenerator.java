/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.uid;

/**
 * Base interface of the implementation providing for the generation of an internal identifier
 * using caller provided key. Provide the full class name of an implementation within
 * the framework.xml FrameworkUIDGenerator property
 * <p> 
 * @author    realMethods, Inc.
 */
public interface IFrameworkUIDGenerator
{
   /**
     * Generate a identifier, which may not be guaranteed to
     * be unique, using the provided key
     * <p>
     * @param	key		client provided key to possibly assist in generating the uid
     * @return	the unique identifier
     */	
	public String generateUID( Object key );
}

/*
 * Change Log:
 */



