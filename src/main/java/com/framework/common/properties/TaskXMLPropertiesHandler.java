/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.common.properties;

//***************************
//Imports
//***************************

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Provides property access to the contents of the task related config file.
 * <p>
 * @author		realMethods, Inc.
 */
public class TaskXMLPropertiesHandler 
	extends XMLPropertiesHandler
	implements ITaskPropertiesHandler
{

//************************************************************************    
// Constructors
//************************************************************************


	public TaskXMLPropertiesHandler()
	{
		super();
	}
	
//************************************************************************    
// Public Methods
//************************************************************************
  	
// ITaskPropertiesHandler implementations

   /**
	* Returns a Collection of Strings, representing the names of the declared tasks.
	* @return		task names
	*/
	public Collection getTaskNames()
	{
		return( getFrameworkXMLParser().getAttributesForEachOccurance( "task", "name" ) );
	}
	  
   /**
	* Returns the attribute names and values for the provided task name.
	* @param 	taskName
	* @return	the properties for the named task handler
	*/
   public Map getTaskParams( String taskName )
   {
	   Map params = getFrameworkXMLParser().getAttributesForEachOccuranceBy( "task",
																	   "name",
																	   taskName );    	
	   return( params );   		
   }
   
   /**
	* Returns a Map where the key is the name of a task, and the value is a Map
	* of its values.
	* <p>
	* @return	all task handler properties, where the key is a task hanndler name, and the
	 * 			related value is a Map of its properties 
	*/
   public Map getTasks()
   {   
		Map tasks = (Map)cache( "tasks" );		
		
		if ( tasks == null )
		{
			tasks = new HashMap();
			Collection names 	= getTaskNames();
			Iterator iter		= names.iterator();
			String name			= null;
				
			while( iter.hasNext() )
			{
				name = (String)iter.next();
				tasks.put( name, getTaskParams( name ) );
			}
			
			cache( "tasks", tasks );
		}
		
		return( tasks );  	 
    }
	
//*************************************************************************    
// Protected / Protected Methods
//************************************************************************

//************************************************************************    
// Attributes
//************************************************************************

}

/*
 * Change Log:
 * $Log$
 */
