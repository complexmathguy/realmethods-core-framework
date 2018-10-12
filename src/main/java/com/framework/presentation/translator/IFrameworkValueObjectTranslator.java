/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.presentation.translator;

//***********************************
// Imports
//***********************************

import java.util.Collection;

import com.framework.business.vo.IFrameworkValueObject;

/**
 * Base interface for translator implementation for specific to a particular
 * IFrameworkValueObject implementation.
 * <p>
 * @author    realMethods, Inc.
 */
public interface IFrameworkValueObjectTranslator extends IBaseTranslator
{

    /**
     * Returns a Collection of a form field names.
     * <p>
     * @return      Collection
     */
    public Collection getFormNames();
    
    /**
     * Returns a Collection of a Objects as attributes related to the data of the
     * provided IFrameworkValueObject, which should be of type IFrameworkValueObject.
     * This Collection corresponds to the data required to display a IFrameworkValueObject
     * in a form.  Make sure the order of these attributes corresponds to the order 
     * of the form field names as specified within the 
     * ApplicationResource.properties file.
     * <p>
     * @param       vo
     * @return      Collection
     */
    public Collection getFormAttributes( IFrameworkValueObject vo );
    
    /**
     * Returns a Collection of a Objects as attributes related to the data of the
     * provided IFrameworkValueObject, which should be of type IFrameworkValueObject.
     * This Collection corresponds to the data required to display a IFrameworkValueObject
     * in a list.  Make sure the order of these attributes corresponds to the order 
     * of the form field names as specified within the 
     * ApplicationResource.properties file.
     * <p>
     * @param       vo
     * @return      Collection
     */
    public Collection getListAttributes( IFrameworkValueObject vo );
}

/*
 * Change Log:
 * $Log: IFrameworkValueObjectTranslator.java,v $
 */
