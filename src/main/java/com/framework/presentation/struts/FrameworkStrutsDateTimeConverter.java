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
import com.framework.common.misc.Utility;

public class FrameworkStrutsDateTimeConverter extends StrutsTypeConverter 
{
    public Object convertFromString(Map context, String[] values, Class toClass) 
    {
    	String indate = values[0];
    	FrameworkDefaultLogger.debug( "***FrameworkStrutsDateTimeConverter.convertFromString() with " + values[0]);    	
        if (indate != null && indate.length() > 0 && indate.trim().length() > 0) 
        {
            try 
            {
            	if ( toClass.getName().equals( "java.sql.Timestamp" ) )
            		return Utility.formatToTimestamp( indate );
            	else if ( toClass.getName().equals( "java.util.Date" ) )
            		return Utility.formatToDate( indate );
            }
            catch(Throwable e) 
            {
            	FrameworkDefaultLogger.error( "DateConverter:convertFromString() - error converting value [" + values[0] + "] to Date " + e );            	
            }
        }
        return null;
    }

    public String convertToString(Map context, Object o) 
    {
    	FrameworkDefaultLogger.debug( "***FrameworkStrutsDateTimeConverter.convertToString() with " + o.toString());    	

        if ( o instanceof java.sql.Timestamp )
        {
        	return( Utility.formatTimestampToString( (java.sql.Timestamp)o ) );
        }
        else if ( o instanceof java.util.Date )
        {
        	return( Utility.formatDateToString( (java.util.Date)o ) );
        }
        return "";
    }
    
    protected Object performFallbackConversion(Map context, Object o, Class toClass)
    {
    	FrameworkDefaultLogger.debug( "***inside of FrameworkStrutsDateTimeConverter.performFallbackConversion() with " + o.getClass().getName() + " and " + toClass.getName() );    	

    	return super.performFallbackConversion(context, o, toClass);
    }    
}

