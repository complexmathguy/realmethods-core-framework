/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.presentation.taglib;

//***********************************
// Imports
//***********************************
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * TEI class for the TranslatorTagLib.
 * <p>
 * @author    realMethods, Inc.
 */
public class TranslatorTEI extends TagExtraInfo
{
//************************************************************************    
// Public Methods
//************************************************************************
    /** 
     * Base constructor.
     */
    public TranslatorTEI()
    {
        // call base class
        super();    
    }

    /**
     * Retrieves the VariableInfo associated with the Tag.
     * <p>
     * @param data 
     * @return VariableInfo[] Array of VariableInfo's
     */
    public VariableInfo[] getVariableInfo( TagData data )
    {
        return new VariableInfo[]
        {
            new VariableInfo( data.getId(),
                                data.getAttributeString("type"),
                                true,
                                VariableInfo.AT_BEGIN),
        };
    }
    
//************************************************************************    
// Private / Protected Methods
//************************************************************************

//************************************************************************    
// Attributes
//************************************************************************
}

/*
 * Change Log:
 * $Log: TranslatorTEI.java,v $
 */
