/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.business.ejb;

//************************************
// Imports
//************************************
import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.framework.business.bo.IFrameworkBusinessObject;

/**
 * Common remote interface for all Framework base generated beans which contain state.
 * <p> 
 * @author    realMethods, Inc.
 */
public interface FrameworkRemote extends EJBObject
{
    /**
    /**
     * returns an IFrameworkBusinessObject  
     *
     * @return 		IFrameworkBusinessObject 
     * @exception	RemoteException
     */
    public IFrameworkBusinessObject getBusinessObject()
    throws RemoteException;
    
    /**
     * External assignment of a model.  
     *
     * @param    	bo
     * @exception	RemoteException
     */
    public void setBusinessObject( IFrameworkBusinessObject bo )
    throws RemoteException;

}

/*
 * Change Log:
 * $Log: FrameworkRemote.java,v $
 */
