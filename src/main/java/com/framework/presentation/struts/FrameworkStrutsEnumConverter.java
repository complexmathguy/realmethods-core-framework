/************************************************************************ 
 * Copyright 2001-2007 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/
package com.framework.presentation.struts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.*;

import org.apache.struts2.util.StrutsTypeConverter;

import com.framework.integration.log.FrameworkDefaultLogger;

public class FrameworkStrutsEnumConverter extends StrutsTypeConverter 
{
    public Object convertFromString(Map context, String[] values, Class toClass) 
    {
    	Enum e = null;

    	if (values == null )
    	{
    		System.out.println( "***FrameworkStrutsEnumConverter.convertFromString() values[] is null " );
    		return e;
    	}

    	if (values.length == 0 )
    	{
    		System.out.println( "***FrameworkStrutsEnumConverter.convertFromString() values[] is empty" );
    		return e;
    	}
    	
    	String enumValue = values[0];
        if (enumValue!= null && enumValue.length() > 0 && enumValue.trim().length() > 0) 
        {
        	e = Enum.valueOf(toClass, enumValue);
        }
//        System.out.println( "***FrameworkStrutsEnumConverter.convertFromString() enumClass: " + toClass + ", enumValue: " + enumValue + ", returning enum: " + e );
    	FrameworkDefaultLogger.debug( "***FrameworkStrutsEnumConverter.convertFromString() enumClass: " + toClass + ", enumValue: " + values[0] + ", returning enum: " + e.toString());    	

        return e;
    }

    public String convertToString(Map context, Object o) 
    {
    	if ( o == null )
    		FrameworkDefaultLogger.debug( "***FrameworkStrutsEnumConverter.convertToString() object arg is null.");
    	
    	FrameworkDefaultLogger.debug( "***FrameworkStrutsEnumConverter.convertToString() with " + o.toString());
    	
    	return( o.toString() );
    }
    
    protected Object performFallbackConversion(Map context, Object o, Class toClass)
    {
    	FrameworkDefaultLogger.debug( "***inside of FrameworkStrutsEnumConverter.performFallbackConversion() with " + o.getClass().getName() + " and " + toClass.getName() );    	

    	return super.performFallbackConversion(context, o, toClass);
    }    
    
}

