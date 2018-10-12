/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.ejb;

//************************************
// Imports
//************************************
import javax.ejb.EJBLocalObject;

import com.framework.business.bo.IFrameworkBusinessObject;

/**
 * Base EJB local interface for all Framework entity beans.
 *
 * 
 * <p> 
 * @author    realMethods, Inc.
 */
public interface FrameworkLocal extends EJBLocalObject
{
	/**
	 * Returns an IFrameworkBusinessObject  
	 * @return 		IFrameworkBusinessObject 
	 */
	public IFrameworkBusinessObject getBusinessObject();
    
	/**
	 * External assignment of a business object.  
	 * @param    	bo
	 */
	public void setBusinessObject( IFrameworkBusinessObject bo );

}

/*
 * Change Log:
 * $Log: FrameworkLocal.java,v $
 */
