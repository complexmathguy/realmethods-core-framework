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
import java.util.Map;

/**
 * Interface to the provider of property access to the contents of the task related config file.
 * <p>
 * @author		realMethods, Inc.
 */
public interface ITaskPropertiesHandler extends IPropertiesHandler
{

//************************************************************************    
// Public Methods
//************************************************************************

   /**
   	* Returns a Collection of Strings, representing the names of the declared  tasks.
   	* @return		task names
   	*/
  	public Collection getTaskNames();
  
   /**
   	* Returns the attribute names and values for the provided task name.
   	* @param 	taskName
   	* @return	the properties related to the named task handler  
   	*/
   public Map getTaskParams( String taskName );
  
   /**
	* Returns a Map where the key is the name of a task, and the value is a Map
	* of its values.
	* <p>
	* @return	all task handler properties, where the key is a task hanndler name, and the
	 * 			related value is a Map of its properties 
	*/
   public Map getTasks();
     	
//************************************************************************    
// Attributes
//************************************************************************

}

/*
 * Change Log:
 * $Log$
 */
