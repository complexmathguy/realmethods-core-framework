/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.exception;

//************************************
// Imports
//************************************

/**
 * Exception class for failure to validate data
 * <p>
 * @author    realMethods, Inc.
 */
public class DataValidationException extends FrameworkCheckedException
{

// constructors
   /**
	* default constructor
	*/
   public DataValidationException()
   {
   }

   /**
	* constructor
	*
	* @param message   text of the exception
	*/
   public DataValidationException( String message )
   {
	   super( message );
   }
    
   /**
	* Constructor with a Throwable for chained exception and a message.
     * @param message
     * @param exception
	*/
   public DataValidationException( String message, Throwable exception )
   {
	   super( message, exception ); 
   }
}

/*
 * Change Log:
 * $Log: DataValidationException,v $
 */



