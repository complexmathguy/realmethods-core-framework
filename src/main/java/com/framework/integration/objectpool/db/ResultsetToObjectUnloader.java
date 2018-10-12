/************************************************************************ 
 * Copyright 2012-2017 realMethods, Inc.  All rights reserved.
 * This software is the proprietary information of realMethods Inc.
 * Use is subject to license terms.
 *************************************************************************/


package com.framework.integration.objectpool.db;

//************************************
// Imports
//************************************
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.framework.common.FrameworkBaseObject;

import com.framework.common.exception.ResultSetCallbackException;

/**
 * Implementation of the IResultsetCallback that returns
 * a Collection of two ArrayLists: First list is a list
 * containing the column names of the Resultset.  The second list
 * is an ArrayList of Hashmaps.  The key into the Hashmap is the
 * field name and the value is an Object representation of the data.
 * <p>
 * @author    realMethods, Inc.
 */
public class ResultsetToObjectUnloader 
    extends FrameworkBaseObject implements IResultSetCallback
{
//****************************************************
// Public Methods
//****************************************************

//*****************************************	
// IResultSetCallback implementation
//*****************************************

  /**
   * Callback method to notify of result set for a provided stored
   * procedure name
   *
   * @param       rs
   * @param       storedProcedureName
   * @return      Collection of 2 ArrayLists...see class description for format
   * @exception   ResultSetCallbackException
   */
    public Collection notifyResultSet( ResultSet rs, String storedProcedureName )
        throws ResultSetCallbackException
    {
        Collection returnCollection = new ArrayList();
        Collection fieldNameCollection = new ArrayList();
        Collection fieldValueCollection = new ArrayList();
        
        // Now populate the returnCollection with the field name collection
        returnCollection.add(fieldNameCollection);
        // Now populate the returnCollection with the field value collection
        returnCollection.add(fieldValueCollection);

        if ( rs == null )
        {
            throw new ResultSetCallbackException( "ResultsetToStringUnloader:notifyResultSet - null ResultSet parameter." );
        }

        try
        {
            // If there are Records in the resultset
            if ( rs.next() == true )
            {
                // -------------------------------------------------------
                // Retrieve the list of field names from the result set
                // -------------------------------------------------------
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                String currentColumnName = null;
                
                for (int nCounter = 1; nCounter <= columnCount; nCounter++)
                {
                    // Retrieve column Name
                    currentColumnName = metaData.getColumnName(nCounter);
                    
                    // Put column Name in return collection
                    fieldNameCollection.add(currentColumnName);
                }

                // -------------------------------------------------------
                // Now cycle through the field names and retrieve the values as strings
                // -------------------------------------------------------
                Iterator fieldNameIterator = null;
                do
                {
                    // Retrieve an iterator from the field name collection
                    fieldNameIterator = fieldNameCollection.iterator();
                    
                    // Cycle through the iterator and pull the values
                    // from the current record
                    Object currentFieldValue = null;
                    String currentFieldName = null;
                    HashMap recordMap = new HashMap();
                    while (fieldNameIterator.hasNext())
                    {
                        currentFieldName = (String)fieldNameIterator.next();
                        currentFieldValue = rs.getObject(currentFieldName);
                        
                        // Now put the value in the HashMap
                        recordMap.put(currentFieldName, currentFieldValue);
                    }
                    
                    // Now put the hashmap in the return field values collection
                    fieldValueCollection.add(recordMap);
                }while ( rs.next() );
            }
        }
        catch ( Exception e )
        {
            throw new ResultSetCallbackException( "ResultsetToStringUnloader:notifyResultSet - could not access the resultset.", e );
        }

        return( returnCollection );
    }

//****************************************************
// Protected/Private Methods
//****************************************************

//****************************************************
// Attributes
//****************************************************
}

/*
 * Change Log:
 * $Log: ResultsetToObjectUnloader.java,v $
 */
